package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.LinkedHashSet;

import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IRange;

public class Range<T> extends LinkedHashSet<ARangeValue<T>> implements IRange<ARangeValue<T>> {

	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Range<?> other = (Range<?>) obj;
		return containsAll(other) && other.containsAll(this);
	}
}
