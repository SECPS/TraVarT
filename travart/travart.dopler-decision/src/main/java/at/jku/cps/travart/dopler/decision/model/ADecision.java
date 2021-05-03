package at.jku.cps.travart.dopler.decision.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;

@SuppressWarnings("rawtypes")
public abstract class ADecision<T> implements IDecision<T> {

	public enum DecisionType {
		BOOLEAN("Boolean"), DOUBLE("Double"), STRING("String"), ENUM("Enumeration");

		private String type;

		DecisionType(final String type) {
			this.type = type;
		}

		public boolean equalString(final String type) {
			return this.type.equals(type);
		}

		@Override
		public String toString() {
			return type;
		}
	}

	private boolean select;
	private String id;
	private String question;
	private String description;
	private DecisionType type;
	private Set<Rule> rules;
	private ICondition visibility;

	protected ADecision(final String id, final DecisionType type) {
		this.id = Objects.requireNonNull(id);
		this.description = "";
		this.question = "";
		this.type = Objects.requireNonNull(type);
		rules = new HashSet<>();
		select = false;
		visibility = ICondition.TRUE;
	}

	@Override
	public final void setSelected(final boolean select) {
		this.select = select;
	}

	@Override
	public final void executeRules() throws ActionExecutionException {
		for (Rule rule : getRules()) {
			try {
				if (rule.getCondition().evaluate()) {
					if (!rule.getAction().isSatisfied()) {
						rule.getAction().execute();
					}
				}
			} catch (ActionExecutionException e) {
				throw new ActionExecutionException(e);
			}
		}
	}

	@Override
	public final boolean isSelected() {
		return select;
	}

	@Override
	public Set<Rule> getRules() {
		return rules;
	}

	@Override
	public void addRule(final Rule rule) {
		rules.add(rule);
	}

	@Override
	public void setRules(final Set<Rule> rules) {
		this.rules = rules;
	}

	@Override
	public boolean removeRule(final Rule rule) {
		return rules.remove(rule);
	}

	@Override
	public void addRules(final Collection<Rule> rules) {
		this.rules.addAll(rules);
	}

	@Override
	public String getQuestion() {
		return question;
	}

	@Override
	public void setQuestion(final String question) {
		this.question = question;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public DecisionType getType() {
		return type;
	}

	@Override
	public void setType(final DecisionType type) {
		this.type = type;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public ICondition getVisiblity() {
		return visibility;
	}

	@Override
	public void setVisibility(final ICondition visibility) {
		this.visibility = visibility;
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public final boolean isVisible() {
		return visibility.evaluate();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, this.getClass());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ADecision)) {
			return false;
		}
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}
		ADecision other = (ADecision) obj;
		if (!Objects.equals(id, other.id))
			return false;
		if (!Objects.equals(type, other.type))
			return false;
		if (!Objects.equals(rules, other.rules))
			return false;
		if (!Objects.equals(visibility, other.visibility))
			return false;
		return true;
	}
}
