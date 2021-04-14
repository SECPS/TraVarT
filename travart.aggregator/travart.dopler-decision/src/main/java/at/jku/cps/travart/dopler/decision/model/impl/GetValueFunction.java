package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Arrays;
import java.util.Objects;

import at.jku.cps.travart.dopler.decision.model.AFunction;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IDecision;

@SuppressWarnings("rawtypes")
public class GetValueFunction extends AFunction<ARangeValue> {

	public static final String FUNCTION_NAME = "getValue";

	private IDecision parameter;

	public GetValueFunction(IDecision parameter) {
		super(FUNCTION_NAME, Arrays.asList(parameter));
		this.parameter = Objects.requireNonNull(parameter);
	}

	public IDecision getDecision() {
		return parameter;
	}

	@Override
	public boolean evaluate() {
		return parameter.evaluate();
	}

	@Override
	public ARangeValue execute() {
		Object obj = parameter.getValue();
		if (!(obj instanceof ARangeValue)) {
			throw new IllegalStateException("Parameter has not returned a " + ARangeValue.class);
		}
		return (ARangeValue) obj;
	}

	@Override
	public String toString() {
		return "getValue(" + parameter + ")";
	}
}
