package at.jku.cps.travart.dopler.decision.model;

public interface IRangeValue<T> extends IValue<T>, IValueCompareable<ARangeValue<T>>, ICondition {

	void enable();

	void disable();

	boolean isEnabled();
}
