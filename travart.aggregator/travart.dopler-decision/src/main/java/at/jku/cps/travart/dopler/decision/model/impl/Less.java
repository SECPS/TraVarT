package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.model.ABinaryCondition;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.ICondition;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Less extends ABinaryCondition {

	public Less(ICondition left, ICondition right) {
		super(left, right);
	}

	@Override
	public boolean evaluate() {
		if (getLeft() instanceof GetValueFunction && getRight() instanceof ARangeValue) {
			GetValueFunction function = (GetValueFunction) getLeft();
			ARangeValue value = (ARangeValue) getRight();
			return function.execute().lessThan(value);
		} else if (getLeft() instanceof ARangeValue && getRight() instanceof GetValueFunction) {
			ARangeValue value = (ARangeValue) getLeft();
			GetValueFunction function = (GetValueFunction) getRight();
			return function.execute().lessThan(value);
		} else if (getLeft() instanceof GetValueFunction && getRight() instanceof GetValueFunction) {
			GetValueFunction leftFunction = (GetValueFunction) getLeft();
			GetValueFunction rightFunction = (GetValueFunction) getRight();
			return leftFunction.execute().lessThan(rightFunction.execute());
		}
		return getLeft().evaluate() && getRight().evaluate();
	}

	@Override
	public String toString() {
		return getLeft() + " < " + getRight();
	}
}
