package at.jku.cps.travart.dopler.decision.model.impl;

public class Cardinality {

	private final int min;
	private final int max;

	public Cardinality(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException("Max cardinality smaller than min cardinality");
		} else if (max < 0 || min < 0) {
			throw new IllegalArgumentException("Cardinality cannot have negative values");
		}
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public boolean isAlternative() {
		return min == 1 && max == 1;
	}

	public boolean isOr() {
		return min == 1 && max > 1;
	}

	public boolean isMutex() {
		return min > 1 && max > 1;
	}

	public boolean isWithinCardinality(int i) {
		return min <= i && i <= max;
	}

	public final boolean isNoCardinality() {
		return min == 0 && max == 0;
	}

	@Override
	public String toString() {
		return isNoCardinality() ? "" : min + ":" + max;
	}
	
	@Override
	public int hashCode() {
		return (min+max)%13;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other ==null) return false;
		if(this == other) return true;
		if(!(other instanceof Cardinality)) {
			return false;
		}
		Cardinality o=(Cardinality)other;
		return this.max==o.max&&this.min==o.min;
	}
}
