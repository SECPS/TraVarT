package at.jku.cps.travart.dopler.decision.model.impl;

import java.util.Objects;

import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;

public class Rule {

	private ICondition condition;
	private IAction action;

	public Rule(ICondition condition, IAction action) {
		this.condition = Objects.requireNonNull(condition);
		this.action = Objects.requireNonNull(action);
	}

	public IAction getAction() {
		return action;
	}

	public void setAction(IAction action) {
		this.action = action;
	}

	public void setCondition(ICondition condition) {
		this.condition = condition;
	}

	public ICondition getCondition() {
		return condition;
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, condition);
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
		Rule other = (Rule) obj;
		return Objects.equals(action, other.action) && Objects.equals(condition, other.condition);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("if ");
		if (condition instanceof IDecision) {
			builder.append("(");
			builder.append(condition);
			builder.append(")");
		} else {
			builder.append(condition);
		}
		builder.append(" {\n");
		builder.append(action);
		builder.append("\n");
		builder.append("}\n");
		return builder.toString();
	}
}
