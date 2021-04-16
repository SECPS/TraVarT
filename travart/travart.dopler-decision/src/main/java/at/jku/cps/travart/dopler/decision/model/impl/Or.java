package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.model.ABinaryCondition;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class Or extends ABinaryCondition {

	public Or(ICondition left, ICondition right) {
		super(left, right);
	}

	@Override
	public String toString() {
		return String.format("%s || %s", getLeft(), getRight());
	}

	@Override
	public boolean evaluate() {
		return getLeft().evaluate() || getRight().evaluate();
	}
}
