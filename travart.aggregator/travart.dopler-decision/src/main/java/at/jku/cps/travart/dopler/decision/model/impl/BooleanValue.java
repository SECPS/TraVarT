package at.jku.cps.travart.dopler.decision.model.impl;

import at.jku.cps.travart.dopler.decision.model.ARangeValue;

public final class BooleanValue extends ARangeValue<Boolean> {

	private static final String SET_VALUE_ERROR = "Boolean value is final in class %s. Use static methods getTrue() or getFalse() instead.";

	public static BooleanValue getTrue() {
		return new BooleanValue(true);
	}

	public static BooleanValue getFalse() {
		return new BooleanValue(false);
	}

	private BooleanValue() {
	}

	private BooleanValue(final Boolean value) {
		super(value);
	}

	@Override
	public void setValue(Boolean value) {
		throw new UnsupportedOperationException(String.format(SET_VALUE_ERROR, getClass().getCanonicalName()));
	}

	@Override
	public boolean lessThan(ARangeValue<Boolean> other) {
		return false;
	}

	@Override
	public boolean greaterThan(ARangeValue<Boolean> other) {
		return false;
	}

	@Override
	public boolean evaluate() {
		return getValue();
	}
}
