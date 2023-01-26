package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import de.vill.model.FeatureModel;

/**
 * The main class for transforming a variabiltiy model of type <T> into a core
 * model. The core model is a Universal Variability Language (UVL) model,
 * developed by the MODEVAR initiative.
 *
 * @param <I> The type of the variability model.
 * @author Kevin Feichtinger
 * @see <a href="https://doi.org/10.1145/3461001.3471145">UVL SPLC Paper 2021</a>
 * @see <a href="https://github.com/Universal-Variability-Language">UVL Github Repository</a>
 * @see <a href="https://modevar.github.io/">MODEVAR initiative</a>
 */
public interface IModelTransformer<I> {

    FeatureModel transform(I model, String modelName, STRATEGY level)
        throws NotSupportedVariabilityTypeException;

    default FeatureModel transform(final I model, final String modelName)
        throws NotSupportedVariabilityTypeException {
        return this.transform(model, modelName, STRATEGY.ONE_WAY);
    }

    default FeatureModel transform(final I model) throws NotSupportedVariabilityTypeException {
        return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
            STRATEGY.ONE_WAY);
    }

    I transform(FeatureModel model, String modelName, STRATEGY level)
        throws NotSupportedVariabilityTypeException;

    default I transform(final FeatureModel model, final String modelName)
        throws NotSupportedVariabilityTypeException {
        return this.transform(model, modelName, STRATEGY.ONE_WAY);
    }

    default I transform(final FeatureModel model) throws NotSupportedVariabilityTypeException {
        return this.transform(model, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
            STRATEGY.ONE_WAY);
    }

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
     */
    enum STRATEGY {
        ONE_WAY, ROUNDTRIP
    }
}
