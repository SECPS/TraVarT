package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

import java.util.Map;
import java.util.Set;

public interface ISampler<T> {

    Set<Map<IConfigurable, Boolean>> sampleValidConfigurations(T model) throws NotSupportedVariabilityTypeException;

    Set<Map<IConfigurable, Boolean>> sampleValidConfigurations(T model, long maxNumber)
            throws NotSupportedVariabilityTypeException;

    Set<Map<IConfigurable, Boolean>> sampleInvalidConfigurations(T model) throws NotSupportedVariabilityTypeException;

    Set<Map<IConfigurable, Boolean>> sampleInvalidConfigurations(T model, long maxNumber)
            throws NotSupportedVariabilityTypeException;

    boolean verifySampleAs(T model, Map<IConfigurable, Boolean> sample) throws NotSupportedVariabilityTypeException;

}
