package at.jku.cps.travart.dopler.decision.parser;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.NoActionInRuleException;
import at.jku.cps.travart.dopler.decision.exc.ParserException;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;

@SuppressWarnings("rawtypes")
public class RulesParser {

	private static final String NO_ACTION_IN_RULE = "A rule must have at least one action to perform!";

	private final IDecisionModel decisions;

	public RulesParser(final IDecisionModel decisions) {
		this.decisions = Objects.requireNonNull(decisions);
	}

	public static boolean isDoubleRangeValue(final String symbol) {
		try {
			Double.parseDouble(symbol);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isStringRangeValue(final IDecisionModel dm, final String symbol) {
		IDecision d = dm.find(symbol);
		return d == null;
	}

	public Set<Rule> parse(final IDecision decision, final String[] csvRuleSplit) throws ParserException {
		Objects.requireNonNull(decision);
		Objects.requireNonNull(csvRuleSplit);
		Set<Rule> rules = new HashSet<>();
		assert csvRuleSplit != null && csvRuleSplit.length > 0;
		for (String csvRule : csvRuleSplit) {
			// Split to condition and actions
			String[] ruleParts = TraVarTUtils.splitString(csvRule, "\\{");
			if (ruleParts.length < 2) {
				throw new NoActionInRuleException(NO_ACTION_IN_RULE);
			}
			// get condition from first part
			ConditionParser conditionParser = new ConditionParser(decisions);
			ICondition condition = conditionParser.parse(ruleParts[0]);
			// derive actions from the second part
			String[] csvActions = TraVarTUtils.splitString(ruleParts[1], ";|}");
			Set<IAction> actions = new HashSet<>();
			for (String csvAction : csvActions) {
				ActionParser actionParser = new ActionParser(decisions);
				IAction action = actionParser.parse(csvAction);
				actions.add(action);
			}
			// create a rule for each action and add them to a set of rules
			for (IAction action : actions) {
				Rule rule = new Rule(condition, action);
				rules.add(rule);
			}
		}
		return rules;
	}
}
