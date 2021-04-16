package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Arrays;
import java.util.Objects;

import at.jku.cps.travart.dopler.decision.model.AFunction;
import at.jku.cps.travart.dopler.decision.model.IDecision;

@SuppressWarnings("rawtypes")
public class IsTakenFunction extends AFunction<Boolean> {

	public static final String FUNCTION_NAME = "isTaken";

	private IDecision parameter;

	public IsTakenFunction(IDecision parameter) {
		super(FUNCTION_NAME, Arrays.asList(parameter));
		this.parameter = Objects.requireNonNull(parameter);
	}

	@Override
	public Boolean execute() {
		return parameter.isSelected();
	}

	@Override
	public boolean evaluate() {
		return parameter.isSelected();
	}

	@Override
	public String toString() {
		return "isTaken(" + parameter + ")";
	}
}
