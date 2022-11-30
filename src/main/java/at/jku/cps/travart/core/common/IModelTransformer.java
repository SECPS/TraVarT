package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import de.vill.model.FeatureModel;

public interface IModelTransformer<I> {
    FeatureModel transform(I model, String modelName) throws NotSupportedVariabilityTypeException;

    default FeatureModel transform(final I model) throws NotSupportedVariabilityTypeException {
        return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME);
    }

    I transform(FeatureModel model, String modelName) throws NotSupportedVariabilityTypeException;

    default I transform(final FeatureModel model) throws NotSupportedVariabilityTypeException {
        return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME);
    }

    // information about round trip or one way journey
}
