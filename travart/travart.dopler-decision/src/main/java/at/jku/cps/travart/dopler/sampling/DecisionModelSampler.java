package at.jku.cps.travart.dopler.sampling;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.core.common.ISampler;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.UnsatisfiedCardinalityException;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecisionModelSampler implements ISampler<IDecisionModel> {

	private static final long DEFAULT_NUMBER_OF_VALID_CONFIGS = 1_000_000L;

	private Set<Map<IConfigurable, Boolean>> validConfigs;
	private int configcounter;
	private long maxNumberOfValidConfigs;

	public DecisionModelSampler(final long maxNumberOfValidConfigs) {
		this.maxNumberOfValidConfigs = maxNumberOfValidConfigs;
	}

	public DecisionModelSampler() {
		this(DEFAULT_NUMBER_OF_VALID_CONFIGS);
	}

	public long getMaxNumberOfValidConfigs() {
		return maxNumberOfValidConfigs;
	}

	public void setMaxNumberOfValidConfigs(final long maxNumberOfValidConfigs) {
		this.maxNumberOfValidConfigs = maxNumberOfValidConfigs;
	}

	@Override
	public Set<Map<IConfigurable, Boolean>> sample(final IDecisionModel dm) throws NotSupportedVariablityTypeException {
		try {
			configcounter = 0;
			validConfigs = new HashSet<>();
			createConfigurations(dm, DecisionModelUtils.getSelectableDecisions(dm));
			System.out.println("Configurations generated: " + configcounter);
			return validConfigs;
		} catch (NotSupportedVariablityTypeException | RangeValueException | ActionExecutionException
				| UnsatisfiedCardinalityException e) {
			throw new NotSupportedVariablityTypeException(e);
		}
	}

	private void createConfigurations(final IDecisionModel dm, final Set<IDecision> decisions)
			throws NotSupportedVariablityTypeException, RangeValueException, ActionExecutionException,
			UnsatisfiedCardinalityException {
		// cancel if max number of valid configurations is reached
		if (validConfigs.size() >= maxNumberOfValidConfigs) {
			return;
		}
		configcounter++;
		if (dm.isValid()) {
			Map<IConfigurable, Boolean> validConfig = dm.getCurrentConfigurationInclValues();
			validConfigs.add(validConfig);
		}
		Set<IDecision> nextDecisions = new LinkedHashSet<>(decisions);
		Iterator<IDecision> decisionIterator = nextDecisions.iterator();
		while (decisionIterator.hasNext()) {
			IDecision decision = decisionIterator.next();
			decisionIterator.remove();
			if (!DecisionModelUtils.isEnumDecision(decision)) {
				for (Object o : decision.getRange()) {
					ARangeValue rangeValue = (ARangeValue) o;
					decision.setValue(rangeValue);
					if (DecisionModelUtils.isBooleanDecision(decision) && decision.isSelected()
							&& !decision.isVisible()) {
						decision.setValue(BooleanValue.getFalse());
					} else if (DecisionModelUtils.isNumberDecision(decision) && decision.isSelected()
							&& !decision.isVisible()) {
						decision.reset();
					} else {
						dm.afterValueSelection(decision);
					}
					decision.executeRules();
					createConfigurations(dm, nextDecisions);
				}
			} else {
				EnumDecision enumDecision = (EnumDecision) decision;
				int minSelectedValues = enumDecision.getCardinality().getMin();
				int maxSelectedValues = enumDecision.getCardinality().getMax();
				Set<Set<ARangeValue<String>>> valueSets = DecisionModelUtils
						.powerSetWithMinAndMax(enumDecision.getRange(), minSelectedValues, maxSelectedValues);
				for (Set<ARangeValue<String>> values : valueSets) {
					enumDecision.setValues(values);
					if (decision.isSelected() && !decision.isVisible()) {
						decision.reset();
					} else {
						decision.executeRules();
					}
					createConfigurations(dm, nextDecisions);
				}
			}
		}
	}

	@Override
	public boolean verifySampleAs(final IDecisionModel dm, final Map<IConfigurable, Boolean> sample)
			throws NotSupportedVariablityTypeException {
		try {
			dm.reset();
//			dm.setAddPrefix(true);
			for (Entry<IConfigurable, Boolean> entry : sample.entrySet()) {
				if (entry.getValue()) {
					// TODO: Add prefix if necessary
					IDecision decision = dm.find(entry.getKey().getName());
					if (decision == null) {
						String name = entry.getKey().getName();
						if (name.contains("_")) {
							decision = dm.find(name.substring(0, name.indexOf("_") - 1));
							ARangeValue value = decision.getRangeValue(name.substring(name.indexOf("_")));
							decision.setValue(value);
							dm.afterValueSelection(decision);
						}
					} else {
						decision.setValue(BooleanValue.getTrue());
						dm.afterValueSelection(decision);
					}
				}
			}
			dm.executeRules();
			boolean valid = dm.isValid();
			return valid;
		} catch (RangeValueException | ActionExecutionException e) {
			throw new NotSupportedVariablityTypeException(e);
		}
	}
}
