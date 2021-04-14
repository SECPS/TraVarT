package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Objects;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IValue;

public class SelectDecisionAction implements IAction {

	private final ADecision<Boolean> decision;

	public SelectDecisionAction(ADecision<Boolean> decision) {
		this.decision = Objects.requireNonNull(decision);
	}

	@Override
	public void execute() throws ActionExecutionException {
		try {
			decision.setValue(BooleanValue.getTrue());
		} catch (RangeValueException e) {
			throw new ActionExecutionException(e);
		}
	}

	@Override
	public boolean isSatisfied() throws ActionExecutionException {
		return decision.isSelected();
	}

	@Override
	public ICondition getVariable() {
		return decision;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public IValue getValue() {
		return BooleanValue.getTrue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(decision);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SelectDecisionAction other = (SelectDecisionAction) obj;
		return Objects.equals(decision, other.decision);
	}

	@Override
	public String toString() {
		return decision + " = true;";
	}
}
