package at.jku.cps.travart.dopler.decision.model;

import java.util.Objects;

import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;

public abstract class ABinaryCondition implements ICondition {

	private ICondition left;
	private ICondition right;

	public ABinaryCondition(ICondition left, ICondition right) {
		this.left = Objects.requireNonNull(left);
		this.right = Objects.requireNonNull(right);
	}

	public ICondition getLeft() {
		return left;
	}

	public void setLeft(ICondition left) throws CircleInConditionException {
		ICondition toAdd = Objects.requireNonNull(left);
		if (DecisionModelUtils.detectCircle(this, toAdd)) {
			throw new CircleInConditionException(toAdd);
		}
		if (DecisionModelUtils.detectCircle(toAdd, this)) {
			throw new CircleInConditionException(toAdd);
		}
		this.left = toAdd;
	}

	public ICondition getRight() {
		return right;
	}

	public void setRight(ICondition right) throws CircleInConditionException {
		ICondition toAdd = Objects.requireNonNull(right);
		if (DecisionModelUtils.detectCircle(this, toAdd)) {
			throw new CircleInConditionException(toAdd);
		}
		if (DecisionModelUtils.detectCircle(toAdd, this)) {
			throw new CircleInConditionException(toAdd);
		}
		this.right = toAdd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (left == null ? 0 : left.hashCode());
		result = prime * result + (right == null ? 0 : right.hashCode());
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
		ABinaryCondition other = (ABinaryCondition) obj;
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
			return false;
		}
		return true;
	}
}
