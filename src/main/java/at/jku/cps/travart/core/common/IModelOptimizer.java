package at.jku.cps.travart.core.common;

/**
 * Defines a base interface for a variability model optimizer. A optimizer
 * performs in-place operation on a given variability model of type <T>.
 *
 * @author Kevin Feichtinger
 *
 * @param <T> The type of the variability artifact.
 */
public interface IModelOptimizer<T> {

	/**
	 * Defines the strategy of optimization, which the transformation performs.
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
	 * Optimizations may influence the roundtrip transformation of a model.
	 * {@link IModelTransformer.STRATEGY}.
	 * <p>
	 * </p>
	 *
	 * @author Kevin Feichtinger
	 *
	 */
	enum STRATEGY {
		FULL, CONSTRAINT, UNIT_OF_VARIABILITY
	}

	/**
	 * Optimizes the given variability model of type <T> using the given
	 * optimization {@link STRATEGY}.
	 *
	 * @param model    the variability model to optimize.
	 * @param strategy the strategy to perform.
	 */
	void optimize(T model, STRATEGY strategy);

	/**
	 * Optimizes the given variability model of type <T> using the {@code FULL}
	 * {@link STRATEGY}.
	 *
	 * @param model the variability model to optimize.
	 */
	default void optimize(final T model) {
		optimize(model, STRATEGY.FULL);
	}
}
