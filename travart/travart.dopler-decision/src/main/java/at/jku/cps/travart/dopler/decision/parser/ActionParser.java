package at.jku.cps.travart.dopler.decision.parser;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.InvalidActionException;
import at.jku.cps.travart.dopler.decision.exc.ParserException;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.AllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.SelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class ActionParser {

	private static final int NECESSARY_ELEMENTS_FOR_ACTION = 2;

	private static final String REGEX = "(?<=\\.)|(?=\\.)|(?<=\\=)|(?=\\=)|((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))";

	private static final String EOF = "EOF";

	private static final String ASSIGN = "=";
	private static final String DECISION_VALUE_DELIMITER = ".";
	private static final String OPEN_PARENTHESE = "(";
	private static final String CLOSING_PARENTHESE = ")";

	private static final String TRUE = "true";
	private static final String FALSE = "false";

	private String[] input;
	private int index = 0;
	private String symbol;

	private final IDecisionModel dm;
	private final Queue<ICondition> actionElements = new LinkedList<>();

	public ActionParser(final IDecisionModel decisions) {
		dm = Objects.requireNonNull(decisions);
	}

	public IAction parse(final String str) throws ParserException {
		Objects.requireNonNull(str);
		index = 0;
		input = TraVarTUtils.splitString(str, REGEX);
		if (input.length > 0) {
			return parseAction();
		}
		throw new ParserException("No action found in String " + str);
	}

	@SuppressWarnings("rawtypes")
	private IAction parseAction() {
		IAction action = null;
		boolean isAssign = false;
		boolean isAllowFunction = false;
		boolean isDisAllowFunction = false;
		nextSymbol();
		while (!symbol.equals(EOF)) {
			if (symbol.equals(OPEN_PARENTHESE) || symbol.equals(CLOSING_PARENTHESE)
					|| symbol.equals(DECISION_VALUE_DELIMITER)) {
				nextSymbol();
				continue;
			}
			if (symbol.equals(TRUE)) {
				actionElements.add(ICondition.TRUE);
			} else if (symbol.equals(FALSE)) {
				actionElements.add(ICondition.FALSE);
			} else if (symbol.equals(ASSIGN)) {
				isAssign = true;
			} else if (symbol.equals(AllowAction.FUNCTION_NAME)) {
				isAllowFunction = true;
			} else if (symbol.equals(DisAllowAction.FUNCTION_NAME)) {
				isDisAllowFunction = true;
			} else if (symbol.equals("enforce")) {
				isAssign = true;
			} else if (RulesParser.isDoubleRangeValue(symbol)) {
				actionElements.add(new DoubleValue(Double.parseDouble(symbol)));
			} else if (RulesParser.isStringRangeValue(dm, symbol)) {
				actionElements.add(new StringValue(symbol));
			} else { // decision
				IDecision d = dm.find(symbol);
				actionElements.add(d);
			}
			if (actionElements.size() == NECESSARY_ELEMENTS_FOR_ACTION) {
				if (isAssign) {
					ICondition left = actionElements.remove();
					ICondition right = actionElements.remove();
					if (!(left instanceof IDecision)) {
						throw new InvalidActionException("Lefthand operand is not a valid decision.");
					}
					if (right == ICondition.TRUE) {
						action = new SelectDecisionAction((BooleanDecision) left);
					} else if (right == ICondition.FALSE) {
						action = new DeSelectDecisionAction((BooleanDecision) left);
					} else if (right instanceof ARangeValue) {
						action = new SetValueAction((IDecision) left, (ARangeValue) right);
					}
				} else if (isAllowFunction) {
					ICondition left = actionElements.remove();
					ICondition right = actionElements.remove();
					if (left instanceof IDecision && right instanceof ARangeValue) {
						action = new AllowAction((IDecision) left, (ARangeValue) right);
					}
				} else if (isDisAllowFunction) {
					ICondition left = actionElements.remove();
					ICondition right = actionElements.remove();
					if (left instanceof IDecision && right instanceof ARangeValue) {
						action = new DisAllowAction((IDecision) left, (ARangeValue) right);
					}
				}
			}
			nextSymbol();
		}
		return action;
	}

	private void nextSymbol() {
		if (index == input.length) {
			symbol = EOF;
		} else {
			symbol = input[index++];
		}
	}
}