package at.jku.cps.travart.core.helpers;

import static at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties.ABSTRACT_ATTRIBUTE;
import static at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties.HIDDEN_ATTRIBUTE;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.logicng.formulas.FType;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.logicng.formulas.Or;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import de.vill.main.UVLModelFactory;
import de.vill.model.Attribute;
import de.vill.model.Feature;
import de.vill.model.FeatureModel;
import de.vill.model.Group;
import de.vill.model.Group.GroupType;
import de.vill.model.Import;
import de.vill.model.constraint.AndConstraint;
import de.vill.model.constraint.Constraint;
import de.vill.model.constraint.EquivalenceConstraint;
import de.vill.model.constraint.ImplicationConstraint;
import de.vill.model.constraint.LiteralConstraint;
import de.vill.model.constraint.NotConstraint;
import de.vill.model.constraint.OrConstraint;
import de.vill.model.constraint.ParenthesisConstraint;

public class TraVarTUtils {
	private static final UVLModelFactory factory = new UVLModelFactory();
	private static final FormulaFactory formulaFactory = new FormulaFactory();

	private TraVarTUtils() {
	}

	public static String[] splitString(final String toSplit, final String delimiter) {
		return Arrays.stream(toSplit.split(delimiter)).map(String::trim).filter(s -> !s.isEmpty() && !s.isBlank())
				.toArray(String[]::new);
	}

	/**
	 * Creates a Set of names of the selected IConfigurable names.
	 *
	 * @param samples - the samples of the feature model.
	 * @return A Set of name sets of the configuration samples.
	 */
	public static Set<Set<String>> createConfigurationNameSet(final Set<Map<IConfigurable, Boolean>> samples) {
		Objects.requireNonNull(samples);
		final Set<Set<String>> configurations = new HashSet<>();
		for (final Map<IConfigurable, Boolean> sample : samples) {
			final Set<String> sampleNames = new HashSet<>();
			for (final Map.Entry<IConfigurable, Boolean> sampleEntry : sample.entrySet()) {
				if (sampleEntry.getValue()) {
					sampleNames.add(sampleEntry.getKey().getName());
				}
			}
			configurations.add(sampleNames);
		}
		return configurations;
	}

	/**
	 * Recursively builds a featureMap starting from the passed root.
	 *
	 * @param feature the root of the tree
	 * @return A map of all features in the tree with their names as keys
	 */
	public static Map<String, Feature> getFeatureMapFromRoot(final Feature feature) {
		Objects.requireNonNull(feature);
		Map<String, Feature> featureMap = new HashMap<>();
		// add self
		featureMap.put(feature.getFeatureName(), feature);
		if (feature.getChildren().isEmpty()) {
			return featureMap;
		}
		List<Feature> childFeatures = feature.getChildren().stream().flatMap(g -> g.getFeatures().stream())
				.collect(Collectors.toList());
		// add all children
		for (Feature childFeature : childFeatures) {
			featureMap.putAll(getFeatureMapFromRoot(childFeature));
		}
		return featureMap;
	}

	/**
	 * This function recursively translates a propositional logic formula from the
	 * data model used in the de.neominik.uvl library to the data model used in the
	 * logicng library, to facilitate the later translation to pure variants
	 * relations. Does not allow expression constraints.
	 *
	 * @param constraint the current node of the propositional formula
	 * @param factory    The FormulaFactory required to create formulas for logicng
	 * @return the logic formula parsed for the logicng library
	 */
	public static Formula buildFormulaFromConstraint(final Constraint constraint, final FormulaFactory factory) {
		Objects.requireNonNull(constraint);
		Objects.requireNonNull(factory);
		Formula term = null;
		if (constraint instanceof ImplicationConstraint) {
			term = factory.implication(
					buildFormulaFromConstraint(((ImplicationConstraint) constraint).getLeft(), factory),
					buildFormulaFromConstraint(((ImplicationConstraint) constraint).getRight(), factory));
		} else if (constraint instanceof EquivalenceConstraint) {
			term = factory.equivalence(
					buildFormulaFromConstraint(((EquivalenceConstraint) constraint).getLeft(), factory),
					buildFormulaFromConstraint(((EquivalenceConstraint) constraint).getRight(), factory));
		} else if (constraint instanceof AndConstraint) {
			term = factory.and(buildFormulaFromConstraint(((AndConstraint) constraint).getLeft(), factory),
					buildFormulaFromConstraint(((AndConstraint) constraint).getRight(), factory));
		} else if (constraint instanceof OrConstraint) {
			term = factory.or(buildFormulaFromConstraint(((OrConstraint) constraint).getLeft(), factory),
					buildFormulaFromConstraint(((OrConstraint) constraint).getRight(), factory));
		} else if (constraint instanceof NotConstraint) {
			term = factory.not(buildFormulaFromConstraint(((NotConstraint) constraint).getContent(), factory));
		} else if (constraint instanceof ParenthesisConstraint) {
			term = buildFormulaFromConstraint(((ParenthesisConstraint) constraint).getContent(), factory);
		} else if (constraint instanceof LiteralConstraint) {
			term = factory.literal(((LiteralConstraint) constraint).getLiteral(), true);
		}
		return term;
	}

	/**
	 * Translates a Formula back to a constraint format.
	 *
	 * @param formula the formula in logicNG format
	 * @return the same formula represented by UVLs Constraint hierarchy.
	 */
	public static Constraint buildConstraintFromFormula(final Formula formula) {
		Objects.requireNonNull(formula);
		// replace negation sign for uvl parser to recognize it.
		return factory.parseConstraint(formula.toString().replace("~", "!"));
	}

	/**
	 * Recursively counts all literals in a given constraint tree.
	 *
	 * @param constraint The constraint to count the literals from
	 * @return the number of literals
	 */
	public static int countLiterals(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		if (constraint instanceof LiteralConstraint) {
			return 1;
		}
		int i = 0;
		for (final Constraint subconst : constraint.getConstraintSubParts()) {
			i += countLiterals(subconst);
		}
		return i;
	}

	/**
	 * Iterate over the {@code createConfigurationNameSet} configurations and find
	 * the common feature names of the configurations
	 *
	 * @param samples - the samples of the feature model.
	 * @return A Set of name sets of the configuration samples.
	 */
	public static Set<String> getCommonConfigurationNameSet(final Set<Map<IConfigurable, Boolean>> samples) {
		final Set<Set<String>> configurations = createConfigurationNameSet(samples);
		final Iterator<Set<String>> iterator = configurations.iterator();
		Set<String> element = iterator.next();
		iterator.remove();
		final Set<String> commonNames = new HashSet<>(element);
		while (iterator.hasNext()) {
			element = iterator.next();
			commonNames.retainAll(element);
		}
		return commonNames;
	}

	/**
	 * checks if a feature is the child of another one.
	 *
	 * @param child  the feature to check
	 * @param parent the proposed parent feature
	 * @return boolean if child is - in fact - a child of parent
	 */
	public static boolean isParentOf(final Feature child, final Feature parent) {
		if (child.getParentFeature() == null) {
			return false;
		}

		return child.getParentFeature().equals(parent);
	}

	/**
	 * Check if the given Feature should be translated into an Enumeration Type
	 * Decision for the DecisionModel transformation
	 *
	 * @param feature the feature to check
	 * @return boolean if feature should be enumeration decision
	 */
	public static boolean isEnumerationType(final Feature feature) {
		return feature.getChildren().stream().anyMatch(group -> (group.GROUPTYPE.equals(GroupType.ALTERNATIVE)
				|| group.GROUPTYPE.equals(GroupType.OR) || group.GROUPTYPE.equals(GroupType.GROUP_CARDINALITY)));
	}

	/**
	 * checks if the passed feature has the Abstract-attribute
	 *
	 * @param feature the feature to check
	 * @return true abstract attribute is present, false otherwise.
	 */
	public static boolean isAbstract(final Feature feature) {
		return feature.getAttributes().get(ABSTRACT_ATTRIBUTE) != null
				&& Boolean.parseBoolean(feature.getAttributes().get(ABSTRACT_ATTRIBUTE).getValue().toString());
	}

	/**
	 * checks if the passed feature has the Hidden-attribute
	 *
	 * @param feature the feature to check
	 * @return true hidden attribute is present, false otherwise.
	 */
	public static boolean isHidden(final Feature feature) {
		return feature.getAttributes().get(HIDDEN_ATTRIBUTE) != null
				&& Boolean.parseBoolean(feature.getAttributes().get(HIDDEN_ATTRIBUTE).getValue().toString());
	}

	/**
	 * get the value of a given attribute key
	 *
	 * @param feature       the feature from which to extract the attribute value
	 * @param attributeName the key for the attribute
	 * @return the value of the attribute
	 */
	public static Object getAttributeValue(final Feature feature, final String attributeName) {
		return feature.getAttributes().get(attributeName) == null ? null
				: feature.getAttributes().get(attributeName).getValue();
	}

	/**
	 * get a list of all child features of a feature. This pools features from ALL
	 * groups
	 *
	 * @param feature the feature from which to get all children
	 * @return a List of all Features
	 */
	public static Set<Feature> getChildren(final Feature feature) {
		final Set<Feature> children = new HashSet<>();
		feature.getChildren().forEach(group -> children.addAll(group.getFeatures()));

		return children;
	}

	/**
	 * Checks if the given feature is contained within a group of a given type.
	 *
	 * @param feature   the feature to check
	 * @param groupType the assumed/proposed {@link Group.GroupType GrouptType}
	 * @return
	 */
	public static boolean checkGroupType(final Feature feature, final Group.GroupType groupType) {
		return feature.getParentGroup().GROUPTYPE.equals(groupType);
	}

	/**
	 * checks whether the constraint is complex. A constraint counts as complex if
	 * it's depth is greater than 2.
	 *
	 * @param constraint the constraint to check
	 * @return true if the constraint is complex, false otherwise
	 */
	public static boolean isComplexConstraint(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		Formula f = TraVarTUtils.buildFormulaFromConstraint(constraint, formulaFactory);
		Objects.requireNonNull(f);
		return f.stream().anyMatch(subf -> !subf.isAtomicFormula());
	}

	/**
	 * Detects if the given constraint is a Requires-Constraint. A constraint like
	 * this could be of the form A => B A => B | C !A | B | C
	 *
	 * @param constraint The constraint the check
	 * @return Boolean if constraint is requires-constraint
	 */
	public static boolean isRequires(final Constraint constraint) {
		Formula formula = TraVarTUtils.buildFormulaFromConstraint(constraint, formulaFactory);
		Formula cnfFormula = formula.cnf();
		return cnfFormula instanceof Or && TraVarTUtils.countNegativeFormulaLiterals(cnfFormula) == 1
				&& TraVarTUtils.countPositiveFormulaLiterals(cnfFormula) > 0;
	}

	/**
	 * Detects if the given constraint is a RequiresForAll-Constraint. A constraint
	 * like this could be of the form (A & B) => C !A | !B | C
	 *
	 * @param constraint The constraint to check
	 * @return boolean if constraint is a RequiresForAll-constraint
	 */
	public static boolean isRequiredForAllConstraint(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		Formula formula = TraVarTUtils.buildFormulaFromConstraint(constraint, formulaFactory);
		Formula cnfFormula = formula.cnf();
		return cnfFormula instanceof Or && TraVarTUtils.countPositiveFormulaLiterals(cnfFormula) == 1
				&& TraVarTUtils.countNegativeFormulaLiterals(cnfFormula) > 1;
	}

	/**
	 * counts all positive literals in a given formula
	 *
	 * @param formula the formula to check
	 * @return the amount of positive literals as long
	 */
	public static long countPositiveFormulaLiterals(final Formula formula) {
		Objects.requireNonNull(formula);
		return countFormulaLiterals(formula, false);
	}

	/**
	 * counts all negative literals in a given formula
	 *
	 * @param formula the formula to check
	 * @return the amount of negative literals as long
	 */
	public static long countNegativeFormulaLiterals(final Formula formula) {
		Objects.requireNonNull(formula);
		return countFormulaLiterals(formula, true);
	}

	/**
	 * private helper method for counting positive and negative literals
	 *
	 * @param formula the formula to count literals of
	 * @param negated true to search for negated literals, false to check for
	 *                positive ones
	 * @return amount of found literals as long
	 */
	private static long countFormulaLiterals(final Formula formula, final boolean negated) {
		Objects.requireNonNull(formula);
		if (negated) {
			return formula.literals().stream().filter(lit -> !lit.phase()).count();
		}

		return formula.literals().stream().filter(Literal::phase).count();
	}

	/**
	 * Gets the first negative literal that is within the constraint
	 *
	 * @param constraint the constraint from which to get the first negated literal
	 *                   from
	 * @return The negated Literal as NotConstraint
	 */
	public static NotConstraint getFirstNegativeLiteral(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		if (isNegativeLiteral(constraint)) {
			return (NotConstraint) constraint;
		}
		for (final Constraint child : constraint.getConstraintSubParts()) {
			NotConstraint childsFirstNegative = getFirstNegativeLiteral(child);
			if (childsFirstNegative != null && isNegativeLiteral(childsFirstNegative)) {
				return childsFirstNegative;
			}
		}
		return null;
	}

	/**
	 * Get's the first positive Literal in a constraint.
	 *
	 * @param constraint the constraint from which to get the literal from
	 * @return the first found positive literal
	 */
	public static Constraint getFirstPositiveLiteral(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		if (isPositiveLiteral(constraint)) {
			return constraint;
		}
		for (final Constraint child : constraint.getConstraintSubParts()) {
			Constraint childsFirstPositive = getFirstPositiveLiteral(child);
			if (childsFirstPositive != null && isPositiveLiteral(childsFirstPositive)) {
				return childsFirstPositive;
			}
		}
		return null;
	}

	/**
	 * checks if the constraint is an excludes constraint. If a constraint is
	 * deconstructed into a CNF form, and all literals are negative it is an
	 * excludes constraint. In all other cases it's not.
	 *
	 * @param constraint a constraint of arbitrary form.
	 * @return true if constraint is an exludes constraint, false otherwise
	 */
	public static boolean isExcludes(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		Formula formula = buildFormulaFromConstraint(constraint, formulaFactory);
		formula = formula.cnf();
		List<Literal> positiveLiterals = formula.literals().stream().filter(Literal::phase)
				.collect(Collectors.toList());
		List<Literal> negativeLiterals = formula.literals().stream().filter(lit -> !lit.phase())
				.collect(Collectors.toList());
		return formula.type().equals(FType.OR) && positiveLiterals.isEmpty() && !negativeLiterals.isEmpty();
	}

	/**
	 * Checks whether the passed constraint is a negated Literal
	 *
	 * @param constraint the constraint to check
	 * @return true if given constraint is a negated Literal, else false
	 */
	public static boolean isNegativeLiteral(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		return constraint instanceof NotConstraint
				&& ((NotConstraint) constraint).getContent() instanceof LiteralConstraint;
	}

	/**
	 * Checks whether the passed constraint is a Literal
	 *
	 * @param constraint the constraint to check
	 * @return true if given constraint is a Literal, else false
	 */
	public static boolean isPositiveLiteral(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		return constraint instanceof LiteralConstraint;
	}

	/**
	 * How deep does the rabbit hole go? Checks the depth of the given constraint.
	 *
	 * @param constraint the constraint to check
	 * @return the highest depth of the constraint as long
	 */
	public static long getMaxDepth(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		long count = 1;
		for (final Constraint child : constraint.getConstraintSubParts()) {
			final long childCount = getMaxDepth(child) + 1;
			if (childCount > count) {
				count = childCount;
			}
		}
		return count;
	}

	/**
	 * if the given constraint can have a right sub-constraint, it returns
	 * it.Otherwise null.
	 *
	 * @param constraint the constraint from which to get the right part of
	 * @return the right sub-constraint
	 */
	public static Constraint getRightConstraint(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		final List<Method> methods = Arrays.asList(constraint.getClass().getMethods());
		final Optional<Method> getRightMethod = methods.stream().filter(m -> m.getName().equals("getRight")).findAny();
		if (getRightMethod.isPresent()) {
			try {
				return (Constraint) getRightMethod.get().invoke(constraint);
			} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * if the given constraint can have a left sub-constraint, it returns it.
	 * Otherwise null.
	 *
	 * @param constraint the constraint from which to get the left part of
	 * @return the left sub-constraint
	 */
	public static Constraint getLeftConstraint(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		final List<Method> methods = Arrays.asList(constraint.getClass().getMethods());
		final Optional<Method> getLeftMethod = methods.stream().filter(m -> m.getName().equals("getLeft")).findAny();
		if (getLeftMethod.isPresent()) {
			try {
				return (Constraint) getLeftMethod.get().invoke(constraint);
			} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Finds all features in a featuremap that do not have a parent are therefore
	 * roots.
	 *
	 * @param featureMap featuremap containing all features of an UVL model
	 * @return A list of features that don't have a parent
	 */
	private static List<Feature> findRoots(final Map<String, Feature> featureMap) {
		Objects.requireNonNull(featureMap);
		// return all features with no parent
		return featureMap.values().stream().filter(f -> f.getParentFeature() == null).collect(Collectors.toList());
	}

	/**
	 * Returns a valid feature tree for an UVL model. If more than one root are
	 * contained in the feature map, they are united under one single virtual root.
	 *
	 * @param featureMap featuremap containing all features of an UVL model
	 * @param rootName   Name of the artificial root
	 * @return name of the root feature
	 */
	public static String deriveFeatureModelRoot(final Map<String, Feature> featureMap, final String rootName) {
		Objects.requireNonNull(featureMap);
		Objects.requireNonNull(rootName);
		final List<Feature> roots = findRoots(featureMap);
		if (roots.isEmpty()) {
			return null;
		}
		if (roots.size() > 1) {
			// artificial root - abstract and hidden
			final Feature artificialRoot = new Feature(rootName);
			artificialRoot.getAttributes().put(ABSTRACT_ATTRIBUTE, new Attribute<>(ABSTRACT_ATTRIBUTE, true));
			artificialRoot.getAttributes().put("hidden", new Attribute<>("hidden", true));
			// add property to identify the virtual root
			artificialRoot.getAttributes().put("ARTIFICIAL_MODEL_NAME", new Attribute<>("ARTIFICIAL_MODEL_NAME",
					DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME));
			final Group mandatoryGroup = new Group(GroupType.MANDATORY);
			mandatoryGroup.getFeatures().addAll(roots);
			artificialRoot.addChildren(mandatoryGroup);
			featureMap.put(rootName, artificialRoot);

			return rootName;
		}

		final Feature root = roots.get(0);
		return root.getFeatureName();
	}

	/**
	 * Tests if the given constraint is a constraint that requires a single
	 * constraint for a single other one
	 *
	 * @param constraint the constraint to thest
	 * @return boolean signaling if the constraint is a single feature that requires
	 *         another.
	 */
	public static boolean isSingleFeatureRequires(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		Formula formula = TraVarTUtils.buildFormulaFromConstraint(constraint, formulaFactory);
		return formula instanceof Or && countNegativeFormulaLiterals(formula) == 1
				&& countPositiveFormulaLiterals(formula) == 1;
	}

	/**
	 * Checks whether the passed constraint is a excludes constraint where exactly
	 * two features exclude each other. The constraint can have one of these two
	 * forms: 1. !A | !B 2. A => !B
	 *
	 * @param constraint the constraint to be checked
	 * @return true if the constraint has one of the given structures, else false
	 */
	public static boolean isSingleFeatureExcludes(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		if (constraint instanceof OrConstraint) {
			final OrConstraint orConstraint = (OrConstraint) constraint;
			return isNegativeLiteral(orConstraint.getLeft()) && isNegativeLiteral(orConstraint.getRight());
		}
		if (constraint instanceof ImplicationConstraint) {
			final ImplicationConstraint implConstraint = (ImplicationConstraint) constraint;
			return isPositiveLiteral(implConstraint.getLeft()) && isNegativeLiteral(implConstraint.getRight());
		}
		return false;
	}

	/**
	 * Checks whether a negated literal exists in the constraint
	 *
	 * @param constraint the constraint to check
	 * @return true if there is a negated literal, else false
	 */
	public static boolean hasNegativeLiteral(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		return countNegativeLiterals(constraint) > 0;
	}

	/**
	 * Counts the amount of negative literals within a constraint
	 *
	 * @param constraint the constraint to inspect
	 * @return the amount of found negative literals as long
	 */
	public static long countNegativeLiterals(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		Formula formula = buildFormulaFromConstraint(constraint, formulaFactory);
		return countNegativeFormulaLiterals(formula);
	}

	/**
	 * Checks whether a positive literal exists in the constraint
	 *
	 * @param constraint the constraint to check
	 * @return true if there is a positive literal, else false
	 */
	public static boolean hasPositiveLiteral(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		return countPositiveLiterals(constraint) > 0;
	}

	/**
	 * Counts the amount of positive literals within a constraint
	 *
	 * @param constraint the constraint to inspect
	 * @return the amount of found positive literals as long
	 */
	public static long countPositiveLiterals(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		Formula formula = buildFormulaFromConstraint(constraint, formulaFactory);
		return countPositiveFormulaLiterals(formula);
	}

	/**
	 * get all contained negative literals within a constraint as a {@link Set Set}
	 *
	 * @param constraint the constraint from which to get the literals
	 * @return all negated Literals within the constraint as a {@link Set Set}
	 */
	public static Set<Constraint> getNegativeLiterals(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		final Set<Constraint> literals = new HashSet<>();
		if (constraint instanceof NotConstraint
				&& ((NotConstraint) constraint).getContent() instanceof LiteralConstraint) {
			literals.add(((NotConstraint) constraint).getContent());
			return literals;
		}
		if (constraint instanceof ParenthesisConstraint) {
			literals.addAll(getNegativeLiterals(((ParenthesisConstraint) constraint).getContent()));
		}
		for (final Constraint subConst : constraint.getConstraintSubParts()) {
			literals.addAll(getNegativeLiterals(subConst));
		}

		return literals;
	}

	/**
	 * get all contained literals within a constraint as a {@link Set Set}
	 *
	 * @param constraint the constraint from which to get the literals
	 * @return all Literals within the constraint as a {@link Set Set}
	 */
	public static Set<Constraint> getLiterals(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		final Set<Constraint> literals = new HashSet<>();
		if (constraint instanceof LiteralConstraint) {
			literals.add(constraint);
			return literals;
		}
		if (constraint instanceof ParenthesisConstraint) {
			literals.addAll(getLiterals(((ParenthesisConstraint) constraint).getContent()));
		}
		for (final Constraint subConst : constraint.getConstraintSubParts()) {
			literals.addAll(getLiterals(subConst));
		}
		return literals;
	}

	/**
	 * Checks whether the given constraint is a literal. Also includes negated ones.
	 *
	 * @param constraint the constraint to check
	 * @return true if a (negated) Literal, else false
	 */
	public static boolean isLiteral(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		return constraint instanceof LiteralConstraint || constraint instanceof NotConstraint
				&& ((NotConstraint) constraint).getContent() instanceof LiteralConstraint;
	}

	public static Set<Constraint> getPositiveLiterals(final Constraint constraint) {
		Objects.requireNonNull(constraint);
		final Set<Constraint> literals = new HashSet<>();
		if (constraint instanceof LiteralConstraint) {
			literals.add(constraint);
			return literals;
		}
		if (constraint instanceof ParenthesisConstraint) {
			literals.addAll(getPositiveLiterals(((ParenthesisConstraint) constraint).getContent()));
		}
		for (final Constraint subConst : constraint.getConstraintSubParts()) {
			literals.addAll(getPositiveLiterals(subConst));
		}
		return literals;
	}

	/**
	 * Moves the given feature from one Group to a group of specified type under the
	 * given parent feature.
	 *
	 * @param featureModel the feature model containing all features
	 * @param feature      the feature to move
	 * @param parent       the new parent feature
	 * @param groupType    the {@link Group.GroupType GroupType} to add feature to
	 */
	public static void setGroup(final FeatureModel featureModel, final Feature feature, final Feature parent,
			final GroupType groupType) {
		Objects.requireNonNull(featureModel);
		Objects.requireNonNull(feature);
		feature.getParentGroup().getFeatures().remove(feature);
		Optional<Group> optGroup = parent.getChildren().stream().filter(g -> g.GROUPTYPE.equals(groupType)).findFirst();
		Group group = optGroup.isPresent() ? optGroup.get() : new Group(groupType);
		group.getFeatures().add(feature);
		parent.addChildren(group);
		feature.setParentGroup(group);
		featureModel.getFeatureMap().put(parent.getFeatureName(), parent);
		featureModel.getFeatureMap().put(feature.getFeatureName(), feature);
	}

	public static FeatureModel createSingleFeatureModel(final FeatureModel... featureModels) {
		FeatureModel singleFm = new FeatureModel();
		for (FeatureModel fm : featureModels) {
			Import imp = new Import(fm.getRootFeature().getFeatureName(), fm.getRootFeature().getFeatureName());
			singleFm.getImports().add(imp);
			singleFm.getFeatureMap().put(fm.getRootFeature().getFeatureName(), fm.getRootFeature());
		}
		return singleFm;
	}

	public static FeatureModel createSingleFeatureModel(final String modelName, final FeatureModel... featureModels) {
		FeatureModel singleFm = createSingleFeatureModel(featureModels);
		TraVarTUtils.deriveFeatureModelRoot(singleFm.getFeatureMap(), modelName);
		return singleFm;
	}
}