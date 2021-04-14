package at.jku.cps.travart.dopler.decision.model;

import java.util.Collection;
import java.util.Set;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ADecision.DecisionType;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;

public interface IDecision<T> extends ICondition, IValue<ARangeValue<T>>, IConfigurable {

	String getId();

	void setId(String id);

	String getQuestion();

	void setQuestion(String question);

	String getDescription();

	void setDescription(String description);

	DecisionType getType();

	void setType(DecisionType type);

	Range<T> getRange();

	ARangeValue<T> getRangeValue(T value);

	ARangeValue<T> getRangeValue(String value);

	boolean containsRangeValue(ARangeValue<T> value);

	void setRange(Range<T> range);

	Set<Rule> getRules();

	void executeRules() throws ActionExecutionException;

	void addRule(Rule rule);

	void addRules(Collection<Rule> rules);

	boolean removeRule(Rule rule);

	void setRules(Set<Rule> rules);

	ICondition getVisiblity();

	void setVisibility(ICondition visiblity);

	boolean isVisible();

	// is only supposed to reset Values of decisions, not removing rules frm a
	// decision
	void reset() throws RangeValueException;
}
