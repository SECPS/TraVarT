package at.jku.cps.travart.dopler.decision.model;

import java.util.Set;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.UnsatisfiedCardinalityException;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;

public interface IEnumerationDecision<T> extends IDecision<T> {

	void setValues(Set<ARangeValue<T>> values) throws RangeValueException, UnsatisfiedCardinalityException;

	Set<ARangeValue<T>> getValues();

	Cardinality getCardinality();

	void setCardinality(Cardinality cardinality);

	ARangeValue<String> getNoneOption();

	boolean isNoneOption(ARangeValue<T> value);

	boolean hasNoneOption();
}
