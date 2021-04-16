package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.model.AUnaryCondition;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class Not extends AUnaryCondition {

	public Not(ICondition value) {
		super(value);
	}

	@Override
	public String toString() {
		return String.format("!%s", getOperand());
	}

	@Override
	public boolean evaluate() {
		return !getOperand().evaluate();
	}
}
