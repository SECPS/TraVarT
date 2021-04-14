package at.jku.cps.travart.dopler.decision.model;

public interface IValueCompareable<T> {

	boolean equalTo(T other);

	boolean lessThan(T other);

	boolean greaterThan(T other);
}
