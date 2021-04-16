package at.jku.cps.travart.dopler.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;
import at.jku.cps.travart.dopler.decision.exc.ConditionCreationException;
import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.ABinaryCondition;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.AUnaryCondition;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.IEnumerationDecision;
import at.jku.cps.travart.dopler.decision.model.IRangeValue;
import at.jku.cps.travart.dopler.decision.model.IValue;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.DecisionValueCondition;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Equals;
import at.jku.cps.travart.dopler.decision.model.impl.Greater;
import at.jku.cps.travart.dopler.decision.model.impl.GreaterEquals;
import at.jku.cps.travart.dopler.decision.model.impl.Less;
import at.jku.cps.travart.dopler.decision.model.impl.LessEquals;
import at.jku.cps.travart.dopler.decision.model.impl.Not;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;

public final class DecisionModelUtils {

	@SuppressWarnings("rawtypes")
	public static ICondition consumeToBinaryCondition(final List<IDecision> decisions,
			final Class<? extends ABinaryCondition> clazz, final boolean negative)
			throws CircleInConditionException, ConditionCreationException {
		if (decisions.isEmpty()) {
			throw new ConditionCreationException(new IllegalArgumentException("Set of decisions is empty!"));
		}
		if (decisions.size() == 1) {
			if(decisions.get(0)==null) {
				throw new ConditionCreationException(new IllegalArgumentException("Only existing decision is null!"));
			}
			return negative ? new Not(decisions.get(0)) : decisions.get(0);
		}
		try {
			// get the constructor
			Constructor<? extends ABinaryCondition> constructor = clazz.getConstructor(ICondition.class,
					ICondition.class);
			// take the first two and create the first ABinaryCondition
			ICondition first = decisions.remove(0);
			ICondition second = decisions.remove(0);
			if (negative) {
				first = new Not(first);
				second = new Not(second);
			}
			ABinaryCondition condition = constructor.newInstance(first, second);
			while (!decisions.isEmpty()) {
				ICondition next = decisions.remove(0);
				if (negative) {
					next = new Not(next);
				}
				condition = constructor.newInstance(next, condition);
			}
			return condition;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ConditionCreationException(e);
		}
	}

	public static boolean detectCircle(final ICondition condition, final ICondition toAdd) {
		if (toAdd == ICondition.TRUE || toAdd == ICondition.FALSE) {
			return false;
		}
		if (condition == ICondition.TRUE || condition == ICondition.FALSE) {
			return false;
		}
		if (condition == toAdd) {
			return true;
		}
		boolean ret = false;
		if (condition instanceof AUnaryCondition) {
			ret = ret || detectCircle(((AUnaryCondition) condition).getOperand(), toAdd);
		}
		if (condition instanceof ABinaryCondition) {
			ret = ret || detectCircle(((ABinaryCondition) condition).getLeft(), toAdd);
			ret = ret || detectCircle(((ABinaryCondition) condition).getRight(), toAdd);
		}
		return ret;
	}

	public static Set<BooleanDecision> getBooleanDecisions(final IDecisionModel dm) {
		return dm.stream().filter(DecisionModelUtils::isBooleanDecision).map(DecisionModelUtils::toBooleanDecision)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<String> getBooleanDecisionsAsNames(final IDecisionModel dm) {
		return getBooleanDecisions(dm).stream().map(BooleanDecision::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<String> getDecisionNames(final IDecisionModel dm) {
		return dm.stream().map(IDecision::getId).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<EnumDecision> getEnumDecisions(final IDecisionModel dm) {
		return dm.stream().filter(DecisionModelUtils::isEnumDecision).map(DecisionModelUtils::toEnumDecision)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<String> getEnumDecisionsAsNames(final IDecisionModel dm) {
		return getEnumDecisions(dm).stream().map(EnumDecision::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<NumberDecision> getNumberDecisions(final IDecisionModel dm) {
		return dm.stream().filter(DecisionModelUtils::isNumberDecision).map(DecisionModelUtils::toNumberDecision)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<String> getNumberDecisionsAsNames(final IDecisionModel dm) {
		return getNumberDecisions(dm).stream().map(NumberDecision::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@SuppressWarnings("rawtypes")
	public static Set<IDecision> getReachableDecisions(final IDecisionModel dm) {
		return dm.stream().filter(d -> d.isVisible() && !d.isSelected())
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<String> getReachableDecisionsAsNames(final IDecisionModel dm) {
		return dm.stream().filter(d -> d.isVisible() && !d.isSelected()).map(IDecision::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@SuppressWarnings("rawtypes")
	public static Set<IDecision> getSelectableDecisions(final IDecisionModel dm) {
		return dm.stream().filter(d -> !(d.getVisiblity() == ICondition.FALSE))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	 * 
	 * @param dm
	 * @return returns a list of visible decisions as names
	 */
	public static Set<String> getSelectableDecisionsAsNames(final IDecisionModel dm) {
		return getSelectableDecisions(dm).stream().map(IDecision::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@SuppressWarnings("rawtypes")
	public static List<IDecision> getSelectedDecisions(final IDecisionModel dm) {
		return dm.stream().filter(IDecision::isSelected).collect(Collectors.toCollection(ArrayList::new));
	}

	public static List<String> getSelectedDecisionsAsNames(final IDecisionModel dm) {
		return getSelectedDecisions(dm).stream().map(IDecision::getId).collect(Collectors.toCollection(ArrayList::new));
	}

	public static Set<StringDecision> getStringDecisions(final IDecisionModel dm) {
		return dm.stream().filter(DecisionModelUtils::isStringDecision).map(DecisionModelUtils::toStringDecision)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static Set<String> getStringDecisionsAsNames(final IDecisionModel dm) {
		return getStringDecisions(dm).stream().map(StringDecision::getId)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static boolean hasReachableDecisions(final IDecisionModel dm) {
		return !getReachableDecisions(dm).isEmpty();
	}

	public static boolean isBinaryCondition(final ICondition visiblity) {
		return visiblity instanceof ABinaryCondition;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isBooleanDecision(final IDecision decision) {
		return decision instanceof BooleanDecision;
	}

	public static boolean isComplexCondition(final ICondition condition) {
		return !isDecisionValueCondition(condition)
				&& (isNotCondition(condition) || condition instanceof ABinaryCondition);
	}

	public static boolean isComplexVisibilityCondition(final ICondition visibility) {
		return !isDecision(visibility) && !(visibility instanceof IRangeValue) && visibility != ICondition.TRUE && visibility != ICondition.FALSE;
	}

	public static boolean isDecision(final ICondition decision) {
		return decision instanceof IDecision;
	}

	public static boolean isDecisionValueCondition(ICondition condition) {
		if (condition instanceof Not) {
			condition = ((Not) condition).getOperand();
		}
		return condition instanceof DecisionValueCondition;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEnumDecision(final IDecision decision) {
		return decision instanceof EnumDecision;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isEnumNoneOption(final IDecision decision, final IValue value) {
		return DecisionModelUtils.isEnumDecision(decision) && value instanceof ARangeValue
				&& ((EnumDecision) decision).isNoneOption((ARangeValue) value);
	}

	/*
	 * Identify rules which contain actions to itself. e.g., if (d_Storage) {
	 * disAllow(d_Storage_0.None); }
	 */
	public static boolean isInItSelfRule(final Rule rule) {
		ICondition condition = rule.getCondition();
		if (condition instanceof Not) {
			condition = ((Not) condition).getOperand();
		}
		if (!isDecision(condition)) {
			return false;
		}
		ICondition actionVariable = rule.getAction().getVariable();
		if (actionVariable instanceof Not) {
			actionVariable = ((Not) actionVariable).getOperand();
		}
		if (!isDecision(actionVariable)) {
			return false;
		}
		return condition == actionVariable || condition == toDecision(actionVariable).getVisiblity();
	}

	public static boolean isMandatoryVisibilityCondition(final ICondition visiblity) {
		if (!(visiblity instanceof And)) {
			return false;
		}
		And and = (And) visiblity;
		return and.getLeft() == ICondition.FALSE && and.getRight() instanceof IDecision
				|| and.getLeft() instanceof IDecision && and.getRight() == ICondition.FALSE;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isNoneAction(final IAction action) {
		return action.getVariable() instanceof IEnumerationDecision && action.getValue() instanceof ARangeValue
				&& ((IEnumerationDecision) action.getVariable()).isNoneOption((ARangeValue) action.getValue());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isNoneCondition(ICondition condition) {
		if (!isDecisionValueCondition(condition)) {
			return false;
		}
		if (condition instanceof Not) {
			condition = ((Not) condition).getOperand();
		}
		IDecision decision = ((DecisionValueCondition) condition).getDecision();
		ARangeValue value = ((DecisionValueCondition) condition).getValue();
		return decision instanceof IEnumerationDecision && ((IEnumerationDecision) decision).isNoneOption(value);
	}

	public static boolean isNot(final ICondition condition) {
		return condition instanceof Not;
	}

	public static boolean isNotCondition(final ICondition condition) {
		return condition instanceof Not;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNumberDecision(final IDecision decision) {
		return decision instanceof NumberDecision;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isStringDecision(final IDecision decision) {
		return decision instanceof StringDecision;
	}

	public static <T> Set<Set<T>> powerSet(final Set<T> originalSet) {
		Set<Set<T>> sets = new HashSet<>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		List<T> list = new ArrayList<>(originalSet);
		T head = list.get(0);
		Set<T> rest = new HashSet<>(list.subList(1, list.size()));
		for (Set<T> set : powerSet(rest)) {
			Set<T> newSet = new HashSet<>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	public static <T> Set<Set<T>> powerSetWithMinAndMax(final Set<T> originalSet, final int min, final int max) {
		return powerSet(originalSet).stream().filter(set -> set.size() >= min && set.size() <= max)
				.collect(Collectors.toSet());
	}

	@SuppressWarnings("rawtypes")
	public static Set<IDecision> retriveConditionDecisions(final ICondition condition) {
		Set<IDecision> decisions = new HashSet<>();
		retriveConditionDecisionsRec(decisions, condition);
		return decisions;
	}

	@SuppressWarnings("rawtypes")
	private static void retriveConditionDecisionsRec(final Set<IDecision> decisions, final ICondition condition) {
		if (condition instanceof AUnaryCondition) {
			retriveConditionDecisionsRec(decisions, ((AUnaryCondition) condition).getOperand());
		} else if (condition instanceof ABinaryCondition) {
			retriveConditionDecisionsRec(decisions, ((ABinaryCondition) condition).getLeft());
			retriveConditionDecisionsRec(decisions, ((ABinaryCondition) condition).getRight());
		} else if (condition instanceof DecisionValueCondition) {
			retriveConditionDecisionsRec(decisions, ((DecisionValueCondition) condition).getDecision());
		} else if (condition instanceof IDecision) {
			decisions.add((IDecision) condition);
		}
	}

	@SuppressWarnings("rawtypes")
	public static String retriveFeatureName(final IDecision decision, final boolean addPrefix,
			final boolean addEnumExtension) {
		int fromIndex = addPrefix ? DecisionModel.DEFAULT_DECISION_ID_PREFIX.length() : 0;
		int toIndex = addEnumExtension ? decision.getId().lastIndexOf('_', decision.getId().length() - 1)
				: decision.getId().length();
		// TODO: fix this magic number for more enumerations then just one
		if (toIndex < 0 || toIndex < fromIndex || decision.getId().length() - 2 != toIndex) {
			toIndex = decision.getId().length();
		}
		return decision.getId().substring(fromIndex, toIndex);
	}

	public static String retriveFeatureName(String decisionName, final boolean addPrefix,
			final boolean addEnumExtension) {
		decisionName = addPrefix ? DecisionModel.DEFAULT_DECISION_ID_PREFIX + decisionName : decisionName;
		decisionName = addEnumExtension ? decisionName + "_0" : decisionName;
		return decisionName;
	}

	@SuppressWarnings("rawtypes")
	public static BooleanDecision toBooleanDecision(final IDecision decision) {
		return (BooleanDecision) decision;
	}

	@SuppressWarnings("rawtypes")
	public static IDecision toDecision(final ICondition decision) {
		return (IDecision) decision;
	}

	@SuppressWarnings("rawtypes")
	public static EnumDecision toEnumDecision(final IDecision decision) {
		return (EnumDecision) decision;
	}

	@SuppressWarnings("rawtypes")
	public static NumberDecision toNumberDecision(final IDecision decision) {
		return (NumberDecision) decision;
	}

	@SuppressWarnings("rawtypes")
	public static StringDecision toStringDecision(final IDecision decision) {
		return (StringDecision) decision;
	}

	private DecisionModelUtils() {
	}

	public static boolean isCompareCondition(final ICondition condition) {
		return condition instanceof Equals || condition instanceof Greater || condition instanceof Less
				|| condition instanceof GreaterEquals || condition instanceof LessEquals;
	}

	@SuppressWarnings("rawtypes")
	public static ADecision retriveMandatoryVisibilityCondition(final ICondition visiblity) {
		if (!isMandatoryVisibilityCondition(visiblity)) {
			throw new IllegalArgumentException("Given visibility " + visiblity
					+ " is no mandatory visibility condition. Check with #isMandatoryVisibilityCondition first.");
		}
		And and = (And) visiblity;
		return (ADecision) (and.getLeft() == ICondition.FALSE ? and.getRight() : and.getLeft());
	}
}
