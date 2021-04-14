package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;

public class BooleanDecision extends ADecision<Boolean> {

	private final Range<Boolean> range;
	private ARangeValue<Boolean> value;

	public BooleanDecision(final String id) {
		super(id, DecisionType.BOOLEAN);
		range = new Range<>();
		range.add(BooleanValue.getTrue());
		range.add(BooleanValue.getFalse());
		value = BooleanValue.getFalse();
	}

	@Override
	public ARangeValue<Boolean> getValue() {
		return value;
	}

	@Override
	public void setValue(final ARangeValue<Boolean> value) throws RangeValueException {
		this.value = value;
		setSelected(value.getValue());
	}

	@Override
	public boolean evaluate() {
		return value.getValue();
	}

	@Override
	public BooleanValue getRangeValue(final Boolean value) {
		return value ? BooleanValue.getTrue() : BooleanValue.getFalse();
	}

	@Override
	public final ARangeValue<Boolean> getRangeValue(final String str) {
		return getRangeValue(Boolean.valueOf(str));
	}

	@Override
	public Range<Boolean> getRange() {
		return range;
	}

	/**
	 * Does nothing as a boolean has a defined set of values
	 */
	@Override
	public void setRange(final Range<Boolean> range) {
	}

	@Override
	public void reset() throws RangeValueException {
		setValue(BooleanValue.getFalse());
		range.forEach(ARangeValue::enable);
	}

	@Override
	public boolean containsRangeValue(final ARangeValue<Boolean> value) {
		return getRange().contains(value);
	}
}
