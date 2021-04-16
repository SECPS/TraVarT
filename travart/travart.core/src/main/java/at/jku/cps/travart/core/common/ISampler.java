package at.jku.cps.travart.core.common;

import java.util.Map;
import java.util.Set;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;

public interface ISampler<T> {

	Set<Map<IConfigurable, Boolean>> sample(T model) throws NotSupportedVariablityTypeException;

	boolean verifySampleAs(T model, Map<IConfigurable, Boolean> sample) throws NotSupportedVariablityTypeException;
}
