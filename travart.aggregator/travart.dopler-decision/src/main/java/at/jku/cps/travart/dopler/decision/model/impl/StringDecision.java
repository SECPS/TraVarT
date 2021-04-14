package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueNotEnabledException;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;

public class StringDecision extends ADecision<String> {

	private static final String RANGE_VALUE_NULL_ERROR = "Given value for decision %s is null";
	private static final String RANGE_VALUE_NOT_ENABLED_ERROR = "Value %s is not enabled. Can't be set for String decision %s";

	private ARangeValue<String> value;

	public StringDecision(final String id) {
		super(id, DecisionType.STRING);
		value = new StringValue("");
	}

	@Override
	public boolean evaluate() {
		return !value.getValue().isEmpty();
	}

	@Override
	public ARangeValue<String> getValue() {
		return value;
	}

	@Override
	public void setValue(final ARangeValue<String> value) throws RangeValueException {
		if (value == null) {
			throw new RangeValueException(String.format(RANGE_VALUE_NULL_ERROR, getId()));
		}
		if (!value.isEnabled()) {
			throw new RangeValueNotEnabledException(String.format(RANGE_VALUE_NOT_ENABLED_ERROR, value, getId()));
		}
		this.value = value;
		setSelected(true);
	}

	@Override
	public Range<String> getRange() {
		return null;
	}

	@Override
	public void setRange(final Range<String> range) {
	}

	@Override
	public StringValue getRangeValue(final String str) {
		return new StringValue(str);
	}

	@Override
	public void reset() throws RangeValueException {
		setValue(new StringValue(""));
		setSelected(false);
	}

	@Override
	public boolean containsRangeValue(final ARangeValue<String> value) {
		return false;
	}
}
