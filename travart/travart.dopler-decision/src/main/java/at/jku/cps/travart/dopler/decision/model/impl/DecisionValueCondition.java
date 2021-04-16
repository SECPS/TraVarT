package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Objects;

import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;

@SuppressWarnings("rawtypes")
public class DecisionValueCondition implements ICondition {

	private final IDecision decision;
	private final ARangeValue value;

	public DecisionValueCondition(final IDecision decision, final ARangeValue value) {
		this.decision = Objects.requireNonNull(decision);
		this.value = Objects.requireNonNull(value);
	}

	public IDecision getDecision() {
		return decision;
	}

	public ARangeValue getValue() {
		return value;
	}

	@Override
	public boolean evaluate() {
		return decision.getValue().equals(value);
	}

	@Override
	public String toString() {
		return decision + "." + value;
	}
}
