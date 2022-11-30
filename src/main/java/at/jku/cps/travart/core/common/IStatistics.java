package at.jku.cps.travart.core.common;

/**
 * @param <T> The type of model for which necessary statistics should be implemented
 * @author Prankur Agarwal
 */
public interface IStatistics<T> {
    Integer getVariabilityElementsCount(T model);

    Integer getConstraintsCount(T model);
}