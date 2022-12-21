package at.jku.cps.travart.core.common;

public interface IModelOptimizer<I> {

	/**
	 * Defines the level of optimization, which the transformation performs.
	 * <p>
	 * </p>
	 * {@code CONSTRAINT} the optimizer tries to minimize the number of constraints
	 * in the given model. This may results in a bigger number of variability units,
	 * e.g., features, decisions...
	 * <p>
	 * </p>
	 * {@code UNIT_OF_VARIABILITY} the optimizer tries to minimize the number of
	 * variability units in the given model. This may results in a bigger number of
	 * constraints.
	 * <p>
	 * </p>
	 * {@code FULL} optimizes the resulting model as much as possible, e.g.,
	 * reducing the number of modeling elements. It is the default case.
	 * <p>
	 * </p>
	 *
	 * @author Kevin Feichtinger
	 *
	 */
	enum OPTIMIZING_LEVEL {
		// TODO: Think of additional levels of optimizations? Talk to Dario!
		FULL, CONSTRAINT, UNIT_OF_VARIABILITY
	}

	void optimize(I model, OPTIMIZING_LEVEL level);

	default void optimize(final I model) {
		optimize(model, OPTIMIZING_LEVEL.FULL);
	}
}
