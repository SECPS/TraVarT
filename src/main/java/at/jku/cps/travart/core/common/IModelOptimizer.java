/*******************************************************************************
 * TODO: explanation what the class does
 *  
 * @author Kevin Feichtinger
 *  
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

/**
 * Defines a base interface for a variability model optimizer. A optimizer
 * performs in-place operation on a given variability model of type <T>.
 *
 * @param <T> The type of the variability artifact.
 * @author Kevin Feichtinger
 */
public interface IModelOptimizer<T> {
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
        this.optimize(model, STRATEGY.FULL);
    }

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
     */
    enum STRATEGY {
        FULL, CONSTRAINT, UNIT_OF_VARIABILITY
    }
}
