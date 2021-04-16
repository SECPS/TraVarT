package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Comparator;
import java.util.NoSuchElementException;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueNotEnabledException;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;

public class NumberDecision extends ADecision<Double> {

	private static final String RANGE_VALUE_ERROR = "Value %s is not a range value of decision %s";
	private static final String RANGE_VALUE_NOT_ENABLED_ERROR = "Value %s is not enabled. Can't be set for Number decision %s";

	private Range<Double> range;
	private ARangeValue<Double> value;

	public NumberDecision(final String id) {
		super(id, DecisionType.DOUBLE);
		range = new Range<>();
		value = getMinRangeValue();
	}

	@Override
	public boolean evaluate() {
		return !Double.isNaN(value.getValue());
	}

	@Override
	public ARangeValue<Double> getValue() {
		return value;
	}

	@Override
	public void setValue(final ARangeValue<Double> value) throws RangeValueException {
		if (value == null || Double.isNaN(value.getValue())) {
			throw new RangeValueException(String.format(RANGE_VALUE_ERROR, value, getId()));
		}
		if (!value.isEnabled()) {
			throw new RangeValueNotEnabledException(String.format(RANGE_VALUE_NOT_ENABLED_ERROR, value, getId()));
		}
		this.value = value;
		setSelected(true);
	}

	@Override
	public Range<Double> getRange() {
		return range;
	}

	@Override
	public void setRange(final Range<Double> range) {
		this.range = range;
		value = getMinRangeValue();
		setSelected(true);
	}

	@Override
	public boolean containsRangeValue(final ARangeValue<Double> value) {
		return range == null || range.contains(value);
	}

	@Override
	public DoubleValue getRangeValue(final Double value) {
//		Optional<ARangeValue<Double>> rangeValue = range.stream().filter(v -> v.getValue() == value).findFirst();
//		if (rangeValue.isPresent()) {
//			return rangeValue.get();
//		}
		return range.contains(new DoubleValue(value)) ? new DoubleValue(value) : null;
	}

	@Override
	public final ARangeValue<Double> getRangeValue(final String str) {
		return getRangeValue(Double.parseDouble(str));
	}

	@Override
	public void reset() throws RangeValueException {
		setValue(getMinRangeValue());
		setSelected(false);
	}

	public ARangeValue<Double> getMinRangeValue() {
		if (range.isEmpty()) {
			return new DoubleValue(0);
		}
		return range.stream().min(Comparator.comparing(ARangeValue<Double>::getValue))
				.orElseThrow(NoSuchElementException::new);
	}

	public ARangeValue<Double> getMaxRangeValue() {
		if (range.isEmpty()) {
			return new DoubleValue(0);
		}
		return range.stream().max(Comparator.comparing(ARangeValue<Double>::getValue))
				.orElseThrow(NoSuchElementException::new);
	}
}
