package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.model.ABinaryCondition;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class And extends ABinaryCondition {

	public And(ICondition left, ICondition right) {
		super(left, right);
	}

	@Override
	public boolean evaluate() {
		return getLeft().evaluate() && getRight().evaluate();
	}

	@Override
	public String toString() {
		return String.format("(%s && %s)", getLeft(), getRight());
	}
}
