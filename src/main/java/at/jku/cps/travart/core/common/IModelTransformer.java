package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import de.vill.model.FeatureModel;

public interface IModelTransformer<I> {

	/**
	 * Defines the level of transformation, which is performs.
	 * <p>
	 * </p>
	 * {@code ONE_WAY} reduces the number of modeling elements as much as possible.
	 * It is the default case.
	 * <p>
	 * </p>
	 * {@code ROUDNTRIP} the transformation is performed with the roundtrip, i.e.,
	 * transformation back to the original notation, in mind. The resulting model
	 * avoids information loss as much as possible.
	 * <p>
	 * </p>
	 *
	 * @author Kevin Feichtinger
	 *
	 */
	enum TRANSFORMATION_LEVEL {
		// TODO: Think of additional levels? Talk to Dario!
		ONE_WAY, ROUNDTRIP
	}

	FeatureModel transform(I model, String modelName, TRANSFORMATION_LEVEL level)
			throws NotSupportedVariabilityTypeException;

	default FeatureModel transform(final I model) throws NotSupportedVariabilityTypeException {
		return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
				TRANSFORMATION_LEVEL.ONE_WAY);
	}

	I transform(FeatureModel model, String modelName, TRANSFORMATION_LEVEL level)
			throws NotSupportedVariabilityTypeException;

	default I transform(final FeatureModel model) throws NotSupportedVariabilityTypeException {
		return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
				TRANSFORMATION_LEVEL.ONE_WAY);
	}

	// TODO: Add information methods about round trip or one way journey
}
