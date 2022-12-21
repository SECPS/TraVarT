package at.jku.cps.travart.core.optimize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.logicng.formulas.FormulaFactory;

import at.jku.cps.travart.core.common.IModelOptimizer;
import at.jku.cps.travart.core.factory.impl.CoreModelFactory;
import at.jku.cps.travart.core.helpers.TraVarTUtils;
import de.vill.model.Feature;
import de.vill.model.FeatureModel;
import de.vill.model.Group;
import de.vill.model.constraint.LiteralConstraint;

public class DefaultCoreModelOptimizer implements IModelOptimizer<FeatureModel> {

	private static final CoreModelFactory factory = CoreModelFactory.getInstance();
	private static DefaultCoreModelOptimizer instance;

	public static DefaultCoreModelOptimizer getInstance() {
		if (instance == null) {
			instance = new DefaultCoreModelOptimizer();
		}
		return instance;
	}

	private DefaultCoreModelOptimizer() {

	}

	@Override
	public void optimize(final FeatureModel fm, final OPTIMIZING_LEVEL level) {
		// find mandatory features within feature groups
		fixFalseOptionalFeaturesByFeatureGroupConstraints(fm, TraVarTUtils.getRoot(fm));
		// find mandatory features within abstract feature groups
		fixFalseOptionalFeaturesByAbstractFeatureGroup(fm, TraVarTUtils.getRoot(fm));
		// find mandatory features within requires constraints
		fixFalseOptionalFeaturesByConstraints(fm);
		// find alternative groups
		transformConstraintsToAlternativeGroup(fm, TraVarTUtils.getRoot(fm));
	}

	// TODO: This must be possible during creation of the feature tree. child parent
	// check should do it.
	private static void fixFalseOptionalFeaturesByFeatureGroupConstraints(final FeatureModel fm,
			final Feature feature) {
		final Set<Feature> children = TraVarTUtils.getChildren(feature);
		for (final Feature child : children) {
			fixFalseOptionalFeaturesByFeatureGroupConstraints(fm, child);
		}
		// if there is a requires constraint in the feature model between parent and
		// child, we can remove the constraint and make the child mandatory
		for (final Feature childFeature : children) {
			final de.vill.model.constraint.Constraint requiredConstraint = factory.createImplicationConstraint(
					factory.createLiteralConstraint(TraVarTUtils.getFeatureName(feature)),
					factory.createLiteralConstraint(TraVarTUtils.getFeatureName(childFeature)));
			if (TraVarTUtils.hasOwnConstraint(fm, requiredConstraint)) {
				TraVarTUtils.setGroup(fm, childFeature, feature, Group.GroupType.MANDATORY);
				TraVarTUtils.removeOwnConstraint(fm, requiredConstraint);
			}
		}
	}

	private static void fixFalseOptionalFeaturesByConstraints(final FeatureModel fm) {
		List<de.vill.model.constraint.Constraint> toDelete = new ArrayList<>();
		for (final de.vill.model.constraint.Constraint constr : TraVarTUtils.getOwnConstraints(fm)) {
			final de.vill.model.constraint.Constraint cnf = TraVarTUtils.buildConstraintFromFormula(
					TraVarTUtils.buildFormulaFromConstraint(constr, new FormulaFactory()).cnf());

			if (TraVarTUtils.isRequires(constr)) {
				final de.vill.model.constraint.Constraint left = TraVarTUtils.getLeftConstraint(cnf);
				final de.vill.model.constraint.Constraint right = TraVarTUtils.getRightConstraint(cnf);
				if (left != null && right != null && TraVarTUtils.isLiteral(left) && TraVarTUtils.isLiteral(right)) {
					final Feature leftFeature = ((LiteralConstraint) left).getFeature();
					final Feature rightFeature = ((LiteralConstraint) right).getFeature();

					if (TraVarTUtils.isInGroup(leftFeature, Group.GroupType.MANDATORY)
							&& TraVarTUtils.isInGroup(rightFeature, Group.GroupType.MANDATORY)) {
						TraVarTUtils.setGroup(fm, rightFeature, rightFeature.getParentFeature(),
								Group.GroupType.MANDATORY);
						toDelete.add(constr);
					}
				}
			}
		}
		toDelete.forEach(fm.getOwnConstraints()::remove);
	}

	private static void fixFalseOptionalFeaturesByAbstractFeatureGroup(final FeatureModel fm, final Feature feature) {
		// TODO: think about multiple getChildren calls
		// TODO: check if it works
		final Set<Feature> children = TraVarTUtils.getChildren(feature);
		for (final Feature child : children) {
			fixFalseOptionalFeaturesByAbstractFeatureGroup(fm, child);
		}
		if (!children.isEmpty() && TraVarTUtils.isAbstract(feature)
				&& (TraVarTUtils.checkGroupType(feature, Group.GroupType.OR)
						|| TraVarTUtils.checkGroupType(feature, Group.GroupType.ALTERNATIVE))) {
			// abstract features where all child features are mandatory are also mandatory
			boolean childMandatory = true;
			for (final Feature childFeature : children) {
				childMandatory = childMandatory && TraVarTUtils.checkGroupType(childFeature, Group.GroupType.MANDATORY);
			}
			if (childMandatory) {
				TraVarTUtils.setGroup(fm, feature, feature.getParentFeature(), Group.GroupType.MANDATORY);
			}
		}
	}

	private static void transformConstraintsToAlternativeGroup(final FeatureModel fm, final Feature feature) {
		final Set<Feature> children = TraVarTUtils.getChildren(feature);
		final List<de.vill.model.constraint.Constraint> excludesConstraints = TraVarTUtils.getOwnConstraints(fm)
				.stream().filter(TraVarTUtils::isExcludes).collect(Collectors.toList());
		final Set<de.vill.model.constraint.Constraint> relevantExcludesConstraints = new HashSet<>();
		for (final Feature childFeature : children) {
			transformConstraintsToAlternativeGroup(fm, childFeature);
			final Set<Feature> otherChildren = new HashSet<>(children);
			otherChildren.remove(childFeature);
			for (final de.vill.model.constraint.Constraint constr : excludesConstraints) {
				Set<LiteralConstraint> lcs = otherChildren.stream()
						.map(oc -> factory.createLiteralConstraint(TraVarTUtils.getFeatureName(oc)))
						.collect(Collectors.toSet());
				if (constr.getConstraintSubParts()
						.contains(factory.createLiteralConstraint(TraVarTUtils.getFeatureName(childFeature)))
						&& constr.getConstraintSubParts().stream().anyMatch(lcs::contains)) {
					for (final Feature other : otherChildren) {
						final de.vill.model.constraint.Constraint constraint = factory.createImplicationConstraint(
								factory.createLiteralConstraint(TraVarTUtils.getFeatureName(childFeature)),
								factory.createLiteralConstraint(TraVarTUtils.getFeatureName(other)));
						if (constr.equals(constraint)) {
							relevantExcludesConstraints.add(constr);
						}
					}

				}
			}
		}
		if (isAlternativeGroup(children, relevantExcludesConstraints)) {
			TraVarTUtils.setGroup(fm, feature, feature.getParentFeature(), Group.GroupType.ALTERNATIVE);
			relevantExcludesConstraints.forEach(excludesConstraints::remove);
		}
	}

	private static boolean isAlternativeGroup(final Set<Feature> children,
			final Set<de.vill.model.constraint.Constraint> relevantExcludesConstraints) {
		return !relevantExcludesConstraints.isEmpty()
				&& children.size() * (children.size() - 1) <= relevantExcludesConstraints.size();
	}
}
