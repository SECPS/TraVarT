package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueNotEnabledException;
import at.jku.cps.travart.dopler.decision.exc.UnsatisfiedCardinalityException;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IEnumerationDecision;

public class EnumDecision extends ADecision<String> implements IEnumerationDecision<String> {

	private static final String RANGE_VALUE_ERROR = "Value %s is not a range value of decision %s";
	private static final String UNSATISFIED_CARDINALITY_ERROR = "Number of selected values does not satisfy cardinality of decision %s";
	private static final String RANGE_VALUE_NOT_ENABLED_ERROR = "Value %s is not enabled. Can't be set for Enumeration decision %s";
	private static final String NONE_VALUE = "None";

	private static StringValue noneOption;

	private Range<String> range;
	// depending on cardinality a min or max selection is possible
	private final Set<ARangeValue<String>> values;
	private Cardinality cardinality;

	public EnumDecision(final String id) {
		this(id, new Cardinality(0, 1));
	}

	public EnumDecision(final String id, final Cardinality cardinality) {
		super(id, DecisionType.ENUM);
		range = new Range<>();
		values = new HashSet<>();
		this.cardinality = cardinality;
	}

	@Override
	public Range<String> getRange() {
		return range;
	}

	@Override
	public ARangeValue<String> getRangeValue(final String value) {
		return range.contains(new StringValue(value)) ? new StringValue(value) : null;
	}

	@Override
	public void setRange(final Range<String> range) {
		this.range = range;
		values.clear();
	}

	@Override
	public Cardinality getCardinality() {
		return cardinality;
	}

	@Override
	public void setCardinality(final Cardinality cardinality) {
		this.cardinality = Objects.requireNonNull(cardinality);
	}

	@Override
	public void reset() throws RangeValueException {
		values.clear();
		setSelected(false);
		range.forEach(ARangeValue::enable);
	}

	@Override
	public boolean evaluate() {
		return !values.isEmpty() && values.size() >= cardinality.getMin() && values.size() <= cardinality.getMax();
	}

	@Override
	public ARangeValue<String> getValue() {
		if (values.isEmpty() && hasNoneOption()) {
			values.add(getNoneOption());
		}
		if (values.isEmpty() && !hasNoneOption()) {
			return new StringValue(" ");
		}
		return values.stream().findAny().get();
	}

	@Override
	public void setValue(final ARangeValue<String> value) throws RangeValueException {
		if (!range.contains(value)) {
			throw new RangeValueException(String.format(RANGE_VALUE_ERROR, value, getId()));
		}
		if (!value.isEnabled()) {
			throw new RangeValueNotEnabledException(String.format(RANGE_VALUE_NOT_ENABLED_ERROR, value, getId()));
		}
		if (!cardinality.isWithinCardinality(1)) {
			throw new RangeValueException(
					new UnsatisfiedCardinalityException(String.format(UNSATISFIED_CARDINALITY_ERROR, getId())));
		}
		values.clear();
		values.add(value);
		setSelected(!isNoneOption(value));
	}

	@Override
	public void setValues(final Set<ARangeValue<String>> values)
			throws RangeValueException, UnsatisfiedCardinalityException {
		for (ARangeValue<String> value : values) {
			if (!range.contains(value)) {
				throw new RangeValueException(String.format(RANGE_VALUE_ERROR, value, getId()));
			}
			if (!value.isEnabled()) {
				throw new RangeValueNotEnabledException(String.format(RANGE_VALUE_NOT_ENABLED_ERROR, value, getId()));
			}
		}
		if (!cardinality.isWithinCardinality(values.size())) {
			throw new UnsatisfiedCardinalityException(String.format(UNSATISFIED_CARDINALITY_ERROR, getId()));
		}
		this.values.clear();
		this.values.addAll(values);
		setSelected(!values.stream().anyMatch(this::isNoneOption));
	}

	@Override
	public Set<ARangeValue<String>> getValues() {
		if (values.isEmpty() && hasNoneOption()) {
			values.add(getNoneOption());
		}
		return values;
	}

	@Override
	public boolean hasNoneOption() {
		return range.stream().anyMatch(this::isNoneOption);
	}

	@Override
	public ARangeValue<String> getNoneOption() {
		if (noneOption == null) {
			noneOption = new StringValue(NONE_VALUE);
		}
		return noneOption;
	}

	@Override
	public boolean isNoneOption(final ARangeValue<String> value) {
		return getNoneOption() == value;
	}

	@Override
	public boolean containsRangeValue(final ARangeValue<String> value) {
		return getRange().contains(value);
	}
}
