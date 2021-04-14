package at.jku.cps.travart.dopler.common;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;

public final class DecisionModelStatisticsUtils {

	private DecisionModelStatisticsUtils() {
	}

	public static int getNumberOfDecisions(final IDecisionModel dm) {
		return dm.size();
	}

	@SuppressWarnings("rawtypes")
	public static int getConditonsCount(final IDecisionModel dm) {
		Set<ICondition> conditions = new HashSet<>();
		for (IDecision decision : dm) {
			for (Object o : decision.getRules()) {
				Rule rule = (Rule) o;
				conditions.add(rule.getCondition());
			}
		}
		return conditions.size();
	}

	@SuppressWarnings("rawtypes")
	public static int getActionCount(final IDecisionModel dm) {
		Set<IAction> actions = new HashSet<>();
		for (IDecision decision : dm) {
			for (Object o : decision.getRules()) {
				Rule rule = (Rule) o;
				actions.add(rule.getAction());
			}
		}
		return actions.size();
	}

	public static Set<ICondition> getSimpleVisibilityConditions(final IDecisionModel dm) {
		return dm.stream().filter(d -> !DecisionModelUtils.isComplexVisibilityCondition(d.getVisiblity()))
				.map(IDecision::getVisiblity).collect(Collectors.toSet());
	}

	public static long getSimpleVisibilityConditionsCount(final IDecisionModel dm) {
		return dm.stream().filter(d -> !DecisionModelUtils.isComplexVisibilityCondition(d.getVisiblity())).count();
	}

	public static Set<ICondition> getComplexVisibilityConditions(final IDecisionModel dm) {
		return dm.stream().filter(d -> DecisionModelUtils.isComplexVisibilityCondition(d.getVisiblity()))
				.map(IDecision::getVisiblity).collect(Collectors.toSet());
	}

	public static long getComplexVisibilityConditionsCount(final IDecisionModel dm) {
		return dm.stream().filter(d -> DecisionModelUtils.isComplexVisibilityCondition(d.getVisiblity())).count();
	}

	public static void printModelStatistics(final IDecisionModel dm) {
		System.out.println("Decision Model Statistics: ");
		System.out.println("Name: " + dm.getName());
		System.out.println("#Decisons: " + DecisionModelStatisticsUtils.getNumberOfDecisions(dm));
		System.out.println("#Rule Conditions: " + DecisionModelStatisticsUtils.getConditonsCount(dm));
		System.out.println("#Actions: " + DecisionModelStatisticsUtils.getActionCount(dm));
		System.out.println(
				"#Visibility Conditions: " + DecisionModelStatisticsUtils.getComplexVisibilityConditionsCount(dm));
	}
}
