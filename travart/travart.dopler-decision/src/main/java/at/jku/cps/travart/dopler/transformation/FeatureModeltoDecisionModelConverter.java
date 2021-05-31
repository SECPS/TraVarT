package at.jku.cps.travart.dopler.transformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.prop4j.Literal;
import org.prop4j.Node;

import at.jku.cps.travart.core.common.IModelTransformer;
import at.jku.cps.travart.core.common.Prop4JUtils;
import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;
import at.jku.cps.travart.dopler.decision.exc.ConditionCreationException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.factory.impl.DecisionModelFactory;
import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.AllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.DecisionValueCondition;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Not;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.SelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;
import at.jku.cps.travart.dopler.decision.parser.ConditionParser;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class FeatureModeltoDecisionModelConverter implements IModelTransformer<IFeatureModel, IDecisionModel> {

	private DecisionModelFactory factory;
	private IFeatureModel fm;
	private DecisionModel dm;

	@Override
	public DecisionModel transform(final IFeatureModel fm) throws NotSupportedVariablityTypeException {
		this.fm = fm;
		try {
			factory = DecisionModelFactory.getInstance();
			dm = factory.create();
			dm.setName(FeatureUtils.getName(FeatureUtils.getRoot(fm)));
			convertFeature(FeatureUtils.getRoot(fm));
			convertConstraints(FeatureUtils.getConstraints(fm));
			convertVisibilityCustomProperties(FeatureUtils.getFeatures(fm));
			return dm;
		} catch (CircleInConditionException | ConditionCreationException e) {
			throw new NotSupportedVariablityTypeException(e);
		}
	}

	private void convertFeature(final IFeature feature) throws NotSupportedVariablityTypeException {
		if (!FeatureUtils.getName(feature).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME)) {
			BooleanDecision decision = factory.createBooleanDecision(feature.getName());
			dm.add(decision);
			decision.setQuestion(feature.getName() + "?");
			if (TraVarTUtils.isEnumerationType(feature)) {
				createEnumDecisions(feature);
			}
			if (FeatureUtils.isRoot(feature)) {
				decision.setVisibility(ICondition.TRUE);
			} else if (isEnumSubFeature(feature) && !hasVirtualParent(feature)) {
				decision.setVisibility(ICondition.FALSE);
			} else if (FeatureUtils.isMandatorySet(feature) && !hasVirtualParent(feature)) {
				String parentName = FeatureUtils.getName(FeatureUtils.getParent(feature));
				// as tree traversal, the parent should be dealt with already
				IDecision parent = dm.find(parentName);
				assert parent != null;
				Rule rule = new Rule(parent, new SelectDecisionAction(decision));
				parent.addRule(rule);
				updateRules(parent, rule);
				decision.setVisibility(new And(ICondition.FALSE, parent));
			} else if (!hasVirtualParent(feature)) {
				IFeature parentFeature = FeatureUtils.getParent(feature);
				Objects.requireNonNull(parentFeature);
				IDecision parent = dm.find(parentFeature.getName());
				decision.setVisibility(parent);
			} else {
				decision.setVisibility(ICondition.TRUE);
			}
		}
		for (IFeature child : FeatureUtils.getChildren(feature)) {
			if (FeatureUtils.getName(feature).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME)
					|| !isEnumSubFeature(child)) {
				convertFeature(child);
			}
		}
	}

	private boolean hasVirtualParent(final IFeature feature) {
		IFeature parent = FeatureUtils.getParent(feature);
		if (parent != null) {
			return FeatureUtils.getName(parent).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME);
		}
		return false;
	}

	private void createEnumDecisions(final IFeature feature) throws NotSupportedVariablityTypeException {
		// in FeatureIDE only one feature group is possible per feature
		EnumDecision enumDecision = factory.createEnumDecision(feature.getName() + "_0");
		dm.add(enumDecision);
		enumDecision.setQuestion("Choose your " + feature.getName() + "?");
		defineCardinality(enumDecision, feature);
		defineRange(enumDecision, feature);
		IDecision parent = dm.find(feature.getName());
		enumDecision.setVisibility(parent);
		for (IFeature optionFeature : FeatureUtils.getChildren(feature)) {
			convertFeature(optionFeature);
			BooleanDecision optionDecision = (BooleanDecision) dm.find(optionFeature.getName());
			ARangeValue optionValue = enumDecision.getRangeValue(optionFeature.getName());
			// as tree traversal, the parent should be dealt with already
			assert optionDecision != null;
			Rule rule = new Rule(new DecisionValueCondition(enumDecision, optionValue),
					new SelectDecisionAction(optionDecision));
			enumDecision.addRule(rule);
			updateRules(enumDecision, rule);
		}
		// create rule for optional parent features
		if (!FeatureUtils.isRoot(feature) && !FeatureUtils.isMandatorySet(feature)) {
			Rule rule = new Rule(new Not(parent), new SetValueAction(enumDecision, enumDecision.getNoneOption()));
			parent.addRule(rule);
			updateRules(parent, rule);
		}
	}

	private void convertConstraints(final List<IConstraint> constraints)
			throws CircleInConditionException, ConditionCreationException {
		for (IConstraint constraint : constraints) {
			convertConstraintNodeRec(constraint.getNode());
		}
	}

	private void convertConstraintNodeRec(final Node node)
			throws CircleInConditionException, ConditionCreationException {
		// create a CNF from nodes enables separating the concerns how to transform the
		// different groups.
		// A requires B <=> CNF: Not(A) or B
		// A excludes B <=> CNF: Not(A) or Not(B)
		Node cnfNode = node.toCNF();
		if (Prop4JUtils.isComplexNode(cnfNode)) {
			for (Node child : cnfNode.getChildren()) {
				convertConstraintNodeRec(child);
			}
		} else {
			convertConstraintNode(cnfNode);
		}
	}

	private void deriveUnidirectionalRules(Node cnfNode) throws CircleInConditionException, ConditionCreationException {
		Node negative = Prop4JUtils.getFirstNegativeLiteral(cnfNode);
		String feature = Prop4JUtils.getLiteralName((Literal) negative);
		IDecision decision = dm.find(feature);
		if (DecisionModelUtils.isBooleanDecision(decision)) {
			BooleanDecision target = DecisionModelUtils.toBooleanDecision(decision);
			Set<Node> positiveLiterals = Prop4JUtils.getPositiveLiterals(cnfNode);
			List<IDecision> conditionDecisions = findDecisionsForLiterals(positiveLiterals);
			List<IDecision> ruleDecisions = findDecisionsForLiterals(positiveLiterals);
			ICondition ruleCondition = DecisionModelUtils.consumeToBinaryCondition(conditionDecisions, And.class, true);
			Rule rule = new Rule(ruleCondition, new DeSelectDecisionAction(target));
			// add new rule to all rule decisions
			for (IDecision source : ruleDecisions) {
				source.addRule(rule);
				updateRules(source, rule);
			}
		}
	}

	private void convertConstraintNode(final Node cnfNode)
			throws CircleInConditionException, ConditionCreationException {
		if (Prop4JUtils.isRequires(cnfNode)) {
			deriveRequiresRules(cnfNode);
		} else if (Prop4JUtils.isExcludes(cnfNode)) {
			deriveExcludeRules(cnfNode);
		} else if (Prop4JUtils.countLiterals(cnfNode) == 2 && Prop4JUtils.hasNegativeLiteral(cnfNode)
				&& Prop4JUtils.countNegativeLiterals(cnfNode) == 1) {
			deriveUnidirectionalRules(cnfNode);
		} else if (Prop4JUtils.countLiterals(cnfNode) > 1 && Prop4JUtils.hasNegativeLiteral(cnfNode)
				&& Prop4JUtils.countNegativeLiterals(cnfNode) == 1) {
			deriveUnidirectionalRules(cnfNode);
		} else if (Prop4JUtils.countLiterals(cnfNode) == 2 && Prop4JUtils.hasPositiveLiteral(cnfNode)
				&& Prop4JUtils.countPositiveLiterals(cnfNode) == 1) {
			Node positive = Prop4JUtils.getFirstPositiveLiteral(cnfNode);
			String feature = Prop4JUtils.getLiteralName((Literal) positive);
			IDecision decision = dm.find(feature);
			if (DecisionModelUtils.isBooleanDecision(decision)) {
				BooleanDecision target = DecisionModelUtils.toBooleanDecision(decision);
				Set<Node> negativeLiterals = Prop4JUtils.getNegativeLiterals(cnfNode);
				List<IDecision> conditionDecisions = findDecisionsForLiterals(negativeLiterals);
				List<IDecision> ruleDecisions = findDecisionsForLiterals(negativeLiterals);
				ICondition ruleCondition = DecisionModelUtils.consumeToBinaryCondition(conditionDecisions, And.class,
						false);
				Rule rule = new Rule(ruleCondition, new SelectDecisionAction(target));
				// add new rule to all rule decisions
				for (IDecision source : ruleDecisions) {
					source.addRule(rule);
					updateRules(source, rule);
				}
			}
		} else if (Prop4JUtils.countLiterals(cnfNode) > 1 && Prop4JUtils.hasPositiveLiteral(cnfNode)
				&& Prop4JUtils.countPositiveLiterals(cnfNode) == 1) {
			Node positive = Prop4JUtils.getFirstPositiveLiteral(cnfNode);
			String feature = Prop4JUtils.getLiteralName((Literal) positive);
			IDecision decision = dm.find(feature);
			if (DecisionModelUtils.isBooleanDecision(decision)) {
				BooleanDecision target = DecisionModelUtils.toBooleanDecision(decision);
				Set<Node> negativeLiterals = Prop4JUtils.getNegativeLiterals(cnfNode);
				List<IDecision> conditionDecisions = findDecisionsForLiterals(negativeLiterals);
				List<IDecision> ruleDecisions = findDecisionsForLiterals(negativeLiterals);
				ICondition ruleCondition = DecisionModelUtils.consumeToBinaryCondition(conditionDecisions, And.class,
						false);
				Rule rule = new Rule(ruleCondition, new SelectDecisionAction(target));
				// add new rule to all rule decisions
				for (IDecision source : ruleDecisions) {
					source.addRule(rule);
					updateRules(source, rule);
				}
			}
		} else if (Prop4JUtils.countLiterals(cnfNode) > 1
				&& Prop4JUtils.countLiterals(cnfNode) == Prop4JUtils.countPositiveLiterals(cnfNode)
				&& Prop4JUtils.isOr(cnfNode)) {
			Set<Node> literals = Prop4JUtils.getLiterals(cnfNode);
			List<IDecision> literalDecisions = findDecisionsForLiterals(literals);
			for (IDecision decision : literalDecisions) {
				if (DecisionModelUtils.isBooleanDecision(decision)) {
					BooleanDecision boolDecision = (BooleanDecision) decision;
					List<IDecision> conditionDecisions = findDecisionsForLiterals(literals);
					conditionDecisions.remove(decision);
					ICondition condition = DecisionModelUtils.consumeToBinaryCondition(conditionDecisions, And.class,
							true);
					Rule rule = new Rule(condition, new SelectDecisionAction(boolDecision));
					List<IDecision> ruleDecisions = findDecisionsForLiterals(literals);
					ruleDecisions.remove(decision);
					for (IDecision ruleDecision : ruleDecisions) {
						ruleDecision.addRule(rule);
						updateRules(ruleDecision, rule);
					}
				}
			}
		} else if (Prop4JUtils.countLiterals(cnfNode) == 1 && Prop4JUtils.isLiteral(cnfNode)) {
			if (Prop4JUtils.hasPositiveLiteral(cnfNode)) {
				String feature = Prop4JUtils.getLiteralName((Literal) cnfNode);
				IDecision decision = dm.find(feature);
				if (DecisionModelUtils.isBooleanDecision(decision)) {
					BooleanDecision boolDecision = DecisionModelUtils.toBooleanDecision(decision);
					if (DecisionModelUtils.isDecision(decision.getVisiblity())) {
						IDecision source = (IDecision) decision.getVisiblity();
						Rule rule = new Rule(source, new SelectDecisionAction(boolDecision));
						source.addRule(rule);
						updateRules(source, rule);
					} else {
						Rule rule = new Rule(new Not(boolDecision), new SelectDecisionAction(boolDecision));
						boolDecision.addRule(rule);
						updateRules(boolDecision, rule);
					}
				}
			} else {
				String feature = Prop4JUtils.getLiteralName((Literal) cnfNode);
				IDecision decision = dm.find(feature);
				if (DecisionModelUtils.isBooleanDecision(decision)) {
					BooleanDecision boolDecision = DecisionModelUtils.toBooleanDecision(decision);
					Rule rule = new Rule(new Not(boolDecision), new DeSelectDecisionAction(boolDecision));
					boolDecision.addRule(rule);
					updateRules(boolDecision, rule);
				}
			}
		} else {
			System.out.println("Could not transform CNF: " + cnfNode);
		}
	}

	private void deriveRequiresRules(final Node cnfNode) {
		Node sourceLiteral = Prop4JUtils.getFirstNegativeLiteral(cnfNode);
		Node targetLiteral = Prop4JUtils.getFirstPositiveLiteral(cnfNode);
		if (Prop4JUtils.isLiteral(sourceLiteral) && Prop4JUtils.isLiteral(targetLiteral)) {
			String sourceFeature = Prop4JUtils.getLiteralName((Literal) sourceLiteral);
			String targetFeature = Prop4JUtils.getLiteralName((Literal) targetLiteral);
			IDecision source = dm.find(sourceFeature);
			IDecision target = dm.find(targetFeature);
			if (isEnumFeature(source) && !isEnumFeature(target)) {
				EnumDecision enumDecision = findEnumDecision(source);
				if (enumDecision.hasNoneOption()) {
					Rule rule = new Rule(
							new Not(new DecisionValueCondition(enumDecision, enumDecision.getNoneOption())),
							new SelectDecisionAction(DecisionModelUtils.toBooleanDecision(target)));
					enumDecision.addRule(rule);
					updateRules(enumDecision, rule);
				}
			} else if (!isEnumFeature(source) && isEnumFeature(target)) {
				// the none value of the enum feature if available must be disallowed
				EnumDecision enumDecision = findEnumDecision(target);
				if (enumDecision.hasNoneOption()) {
					Rule rule = new Rule(source, new DisAllowAction(enumDecision, enumDecision.getNoneOption()));
					source.addRule(rule);
					updateRules(source, rule);
				}
			} else if (!hasVirtualParent(target) && isEnumSubFeature(target)) {
				EnumDecision enumDecision = findEnumDecision(target);
				ARangeValue value = enumDecision.getRangeValue(targetFeature);
				Rule rule = new Rule(source, new SetValueAction(enumDecision, value));
				source.addRule(rule);
				updateRules(source, rule);
			} else if (DecisionModelUtils.isBooleanDecision(target)) {
				Rule rule = new Rule(source, new SelectDecisionAction(DecisionModelUtils.toBooleanDecision(target)));
				source.addRule(rule);
				updateRules(source, rule);
			}
		}
	}

	private boolean hasVirtualParent(final IDecision decision) {
		IFeature feature = fm.getFeature(DecisionModelUtils.retriveFeatureName(decision, dm.isAddPrefix(),
				DecisionModelUtils.isEnumDecision(decision)));
		IFeature parent = FeatureUtils.getParent(feature);
		return FeatureUtils.getName(parent).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME);
	}

	private void deriveExcludeRules(final Node cnfNode) {
		// excludes constraints are bidirectional by default, therefore direction for
		// defined constraint is negligible, but we need two rules
		Node sourceLiteral = Prop4JUtils.getLeftNode(cnfNode);
		Node targetLiteral = Prop4JUtils.getRightNode(cnfNode);
		if (Prop4JUtils.isLiteral(sourceLiteral) && Prop4JUtils.isLiteral(targetLiteral)) {
			String sourceFeature = Prop4JUtils.getLiteralName((Literal) sourceLiteral);
			String targetFeature = Prop4JUtils.getLiteralName((Literal) targetLiteral);
			IDecision source = dm.find(sourceFeature);
			IDecision target = dm.find(targetFeature);
			if (isEnumSubFeature(source) && !isEnumSubFeature(target)) {
				// if either of the decisions is a enum sub feature the value for the
				// enumeration decision must be disallowed
				Rule rule = new Rule(source, new DeSelectDecisionAction(DecisionModelUtils.toBooleanDecision(target)));
				source.addRule(rule);
				updateRules(source, rule);
				EnumDecision enumDecision = findEnumDecision(source);
				ARangeValue value = enumDecision.getRangeValue(sourceFeature);
				rule = new Rule(target, new DisAllowAction(enumDecision, value));
				target.addRule(rule);
				updateRules(target, rule);
			} else if (!isEnumSubFeature(source) && isEnumSubFeature(target)) {
				EnumDecision enumDecision = findEnumDecision(target);
				ARangeValue value = enumDecision.getRangeValue(targetFeature);
				Rule rule = new Rule(source, new DisAllowAction(enumDecision, value));
				source.addRule(rule);
				updateRules(target, rule);
				rule = new Rule(target, new DeSelectDecisionAction(DecisionModelUtils.toBooleanDecision(source)));
				target.addRule(rule);
				updateRules(target, rule);
			} else if (isEnumSubFeature(source) && isEnumSubFeature(target)) {
				EnumDecision sourceEnumDecision = findEnumDecision(source);
				EnumDecision targetEnumDecision = findEnumDecision(target);
				ARangeValue sourceValue = sourceEnumDecision.getRangeValue(sourceFeature);
				ARangeValue targetValue = targetEnumDecision.getRangeValue(targetFeature);
				Rule rule = new Rule(source, new DisAllowAction(targetEnumDecision, targetValue));
				source.addRule(rule);
				updateRules(source, rule);
				rule = new Rule(target, new DisAllowAction(sourceEnumDecision, sourceValue));
				target.addRule(rule);
				updateRules(target, rule);
			} else if (DecisionModelUtils.isBooleanDecision(source) && DecisionModelUtils.isBooleanDecision(target)) {
				Rule rule = new Rule(source, new DeSelectDecisionAction(DecisionModelUtils.toBooleanDecision(target)));
				source.addRule(rule);
				updateRules(source, rule);
				rule = new Rule(target, new DeSelectDecisionAction(DecisionModelUtils.toBooleanDecision(source)));
				target.addRule(rule);
				updateRules(target, rule);
			}
		}
	}

	private EnumDecision findEnumDecision(final IDecision decision) {
		List<EnumDecision> decisions = new ArrayList<>(dm.findWithRangeValue(decision));
		if (!decisions.isEmpty()) {
			return decisions.remove(0);
		}
		decisions = dm.findWithVisibility(decision).stream().filter(DecisionModelUtils::isEnumDecision)
				.map(DecisionModelUtils::toEnumDecision).collect(Collectors.toList());
		if (!decisions.isEmpty()) {
			return decisions.remove(0);
		}
		throw new IllegalArgumentException("No Enumeration Decision available");
	}

	private List<IDecision> findDecisionsForLiterals(final Set<Node> literals) {
		List<IDecision> literalDecisions = new ArrayList<>();
		for (Node positive : literals) {
			String name = Prop4JUtils.getLiteralName((Literal) positive);
			IDecision d = dm.find(name);
			if (d != null) {
				literalDecisions.add(d);
			}
		}
		return literalDecisions;
	}

	private void updateRules(final IDecision decision, final Rule rule) {
		// Invert the created rule and add it
		invertRule(decision, rule);
		// if the rule contains a none option also disallow it and invert it again for
		// the enumeration
		if (DecisionModelUtils.isNoneAction(rule.getAction())) {
			EnumDecision enumDecision = (EnumDecision) rule.getAction().getVariable();
			ARangeValue rangeValue = (ARangeValue) rule.getAction().getValue();
			Rule r = new Rule(decision, new DisAllowAction(enumDecision, rangeValue));
			decision.addRule(r);
			// and again invert the added rule
			invertRule(decision, r);
		}
	}

	private void invertRule(final IDecision decision, final Rule rule) {
		if (rule.getAction() instanceof DisAllowAction) {
			DisAllowAction function = (DisAllowAction) rule.getAction();
			IAction allow = new AllowAction((IDecision) function.getVariable(), (ARangeValue) function.getValue());
			ICondition condition = invertCondition(rule.getCondition());
			Rule r = new Rule(condition, allow);
			decision.addRule(r);
		} else if (rule.getAction() instanceof SelectDecisionAction && DecisionModelUtils.isEnumDecision(decision)
				&& ((EnumDecision) decision).getCardinality().isAlternative()
				&& !DecisionModelUtils.isNoneCondition(rule.getCondition())) {
			SelectDecisionAction select = (SelectDecisionAction) rule.getAction();
			ICondition condition = invertCondition(rule.getCondition());
			IAction deselect = new DeSelectDecisionAction((BooleanDecision) select.getVariable());
			Rule r = new Rule(condition, deselect);
			decision.addRule(r);
		}
	}

	private ICondition invertCondition(final ICondition condition) {
		if (condition instanceof Not) {
			return ((Not) condition).getOperand();
		}
		return new Not(condition);
	}

	private boolean isEnumSubFeature(final IFeature feature) {
		// works as each tree in FeatureIDE has only one cardinality across all
		// sub-features.
		IFeature parent = FeatureUtils.getParent(feature);
		return parent != null && TraVarTUtils.isEnumerationType(parent);
	}

	private boolean isEnumSubFeature(final IDecision decision) {
		IFeature feature = fm.getFeature(DecisionModelUtils.retriveFeatureName(decision, dm.isAddPrefix(),
				DecisionModelUtils.isEnumDecision(decision)));
		return isEnumSubFeature(feature);
	}

	private boolean isEnumFeature(final IDecision decision) {
		IFeature feature = fm.getFeature(DecisionModelUtils.retriveFeatureName(decision, dm.isAddPrefix(),
				DecisionModelUtils.isEnumDecision(decision)));
		return TraVarTUtils.isEnumerationType(feature);
	}

	private void defineCardinality(final EnumDecision decision, final IFeature feature) {
		Cardinality cardinality = null;
		if (FeatureUtils.isOr(feature)) {
			cardinality = new Cardinality(1, FeatureUtils.getChildrenCount(feature));
		} else if (FeatureUtils.isAlternative(feature)) {
			cardinality = new Cardinality(1, 1);
		} else {
			cardinality = new Cardinality(0, FeatureUtils.getChildrenCount(feature));
		}
		decision.setCardinality(cardinality);
	}

	private void defineRange(final EnumDecision decision, final IFeature feature)
			throws NotSupportedVariablityTypeException {
		Range<String> range = new Range<>();
		for (IFeature optionFeature : FeatureUtils.getChildren(feature)) {
			StringValue option = new StringValue(optionFeature.getName());
			range.add(option);
		}
		decision.setRange(range);
		// add non option if it comes from a enum decision
		if (!FeatureUtils.isMandatorySet(feature) || isEnumSubFeature(feature)) {
			ARangeValue noneOption = decision.getNoneOption();
			noneOption.enable();
			decision.getRange().add(noneOption);
			try {
				decision.setValue(noneOption);
			} catch (RangeValueException e) {
				throw new NotSupportedVariablityTypeException(e);
			}
		}
	}

	private void convertVisibilityCustomProperties(final Collection<IFeature> features) {
		for (IFeature feature : features) {
			if (feature.getCustomProperties().has(DefaultDecisionModelTransformationProperties.PROPERTY_KEY_VISIBILITY,
					DefaultDecisionModelTransformationProperties.PROPERTY_KEY_VISIBILITY_TYPE)) {
				ConditionParser conditionParser = new ConditionParser(dm);
				String visbility = feature.getCustomProperties().get(
						DefaultDecisionModelTransformationProperties.PROPERTY_KEY_VISIBILITY,
						DefaultDecisionModelTransformationProperties.PROPERTY_KEY_VISIBILITY_TYPE);
				ICondition visiblity = conditionParser.parse(visbility);
				IDecision decision = dm.find(feature.getName());
				decision.setVisibility(visiblity);
			}
		}
	}
}
