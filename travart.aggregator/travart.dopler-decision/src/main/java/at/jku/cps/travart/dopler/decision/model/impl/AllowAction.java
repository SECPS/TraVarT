package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Objects;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;

@SuppressWarnings("rawtypes")
public class AllowAction implements IAction {

	public static final String FUNCTION_NAME = "allow";

	private static final String RANGE_VALUE_ERROR = "Value %s is not part of decision %s";

	private final IDecision decision;
	private final ARangeValue value;

	public AllowAction(final IDecision decision, final ARangeValue value) {
		this.decision = Objects.requireNonNull(decision);
		this.value = Objects.requireNonNull(value);
	}

	@Override
	public void execute() throws ActionExecutionException {
		if (decision.getRange().contains(value)) {
			value.enable();
		} else {
			throw new ActionExecutionException(
					new RangeValueException(String.format(RANGE_VALUE_ERROR, value, decision.getId())));
		}
	}

	@Override
	public boolean isSatisfied() throws ActionExecutionException {
		if (decision.getRange().contains(value)) {
			return value.isEnabled();
		} else {
			throw new ActionExecutionException(
					new RangeValueException(String.format(RANGE_VALUE_ERROR, value, decision.getId())));
		}
	}

	@Override
	public ICondition getVariable() {
		return decision;
	}

	@Override
	public ARangeValue getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(decision, value);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AllowAction other = (AllowAction) obj;
		return Objects.equals(decision, other.decision) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "allow(" + decision + "." + value + ");";
	}
}
