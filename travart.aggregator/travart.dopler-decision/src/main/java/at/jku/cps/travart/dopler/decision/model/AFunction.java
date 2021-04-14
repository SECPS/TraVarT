package at.jku.cps.travart.dopler.decision.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public abstract class AFunction<R> implements ICondition {

	private final String name;
	private List<IDecision> parameters;

	public AFunction(String name, List<IDecision> paramters) {
		this.name = Objects.requireNonNull(name);
		if (paramters.contains(null)) {
			throw new NullPointerException();
		}
		this.parameters = Objects.requireNonNull(paramters);
	}

	public AFunction(String name, IDecision... paramters) {
		this(name, Arrays.asList(paramters));
	}

	public abstract R execute();

	public String getName() {
		return name;
	}

	public List<IDecision> getParameters() {
		return parameters;
	}

	public void setParameters(List<IDecision> parameters) {
		this.parameters = parameters;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AFunction)) {
			return false;
		}
		if (this == o) {
			return true;
		}
		AFunction other = (AFunction) o;
		if (this.name.equals(other.name) && this.parameters.equals(other.parameters)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, parameters);
	}
}
