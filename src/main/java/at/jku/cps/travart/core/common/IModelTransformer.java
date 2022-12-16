package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import de.vill.model.FeatureModel;

public interface IModelTransformer<I> {

	/**
	 * Defines the level of optimization, which the transformation performs.
	 * <p>
	 * </p>
	 * {@code ONE_WAY} optimizes the resulting model as much as possible, e.g.,
	 * reducing the number of modeling elements. It is the default case.
	 * <p>
	 * </p>
	 * {@code ROUDNTRIP} the transformation is performed with the roundtrip, i.e.,
	 * transformation back to the original notation, in mind. The resulting model
	 * avoids information loss as much as possible.
	 *
	 * @author Kevin Feichtinger
	 *
	 */
	enum OPTIMIZING_LEVEL {
		// TODO: Think of additional levels of optimizations? Talk to Dario!
		ONE_WAY, ROUNDTRIP
	}

	FeatureModel transform(I model, String modelName, OPTIMIZING_LEVEL level)
			throws NotSupportedVariabilityTypeException;

	default FeatureModel transform(final I model) throws NotSupportedVariabilityTypeException {
		return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
				OPTIMIZING_LEVEL.ONE_WAY);
	}

	I transform(FeatureModel model, String modelName, OPTIMIZING_LEVEL level)
			throws NotSupportedVariabilityTypeException;

	default I transform(final FeatureModel model) throws NotSupportedVariabilityTypeException {
		return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
				OPTIMIZING_LEVEL.ONE_WAY);
	}

	// information about round trip or one way journey
}
