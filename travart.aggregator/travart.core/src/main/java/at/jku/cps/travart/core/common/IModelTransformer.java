package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;

public interface IModelTransformer<I, R> {

	R transform(I model) throws NotSupportedVariablityTypeException;
}
