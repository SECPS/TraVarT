package at.jku.cps.travart.dopler.decision.model;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;

public interface IValue<T> {

	T getValue();

	void setValue(T value) throws RangeValueException;

}
