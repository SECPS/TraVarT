package at.jku.cps.travart.dopler.decision.model;

import java.util.Objects;

import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;

public abstract class AUnaryCondition implements ICondition {

	private ICondition operand;

	public AUnaryCondition(ICondition operand) {
		this.operand = Objects.requireNonNull(operand);
	}

	public ICondition getOperand() {
		return operand;
	}

	public void setOperand(ICondition operand) throws CircleInConditionException {
		ICondition toAdd = Objects.requireNonNull(operand);
		if (DecisionModelUtils.detectCircle(this, toAdd)) {
			throw new CircleInConditionException(toAdd);
		}
		if (DecisionModelUtils.detectCircle(toAdd, this)) {
			throw new CircleInConditionException(toAdd);
		}
		this.operand = toAdd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (operand == null ? 0 : operand.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AUnaryCondition other = (AUnaryCondition) obj;
		if (operand == null) {
			if (other.operand != null) {
				return false;
			}
		} else if (!operand.equals(other.operand)) {
			return false;
		}
		return true;
	}
}
