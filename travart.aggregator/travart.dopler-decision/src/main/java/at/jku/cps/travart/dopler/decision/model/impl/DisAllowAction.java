package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Objects;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.IEnumerationDecision;
import at.jku.cps.travart.dopler.decision.model.IValue;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DisAllowAction implements IAction {

	public static final String FUNCTION_NAME = "disAllow";

	private static final String RANGE_VALUE_ERROR = "Value %s is not in range of decision %s";

	private final IDecision decision;
	private final ARangeValue value;

	public DisAllowAction(final IDecision decision, final ARangeValue value) {
		if (!decision.getRange().contains(value)) {

			throw new IllegalArgumentException(String.format(RANGE_VALUE_ERROR, value, decision));
		}
		this.decision = Objects.requireNonNull(decision);
		this.value = Objects.requireNonNull(value);
	}

	@Override
	public void execute() throws ActionExecutionException {
		try {
			if (decision.getRange().contains(value)) {
				value.disable();
				if (decision instanceof IEnumerationDecision && ((IEnumerationDecision) decision).isNoneOption(value)) {
					ICondition vis = decision.getVisiblity();
					if (vis instanceof BooleanDecision && !((BooleanDecision) vis).isSelected()) {
						BooleanDecision d = (BooleanDecision) vis;
						d.setValue(BooleanValue.getTrue());
					}
				}
			} else {
				throw new ActionExecutionException(
						new RangeValueException(String.format(RANGE_VALUE_ERROR, value, decision.getId())));
			}
		} catch (RangeValueException e) {
			throw new ActionExecutionException(e);
		}
	}

	@Override
	public boolean isSatisfied() throws ActionExecutionException {
		if (decision.getRange().contains(value)) {
			return !value.isEnabled();
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
	public IValue getValue() {
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
		DisAllowAction other = (DisAllowAction) obj;
		return Objects.equals(decision, other.decision) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "disAllow(" + decision + "." + value + ");";
	}
}
