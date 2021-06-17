package at.jku.cps.travart.dopler.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.prop4j.Literal;
import org.prop4j.Node;

import at.jku.cps.travart.core.common.IModelTransformer;
import at.jku.cps.travart.core.common.Prop4JUtils;
import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.model.ABinaryCondition;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.AFunction;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.IValue;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.DecisionValueCondition;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Equals;
import at.jku.cps.travart.dopler.decision.model.impl.Greater;
import at.jku.cps.travart.dopler.decision.model.impl.GreaterEquals;
import at.jku.cps.travart.dopler.decision.model.impl.Less;
import at.jku.cps.travart.dopler.decision.model.impl.LessEquals;
import at.jku.cps.travart.dopler.decision.model.impl.Not;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.SelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;
import de.ovgu.featureide.fm.core.ExtensionManager.NoSuchExtensionException;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFactoryManager;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecisionModeltoFeatureModelConverter implements IModelTransformer<IDecisionModel, IFeatureModel> {

	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
	}

	private IFeatureModelFactory factory;
	private IFeatureModel fm;
	private IDecisionModel dm;

	@Override
	public IFeatureModel transform(final IDecisionModel dm) throws NotSupportedVariablityTypeException {
		this.dm = dm;
		try {
			factory = FMFactoryManager.getInstance().getFactory(DefaultFeatureModelFactory.ID);
			fm = factory.create();
			createFeatures();
			createFeatureTree();
			createConstraints();
			TraVarTUtils.deriveFeatureModelRoot(factory, fm);
			return fm;
		} catch (NoSuchExtensionException e) {
			throw new NotSupportedVariablityTypeException(e);
		}
	}

	private void createFeatures() {
		// first add all Boolean decisions
		for (BooleanDecision decision : DecisionModelUtils.getBooleanDecisions(dm)) {
			String featureName = retriveFeatureName(decision);
			IFeature feature = factory.createFeature(fm, featureName);
			// Features are optional by default
			FeatureUtils.addFeature(fm, feature);
		}
		// second add all Number decisions
		for (NumberDecision decision : DecisionModelUtils.getNumberDecisions(dm)) {
			String featureName = retriveFeatureName(decision);
			IFeature feature = factory.createFeature(fm, featureName);
			if (!decision.getRange().isEmpty()) {
				FeatureUtils.setAlternative(feature);
			}
			for (Object o : decision.getRange()) {
				IValue value = (IValue) o;
				String childName = featureName + "_" + value.getValue().toString();
				// first check if you can find a feature with the same name as the value
				// if so use it, otherwise create it
				IFeature child = fm.getFeature(childName);
				if (child != null) {
					FeatureUtils.addChild(feature, child);
				} else {
					child = factory.createFeature(fm, childName);
					FeatureUtils.addFeature(fm, child);
					FeatureUtils.addChild(feature, child);
				}
			}
			// Features are optional by default
			FeatureUtils.addFeature(fm, feature);
		}
		// third add all String decisions
		for (StringDecision decision : DecisionModelUtils.getStringDecisions(dm)) {
			String featureName = retriveFeatureName(decision);
			IFeature feature = factory.createFeature(fm, featureName);
			// Features are optional by default
			FeatureUtils.addFeature(fm, feature);
		}
		// finally build enumeration decisions from exiting features
		for (EnumDecision decision : DecisionModelUtils.getEnumDecisions(dm)) {
			String featureName = retriveFeatureName(decision);
			IFeature feature = fm.getFeature(featureName);
			if (feature == null) {
				feature = factory.createFeature(fm, featureName);
				FeatureUtils.addFeature(fm, feature);
			}
			Cardinality cardinality = decision.getCardinality();
			if (cardinality.isAlternative()) {
				FeatureUtils.setAlternative(feature);
			} else if (cardinality.isOr()) {
				FeatureUtils.setOr(feature);
			}
			for (Object o : decision.getRange()) {
				IValue value = (IValue) o;
				String childName = value.getValue().toString();
				// first check if you can find a feature with the same name as the value
				// if so use it, otherwise create it
				IFeature child = fm.getFeature(childName);
				if (child != null) {
					FeatureUtils.addChild(feature, child);
				}
				// If None value is read, don't add it, as it is special added for optional
				// groups
				else if (!DecisionModelUtils.isEnumNoneOption(decision, value)) {
					child = factory.createFeature(fm, childName);
					FeatureUtils.addFeature(fm, child);
					FeatureUtils.addChild(feature, child);
				}
			}
			// Features are optional by default
			FeatureUtils.addFeature(fm, feature);
		}
	}

	private void createFeatureTree() {
		for (IDecision decision : dm) {
			IFeature feature = fm.getFeature(retriveFeatureName(decision));
			if (FeatureUtils.getParent(feature) == null) {
				ICondition visiblity = decision.getVisiblity();
				if (DecisionModelUtils.isMandatoryVisibilityCondition(visiblity)) {
					FeatureUtils.setMandatory(feature, true);
					ADecision parentD = DecisionModelUtils.retriveMandatoryVisibilityCondition(visiblity);
					String parentFName = retriveFeatureName(parentD);
					IFeature parentF = fm.getFeature(parentFName);
					FeatureUtils.addChild(parentF, feature);
				} else if (visiblity instanceof ADecision) {
					ADecision parentD = (ADecision) visiblity;
					String parentFName = retriveFeatureName(parentD);
					IFeature parentF = fm.getFeature(parentFName);
					if (parentF != feature) {
						FeatureUtils.addChild(parentF, feature);
					}
				}
			}
		}
	}

	private String retriveFeatureName(final IDecision decision) {
		return DecisionModelUtils.retriveFeatureName(decision, dm.isAddPrefix(),
				DecisionModelUtils.isEnumDecision(decision));
	}

	private void createConstraints() {
		for (IDecision decision : dm) {
			// first store complex visibility condition in properties, to avoid losing the
			// information
			if (DecisionModelUtils.isComplexVisibilityCondition(decision.getVisiblity())
					&& !DecisionModelUtils.isMandatoryVisibilityCondition(decision.getVisiblity())) {
				IFeature feature = fm.getFeature(retriveFeatureName(decision));
				// add visibility as property for restoring them later if necessary
				feature.getCustomProperties().set(DefaultDecisionModelTransformationProperties.PROPERTY_KEY_VISIBILITY,
						DefaultDecisionModelTransformationProperties.PROPERTY_KEY_VISIBILITY_TYPE,
						decision.getVisiblity().toString());
			}
			// second transform constraints from the rules
			for (Object o : decision.getRules()) {
				Rule rule = (Rule) o;
				if (!DecisionModelUtils.isInItSelfRule(rule)) {
					if (DecisionModelUtils.isCompareCondition(rule.getCondition())) {
						deriveCompareConstraints(decision, rule.getCondition(), rule.getAction());
					} else {
						deriveConstraint(decision, rule.getCondition(), rule.getAction());
					}
				}
			}
		}
	}

	private void deriveCompareConstraints(final IDecision decision, final ICondition condition, final IAction action) {
		if (DecisionModelUtils.isNumberDecision(decision) && action instanceof DisAllowAction) {
			NumberDecision numberDecision = DecisionModelUtils.toNumberDecision(decision);
			ABinaryCondition binCondition = (ABinaryCondition) condition;
			ARangeValue<Double> conditionValue = (ARangeValue<Double>) binCondition.getRight();
			IFeature disAllowFeature = findValueFeature(((DisAllowAction) action).getValue().toString());
			Literal disAllowLiteral = Prop4JUtils.createLiteral(disAllowFeature);
			if (condition instanceof Equals) {
				createExcludesConstraint(numberDecision, disAllowLiteral, conditionValue);
			} else if (condition instanceof Greater) {
				Set<ARangeValue<Double>> values = numberDecision.getRange().stream()
						.filter(v -> v.getValue() > conditionValue.getValue()).collect(Collectors.toSet());
				for (ARangeValue<Double> value : values) {
					createExcludesConstraint(numberDecision, disAllowLiteral, value);
				}
			} else if (condition instanceof Less) {
				Set<ARangeValue<Double>> values = numberDecision.getRange().stream()
						.filter(v -> v.getValue() < conditionValue.getValue()).collect(Collectors.toSet());
				for (ARangeValue<Double> value : values) {
					createExcludesConstraint(numberDecision, disAllowLiteral, value);
				}
			} else if (condition instanceof GreaterEquals) {
				Set<ARangeValue<Double>> values = numberDecision.getRange().stream()
						.filter(v -> v.getValue() >= conditionValue.getValue()).collect(Collectors.toSet());
				for (ARangeValue<Double> value : values) {
					createExcludesConstraint(numberDecision, disAllowLiteral, value);
				}
			} else if (condition instanceof LessEquals) {
				Set<ARangeValue<Double>> values = numberDecision.getRange().stream()
						.filter(v -> v.getValue() <= conditionValue.getValue()).collect(Collectors.toSet());
				for (ARangeValue<Double> value : values) {
					createExcludesConstraint(numberDecision, disAllowLiteral, value);
				}
			}
		}
	}

	private void createExcludesConstraint(final NumberDecision numberDecision, final Literal disAllowLiteral,
			final ARangeValue<Double> value) {
		IFeature valueFeature = findValueFeature(numberDecision.getId() + "_" + value.getValue().toString());
		Literal valueLiteral = Prop4JUtils.createLiteral(valueFeature);
		IConstraint constraint = factory.createConstraint(fm,
				Prop4JUtils.createImplies(valueLiteral, Prop4JUtils.createNot(disAllowLiteral)));
		addConstraintIfEligible(constraint);
	}

	private void deriveConstraint(final IDecision decision, final ICondition condition, final IAction action) {
		Node conditionNode = deriveConditionNode(decision, condition);
		// case: if decision is selected another one has to be selected as well: implies
		if (condition instanceof ADecision && action.getVariable() instanceof ADecision
				&& action.getValue().equals(BooleanValue.getTrue())) {
			ADecision conditionDecision = (ADecision) condition;
			String conditionFeatureName = retriveFeatureName(conditionDecision);
			IFeature conditionFeature = fm.getFeature(conditionFeatureName);
			ADecision variableDecision = (ADecision) action.getVariable();
			String variableFeatureName = retriveFeatureName(variableDecision);
			IFeature variableFeature = fm.getFeature(variableFeatureName);
			if (FeatureUtils.getParent(variableFeature) != conditionFeature
					|| !FeatureUtils.isMandatorySet(variableFeature)) {
				IConstraint constraint = factory.createConstraint(fm,
						Prop4JUtils.createImplies(conditionNode, Prop4JUtils.createLiteral(variableFeature)));
				addConstraintIfEligible(constraint);
			}
		}
		// case: excludes constraint
		else if (condition instanceof ADecision && action.getVariable() instanceof ADecision
				&& action.getValue().equals(BooleanValue.getFalse())) {
			ADecision variableDecision = (ADecision) action.getVariable();
			String featureName = retriveFeatureName(variableDecision);
			IFeature feature = fm.getFeature(featureName);
			IConstraint constraint = factory.createConstraint(fm, Prop4JUtils.createImplies(conditionNode,
					Prop4JUtils.createNot(Prop4JUtils.createLiteral(feature))));
			addConstraintIfEligible(constraint);
		}
		// case: if the action allows or disallows/sets a decision to a None value
		else if (condition instanceof ADecision && action instanceof SetValueAction
				&& DecisionModelUtils.isNoneAction(action)) {
			ADecision variableDecision = (ADecision) action.getVariable();
			String featureName = retriveFeatureName(variableDecision);
			// set cardinality of variable feature
			IFeature varFeature = fm.getFeature(featureName);
			IConstraint constraint = factory.createConstraint(fm, Prop4JUtils.createImplies(conditionNode,
					Prop4JUtils.createNot(Prop4JUtils.createLiteral(varFeature))));
			addConstraintIfEligible(constraint);
		}
		// case: condition is negated: if both bool then it is an or constraint,
		// otherwise implies
		else if (condition instanceof Not && DecisionModelUtils.isNoneCondition(condition)
				&& action instanceof SelectDecisionAction) {
			String conditionFeatureName = retriveFeatureName(decision);
			ADecision variableDecision = (ADecision) action.getVariable();
			String variableDecisionFeatureName = retriveFeatureName(variableDecision);
			IFeature conditionFeature = fm.getFeature(conditionFeatureName);
			IFeature variableFeature = fm.getFeature(variableDecisionFeatureName);
			IConstraint constraint = factory.createConstraint(fm, Prop4JUtils.createImplies(
					Prop4JUtils.createLiteral(conditionFeature), Prop4JUtils.createLiteral(variableFeature)));
			addConstraintIfEligible(constraint);
		}
		// case: if the condition is a enum value and sets the value of a different
		// decision (Number/String decision)
		else if (!(condition instanceof Not) && action.getVariable() instanceof ADecision
				&& action.getValue() instanceof ARangeValue && !action.getValue().equals(BooleanValue.getTrue())
				&& !action.getValue().equals(BooleanValue.getFalse())) {
			// create a new child feature for the feature the rule writes - if it is not a
			// none value
			ADecision variableDecision = (ADecision) action.getVariable();
			// feature name
			String variableDecisionFeatureName = retriveFeatureName(variableDecision);
			// set cardinality of variable feature
			IFeature variableFeature = fm.getFeature(variableDecisionFeatureName);
			if (DecisionModelUtils.isNoneAction(action)) {
				IConstraint constraint = factory.createConstraint(fm,
						Prop4JUtils.createImplies(conditionNode, Prop4JUtils.createLiteral(variableFeature)));
				addConstraintIfEligible(constraint);
			} else {
				IFeature valueFeature = findValueFeature(action.getValue().toString());
				// if it was not found, then create it
				if (valueFeature == null) {// && !isNoneAction(action)) {
					valueFeature = factory.createFeature(fm, variableDecisionFeatureName + "_" + action.getValue());
					// add value feature as part of the feature model and as child to the variable
					// feature if it was created
					FeatureUtils.addFeature(fm, valueFeature);
					FeatureUtils.addChild(variableFeature, valueFeature);
				}
				if (variableDecision instanceof EnumDecision) {
					Cardinality cardinality = ((EnumDecision) variableDecision).getCardinality();
					if (cardinality.isAlternative()) {
						FeatureUtils.setAlternative(variableFeature);
					} else if (cardinality.isOr()) {
						FeatureUtils.setOr(variableFeature);
					}
				}
				// create and add constraint for setting the condition to set, if is no value
				// condition
				IConstraint constraint = null;
				if (action instanceof DisAllowAction) {
					constraint = factory.createConstraint(fm, Prop4JUtils.createImplies(conditionNode,
							Prop4JUtils.createNot(Prop4JUtils.createLiteral(valueFeature))));
				} else {
					constraint = factory.createConstraint(fm,
							Prop4JUtils.createImplies(conditionNode, Prop4JUtils.createLiteral(valueFeature)));
				}
				addConstraintIfEligible(constraint);
			}
		} else if (DecisionModelUtils.isComplexCondition(condition)
				&& (action instanceof SelectDecisionAction || action instanceof DeSelectDecisionAction)) {
			Set<IDecision> conditionDecisions = DecisionModelUtils.retriveConditionDecisions(condition);
			List<Literal> conditionLiterals = new ArrayList<>(conditionDecisions.size());
			for (IDecision conditionDecision : conditionDecisions) {
				String conditionFeatureName = retriveFeatureName(conditionDecision);
				IFeature conditionFeature = fm.getFeature(conditionFeatureName);
				conditionLiterals.add(Prop4JUtils.createLiteral(conditionFeature));
			}
			if (Prop4JUtils.isAnd(conditionNode)) {
				if (Prop4JUtils.hasNegativeLiteral(conditionNode)) {
					conditionNode = Prop4JUtils.consumeToOrGroup(conditionLiterals, true);
				} else {
					conditionNode = Prop4JUtils.consumeToAndGroup(conditionLiterals, false);
				}
			} else {
				if (Prop4JUtils.hasNegativeLiteral(conditionNode)) {
					conditionNode = Prop4JUtils.consumeToAndGroup(conditionLiterals, true);
				} else {
					conditionNode = Prop4JUtils.consumeToOrGroup(conditionLiterals, false);
				}
			}
			ADecision variableDecision = (ADecision) action.getVariable();
			String variableDecisionFeatureName = retriveFeatureName(variableDecision);
			IFeature variableFeature = fm.getFeature(variableDecisionFeatureName);
			Literal variableLiteral = Prop4JUtils.createLiteral(variableFeature);
			IConstraint constraint;
			if (DecisionModelUtils.isNotCondition(condition)) {
				constraint = factory.createConstraint(fm, Prop4JUtils.createOr(conditionNode, variableLiteral));
			} else if (action instanceof SelectDecisionAction) {
				constraint = factory.createConstraint(fm, Prop4JUtils.createImplies(conditionNode, variableLiteral));
			} else {
				variableLiteral.positive = false;
				constraint = factory.createConstraint(fm, Prop4JUtils.createOr(conditionNode, variableLiteral));
			}
			addConstraintIfEligible(constraint);
		}
	}

	private void addConstraintIfEligible(final IConstraint constraint) {
		if (eligibleForFeatureModel(constraint)) {
			FeatureUtils.addConstraint(fm, constraint);
		}
	}

//	private void createRoot() {
//		List<IFeature> roots = findRoots();
//		if (roots.size() != 1) {
//			// artificial root - abstract and hidden feature
//			IFeature rootFeature = factory.createFeature(fm, DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME);
//			FeatureUtils.addFeature(fm, rootFeature);
//			FeatureUtils.setRoot(fm, rootFeature);
//			FeatureUtils.setOr(rootFeature);
//			FeatureUtils.setAbstract(rootFeature, true);
//			FeatureUtils.setHiddden(rootFeature, true);
//			for (IFeature feature : roots) {
//				FeatureUtils.addChild(rootFeature, feature);
//			}
//		} else {
//			// make this only root to the root of the model
//			IFeature root = roots.get(0);
//			FeatureUtils.setRoot(fm, root);
//		}
//	}
//
//	private List<IFeature> findRoots() {
//		List<IFeature> roots = new ArrayList<>();
//		for (IFeature feature : FeatureUtils.getFeatures(fm)) {
//			if (FeatureUtils.getParent(feature) == null) {
//				roots.add(feature);
//			}
//		}
//		return roots;
//	}

	private IFeature findValueFeature(final String valueName) {
		IFeature valueFeature = fm.getFeature(valueName);
		// if not found before check if another feature exists, or create it
		if (valueFeature == null) {
			String valueDecisionName = DecisionModelUtils.retriveFeatureName(valueName, true, false);
			valueFeature = fm.getFeature(valueDecisionName);
		}
		return valueFeature;
	}

	private boolean eligibleForFeatureModel(final IConstraint constraint) {
		for (IConstraint constr : FeatureUtils.getConstraints(fm)) {
			if (constr.getNode().equals(constraint.getNode())) {
				return false;
			}
		}
		return true;
	}

	private Node deriveConditionNode(final IDecision decision, final ICondition condition) {
		if (DecisionModelUtils.isBinaryCondition(condition)) {
			ABinaryCondition binCondition = (ABinaryCondition) condition;
			return deriveConditionNodeRecursive(binCondition.getLeft(), binCondition.getRight(), condition);
		} else if (condition instanceof DecisionValueCondition) {
			ARangeValue value = ((DecisionValueCondition) condition).getValue();
			IFeature feature = findValueFeature(value.toString());
			return Prop4JUtils.createLiteral(feature);
		} else if (DecisionModelUtils.isNot(condition)) {
			Not notNode = (Not) condition;
			ICondition operand = notNode.getOperand();
			Literal literal;
			if (DecisionModelUtils.isDecision(operand)) {
				ADecision d = (ADecision) operand;
				String featureName = retriveFeatureName(d);
				IFeature feature = fm.getFeature(featureName);
				literal = Prop4JUtils.createLiteral(feature);
			} else {
				literal = Prop4JUtils.createLiteral(operand);
			}
			literal.positive = false;
			return literal;
		} else if (DecisionModelUtils.isDecision(condition)) {
			ADecision d = (ADecision) condition;
			String featureName = retriveFeatureName(d);
			IFeature feature = fm.getFeature(featureName);
			return Prop4JUtils.createLiteral(feature);

		} else if (condition instanceof StringValue) {
			return Prop4JUtils.createLiteral(((StringValue) condition).getValue());
		}
		return Prop4JUtils.createLiteral(condition);
	}

	private Node deriveConditionNodeRecursive(final ICondition left, final ICondition right,
			final ICondition condition) {
		Node cLeft = deriveNode(left);
		Node cRight = deriveNode(right);
		if (cLeft == null && cRight != null) {
			return cRight;
		} else if (cLeft != null && cRight == null) {
			return cLeft;
		} else if (condition instanceof And) {
			return Prop4JUtils.createAnd(cLeft, cRight);
		} else {
			return Prop4JUtils.createAnd(Prop4JUtils.createNot(cLeft), Prop4JUtils.createNot(cRight));
		}
	}

	private Node deriveNode(final ICondition node) {
		if (DecisionModelUtils.isBinaryCondition(node)) {
			ABinaryCondition binVis = (ABinaryCondition) node;
			return deriveConditionNodeRecursive(binVis.getLeft(), binVis.getRight(), binVis);
		} else if (node instanceof Not) {
			Not notNode = (Not) node;
			Literal literal = Prop4JUtils.createLiteral(notNode.getOperand());
			literal.positive = false;
			return literal;
		} else if (node instanceof AFunction) {
			return null;
		}
		return Prop4JUtils.createLiteral(node);
	}
}
