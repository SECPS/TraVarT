package at.jku.cps.travart.dopler.decision.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.IEnumerationDecision;
import at.jku.cps.travart.dopler.decision.model.impl.AllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;

@SuppressWarnings("rawtypes")
public class DecisionModel extends LinkedHashSet<IDecision> implements IDecisionModel {

	public static final String DEFAULT_NAME = "DecisionModel";
	public static final String DEFAULT_DECISION_ID_PREFIX = "d_";

	private static final long serialVersionUID = -2787196196024386464L;

	private final String factoryId;

	private String sourceFile;
	private String name;
	private boolean addPrefix;

	public DecisionModel(final String factoryId) {
		this(factoryId, DEFAULT_NAME, true);
	}

	public DecisionModel(final String factoryId, final String name, final boolean addPrefix) {
		this.factoryId = Objects.requireNonNull(factoryId);
		this.name = Objects.requireNonNull(name);
		this.addPrefix = addPrefix;
	}

	@Override
	public void afterValueSelection(final IDecision decision) throws RangeValueException {
		for (IDecision dmdDecision : this) {
			if (DecisionModelUtils.isEnumDecision(dmdDecision)) {
				EnumDecision enumDecision = (EnumDecision) dmdDecision;
				for (ARangeValue<String> rangeValue : enumDecision.getRange()) {
					IDecision rangeDecision = find(rangeValue.getValue());
					if (rangeDecision == decision) {
						enumDecision.setValue(rangeValue);
					}
				}
			}
		}
	}

	@Override
	public final void executeRules() throws ActionExecutionException {
		for (IDecision decision : this) {
			if (decision.isSelected()) {
				decision.executeRules();
			}
		}
	}

	@Override
	public IDecision find(final String id) {
		final String pId = addPrefix && !id.startsWith(DEFAULT_DECISION_ID_PREFIX) ? DEFAULT_DECISION_ID_PREFIX + id
				: id;
		for (IDecision d : this) {
			if (d.getId().equals(pId)) {
				return d;
			}
		}
		return null;
	}

	public Set<EnumDecision> findWithRangeValue(final IDecision decision) {
		Set<EnumDecision> decisions = new HashSet<>();
		for (IDecision d : this) {
			if (DecisionModelUtils.isEnumDecision(d)) {
				EnumDecision enumDecision = DecisionModelUtils.toEnumDecision(d);
				ARangeValue value = enumDecision
						.getRangeValue(DecisionModelUtils.retriveFeatureName(decision, addPrefix, true));
				if (value != null) {
					decisions.add(enumDecision);
				}
			}
		}
		return decisions;
	}

	public Set<IDecision> findWithVisibility(final IDecision decision) {
		Set<IDecision> decisions = new HashSet<>();
		for (IDecision d : this) {
			if (d.getVisiblity() == decision) {
				decisions.add(d);
			} else if (DecisionModelUtils.isMandatoryVisibilityCondition(d.getVisiblity())) {
				And and = (And) d.getVisiblity();
				ADecision mandatory = (ADecision) (and.getLeft() == ICondition.FALSE ? and.getRight() : and.getLeft());
				if (mandatory == decision) {
					decisions.add(d);
				}
			}
		}
		return decisions;
	}

	@Override
	public String getFactoryId() {
		return factoryId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSourceFile() {
		return sourceFile;
	}

	@Override
	public boolean isAddPrefix() {
		return addPrefix;
	}

	@Override
	public boolean isValid() {
		try {
			if (DecisionModelUtils.getSelectedDecisions(this).isEmpty()) {
				return false;
			}
			for (IDecision decision : this) {
				if (decision.isSelected() && decision.getVisiblity() != ICondition.FALSE) {
					// if selected, the decision must be visible as well, as long the decision is no
					// mandatory feature or a decision based on a Enum value
					if (!decision.isVisible()
							&& !DecisionModelUtils.isMandatoryVisibilityCondition(decision.getVisiblity())
							|| DecisionModelUtils.isMandatoryVisibilityCondition(decision.getVisiblity())
									&& !DecisionModelUtils.retriveMandatoryVisibilityCondition(decision.getVisiblity())
											.isSelected()) {
						return false;
					}
					// enumeration decision always must have a value, if they are selected (if
					// optional they have the none
					// value); values set of enumerations isn't allowed to be empty.
					if (decision instanceof EnumDecision && ((EnumDecision) decision).getValues().isEmpty()) {
						return false;
					}
				}
				// if selected, all rules must be satisfied, if the rule condition holds
				for (Object o : decision.getRules()) {
					Rule rule = (Rule) o;
					if (rule.getCondition().evaluate()) {
						// test if the rule actions are satisfied
						// TODO: Review: A value may be allowed by setting another value, but it can be
						// disallowed by other conditions, so the only actions to be checked are all
						// others than AllowFunctions
						// TODO: Different interpretation of the AllowFunction and the DisAllowFunction.
						// As long the actual value is different the model is valid. Enabled flag for
						// configurators
						if (rule.getAction() instanceof AllowAction) {
							// nothing to do here
						} else if (rule.getAction() instanceof DisAllowAction) {
							DisAllowAction daf = (DisAllowAction) rule.getAction();
							if (daf.getVariable() instanceof IEnumerationDecision) {
								IEnumerationDecision variable = (IEnumerationDecision) daf.getVariable();
								if (variable.getValues().contains(daf.getValue())) {
									return false;
								}
							} else {
								IDecision variable = (IDecision) daf.getVariable();
								if (!variable.getValue().equals(daf.getValue())) {
									return false;
								}
							}
						} else if (!rule.getAction().isSatisfied()) {
							return false;
						}
					}
				}
			}
			return true;
		} catch (ActionExecutionException e) {
			return false;
		}
	}

	@Override
	public void reset() throws RangeValueException {
		for (IDecision decision : this) {
			decision.reset();
		}
	}

	@Override
	public void setAddPrefix(final boolean addPrefix) {
		this.addPrefix = addPrefix;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setSourceFile(final String sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DecisionModel");
		builder.append(name);
		builder.append("[");
		for (IDecision decision : this) {
			builder.append(decision);
			builder.append("[selected=");
			builder.append(decision.isSelected());
			builder.append("; value=");
			builder.append(decision.getValue());
			builder.append("]");
			builder.append("; ");
		}
		if (builder.lastIndexOf(";") >= 0) {
			builder.deleteCharAt(builder.lastIndexOf(";"));
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean add(final IDecision decision) {
		if (addPrefix) {
			decision.setId(DEFAULT_DECISION_ID_PREFIX + decision.getId());
		}
		return super.add(decision);
	}

	@Override
	public Map<IConfigurable, Boolean> getCurrentConfiguration() {
		Map<IConfigurable, Boolean> configuration = new HashMap<>();
		for (IDecision decision : this) {
			configuration.put(decision, decision.isSelected());
		}
		return configuration;
	}

	@Override
	public Map<IConfigurable, Boolean> getCurrentConfigurationInclValues() {
		Map<IConfigurable, Boolean> configuration = new HashMap<>();
		for (IDecision decision : this) {
			configuration.put(decision, decision.isSelected());
			if (DecisionModelUtils.isEnumDecision(decision)) {
				EnumDecision enumDecision = DecisionModelUtils.toEnumDecision(decision);
				for (ARangeValue value : enumDecision.getValues()) {
					IConfigurable configurable = new IConfigurable() {
						boolean selected;

						@Override
						public void setSelected(final boolean selected) {
							this.selected = selected;
						}

						@Override
						public boolean isSelected() {
							return selected;
						}

						@Override
						public String getName() {
							return value.toString();
						}
					};
					configurable.setSelected(enumDecision.isSelected());
					configuration.put(configurable, enumDecision.isSelected());
				}
			}
			if (DecisionModelUtils.isNumberDecision(decision)) {
				NumberDecision numberDecision = DecisionModelUtils.toNumberDecision(decision);
				IConfigurable configurable = new IConfigurable() {
					boolean selected;

					@Override
					public void setSelected(final boolean selected) {
						this.selected = selected;
					}

					@Override
					public boolean isSelected() {
						return selected;
					}

					@Override
					public String getName() {
						return numberDecision + "_" + numberDecision.getValue();
					}
				};
				configurable.setSelected(numberDecision.isSelected());
				configuration.put(configurable, numberDecision.isSelected());
			}
		}
		return configuration;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DecisionModel))
			return false;
		DecisionModel other = (DecisionModel) o;
		if (!name.equals(other.name))
			return false;
		if (this.isAddPrefix() != other.isAddPrefix())
			return false;
		if (size() != other.size())
			return false;
		if (!this.containsAll(other))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int i = 1;
		if (this.addPrefix)
			i = 2;
		int hash = 0;
		for (IDecision d : this) {
			hash += d.hashCode();
		}
		return (hash * i) % 14851;
	}
}
