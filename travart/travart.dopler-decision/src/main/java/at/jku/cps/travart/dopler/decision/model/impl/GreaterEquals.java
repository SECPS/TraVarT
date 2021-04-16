package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.model.ABinaryCondition;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.ICondition;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GreaterEquals extends ABinaryCondition {

	public GreaterEquals(ICondition left, ICondition right) {
		super(left, right);
	}

	@Override
	public boolean evaluate() {
		if (getLeft() instanceof GetValueFunction && getRight() instanceof ARangeValue) {
			GetValueFunction function = (GetValueFunction) getLeft();
			ARangeValue value = (ARangeValue) getRight();
			return function.execute().greaterThan(value) || function.execute().equalTo(value);
		} else if (getLeft() instanceof ARangeValue && getRight() instanceof GetValueFunction) {
			ARangeValue value = (ARangeValue) getLeft();
			GetValueFunction function = (GetValueFunction) getRight();
			return value.greaterThan(function.execute()) || value.equalTo(function.execute());
		} else if (getLeft() instanceof GetValueFunction && getRight() instanceof GetValueFunction) {
			GetValueFunction leftFunction = (GetValueFunction) getLeft();
			GetValueFunction rightFunction = (GetValueFunction) getRight();
			return leftFunction.execute().greaterThan(rightFunction.execute())
					|| leftFunction.execute().equalTo(rightFunction.execute());
		}
		return getLeft().evaluate() && getRight().evaluate();
	}

	@Override
	public String toString() {
		return getLeft() + " >= " + getRight();
	}
}
