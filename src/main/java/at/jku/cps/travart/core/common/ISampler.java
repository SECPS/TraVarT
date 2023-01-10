package at.jku.cps.travart.core.common;

import java.util.Map;
import java.util.Set;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

/**
 * Defines an interface for sampling a variability model of type <T>.
 *
 * @author Kevin Feichtinger
 *
 * @param <T> The type of model which should be sampled.
 */
public interface ISampler<T> {

	/**
	 * Samples the given variability model and returns a set valid configurations. A
	 * configuration is represented by a Map of a {@link IConfigurable} to a
	 * {@link Boolean} if the {@link IConfigurable} is selected or not.
	 *
	 * @param model the variability model to sample.
	 * @return a set of valid configurations.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	Set<Map<IConfigurable, Boolean>> sampleValidConfigurations(T model) throws NotSupportedVariabilityTypeException;

	/**
	 * Samples the given variability model and returns a set valid configurations. A
	 * configuration is represented by a Map of a {@link IConfigurable} to a
	 * {@link Boolean} if the {@link IConfigurable} is selected or not.
	 *
	 * @param model     the variability model to sample.
	 * @param maxNumber the limit of configurations contained in the set.
	 * @return a set of valid configurations.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	Set<Map<IConfigurable, Boolean>> sampleValidConfigurations(T model, long maxNumber)
			throws NotSupportedVariabilityTypeException;

	/**
	 * Samples the given variability model and returns a set invalid configurations.
	 * A configuration is represented by a Map of a {@link IConfigurable} to a
	 * {@link Boolean} if the {@link IConfigurable} is selected or not.
	 *
	 * @param model the variability model to sample.
	 * @return a set of invalid configurations.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	Set<Map<IConfigurable, Boolean>> sampleInvalidConfigurations(T model) throws NotSupportedVariabilityTypeException;

	/**
	 * Samples the given variability model and returns a set invalid configurations.
	 * A configuration is represented by a Map of a {@link IConfigurable} to a
	 * {@link Boolean} if the {@link IConfigurable} is selected or not.
	 *
	 * @param model     the variability model to sample.
	 * @param maxNumber the limit of configurations contained in the set.
	 * @return a set of invalid configurations.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	Set<Map<IConfigurable, Boolean>> sampleInvalidConfigurations(T model, long maxNumber)
			throws NotSupportedVariabilityTypeException;

	/**
	 * Verifies a given configuration sample, if it is valid in the given
	 * variability model.
	 *
	 * @param model  the variability model to test the configuration sample.
	 * @param sample the configuration sample to test.
	 * @return {@code true} if the given configuration sample is valid in the given
	 *         variability model, {@code false} otherwise.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	boolean verifySampleAs(T model, Map<IConfigurable, Boolean> sample) throws NotSupportedVariabilityTypeException;
}
