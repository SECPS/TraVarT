package at.jku.cps.travart.dopler.decision.parser;

import java.util.Objects;

import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.ParserException;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.DecisionValueCondition;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.Equals;
import at.jku.cps.travart.dopler.decision.model.impl.GetValueFunction;
import at.jku.cps.travart.dopler.decision.model.impl.Greater;
import at.jku.cps.travart.dopler.decision.model.impl.GreaterEquals;
import at.jku.cps.travart.dopler.decision.model.impl.IsTakenFunction;
import at.jku.cps.travart.dopler.decision.model.impl.Less;
import at.jku.cps.travart.dopler.decision.model.impl.LessEquals;
import at.jku.cps.travart.dopler.decision.model.impl.Not;
import at.jku.cps.travart.dopler.decision.model.impl.Or;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

@SuppressWarnings("rawtypes")
public class ConditionParser {

	private static final String REGEX = "(?<=\\.)|(?=\\.)|((?<=\\=)|(?=\\=)|(?<=\\<)|(?=\\<)|(?<=\\>)|(?=\\>))|((?<=\\|\\|)|(?=\\|\\|))|((?<=&&)|(?=&&))|((?<=!)|(?=!))|((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))";

	private static final String EOF = "EOF";

	private static final String NOT = "!";
	private static final String OR = "||";
	private static final String AND = "&&";
	private static final String GREATER = ">";
	private static final String LESS = "<";
	private static final String EQUAL = "=";
	private static final String OPENING_PARENTHESE = "(";
	private static final String DECISION_VALUE_DELIMITER = ".";

	private static final String TRUE = "true";
	private static final String FALSE = "false";

	private String[] input;
	private int index = 0;
	private String symbol;

	private final IDecisionModel dm;

	public ConditionParser(final IDecisionModel dm) {
		this.dm = Objects.requireNonNull(dm);
	}

	public ICondition parse(final String str) throws ParserException {
		Objects.requireNonNull(str);
		index = 0;
		input = TraVarTUtils.splitString(str, REGEX);
		if (input.length > 0) {
			return parseCondition();
		} else {
			return ICondition.TRUE;
		}
	}

	private ICondition parseCondition() {
		ICondition v = term();
		while (symbol.equals(OR)) {
			ICondition r = term();
			v = new Or(v, r);
		}
		return v;
	}

	private ICondition term() {
		ICondition v = comperator();
		while (symbol.equals(AND)) {
			ICondition r = comperator();
			v = new And(v, r);
		}
		return v;
	}

	private ICondition comperator() {
		ICondition v = valueDelimiter();
		while (symbol.equals(EQUAL) || symbol.equals(GREATER) || symbol.equals(LESS)) {
			String first = symbol;
			nextSymbol();
			String second = symbol;
			if (first.equals(EQUAL) && second.equals(EQUAL)) {
				ICondition r = factor();
				v = new Equals(v, r);
			} else if (first.equals(GREATER) && second.equals(EQUAL)) {
				ICondition r = factor();
				v = new GreaterEquals(v, r);
			} else if (first.equals(LESS) && second.equals(EQUAL)) {
				nextSymbol();
				ICondition r = factor();
				v = new LessEquals(v, r);
			} else if (first.equals(GREATER)) {
				// second is operand
				ARangeValue value = null;
				if (RulesParser.isDoubleRangeValue(symbol)) {
					value = new DoubleValue(Double.parseDouble(symbol));
				} else if (RulesParser.isStringRangeValue(dm, symbol)) {
					value = new StringValue(symbol);
				}
				v = new Greater(v, value);
			} else if (first.equals(LESS)) {
				// second is operand
				ARangeValue value = null;
				if (RulesParser.isDoubleRangeValue(symbol)) {
					value = new DoubleValue(Double.parseDouble(symbol));
				} else if (RulesParser.isStringRangeValue(dm, symbol)) {
					value = new StringValue(symbol);
				}
				v = new Less(v, value);
			}
		}
		return v;
	}

	private ICondition valueDelimiter() {
		ICondition v = factor();
		if (symbol.equals(DECISION_VALUE_DELIMITER)) {
			ICondition value = factor();
			if (!(v instanceof IDecision) && !(value instanceof ARangeValue)) {
				throw new IllegalStateException(new ParserException("Expected a " + DecisionValueCondition.class));
			}
			v = new DecisionValueCondition((IDecision) v, (ARangeValue) value);
		}
		return v;
	}

	private ICondition factor() {
		nextSymbol();
		ICondition v = null;
		if (symbol.equals(EOF)) {
			v = ICondition.TRUE;
		} else if (symbol.equals(NOT)) {
			ICondition f = factor();
			v = new Not(f);
		} else if (symbol.equals(OPENING_PARENTHESE)) {
			v = parseCondition();
			nextSymbol(); // we don't care about )
		} else if (symbol.equals(IsTakenFunction.FUNCTION_NAME)) {
			nextSymbol();
			if (symbol.equals(OPENING_PARENTHESE)) {
				ICondition d = parseCondition();
				assert d instanceof IDecision;
				v = new IsTakenFunction((IDecision) d);
				nextSymbol(); // we don't care about )
			}
		} else if (symbol.equals(GetValueFunction.FUNCTION_NAME)) {
			nextSymbol();
			if (symbol.equals(OPENING_PARENTHESE)) {
				ICondition d = parseCondition();
				assert d instanceof IDecision;
				v = new GetValueFunction((IDecision) d);
				nextSymbol(); // we don't care about )
			}
		} else if (symbol.equals(TRUE)) {
			v = ICondition.TRUE;
		} else if (symbol.equals(FALSE)) {
			v = ICondition.FALSE;
		} else if (RulesParser.isDoubleRangeValue(symbol)) {
			v = new DoubleValue(Double.parseDouble(symbol));
			nextSymbol();
		} else if (RulesParser.isStringRangeValue(dm, symbol)) {
			v = new StringValue(symbol);
			nextSymbol();
		} else { // decision
			IDecision d = dm.find(symbol);
			v = d;
			nextSymbol();
		}
		return v;
	}

	private void nextSymbol() {
		if (index == input.length) {
			symbol = EOF;
		} else {
			symbol = input[index++];
		}
	}
}
