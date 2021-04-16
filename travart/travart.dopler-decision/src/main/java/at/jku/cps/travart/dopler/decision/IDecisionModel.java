package at.jku.cps.travart.dopler.decision;

import java.util.Map;
import java.util.Set;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.core.common.IValidate;
import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.IDecision;

@SuppressWarnings("rawtypes")
public interface IDecisionModel extends Set<IDecision>, IValidate {

	/**
	 * Returns the factoryId which created this {@link IDecisionModel}.
	 *
	 * @return the factoryId
	 */
	String getFactoryId();

	/**
	 * Returns the file location where the model is stored.
	 *
	 * @return the file location.
	 */
	String getSourceFile();

	/**
	 * Update the storage location of this {@link IDecisionModel}.
	 *
	 * @param sourceFile the location of this {@link IDecisionModel}.
	 */
	void setSourceFile(String sourceFile);

	void setName(String name);

	String getName();

	void executeRules() throws ActionExecutionException;

	boolean isAddPrefix();

	void setAddPrefix(boolean addPrefix);

	IDecision find(String id);

	void reset() throws RangeValueException;

	Map<IConfigurable, Boolean> getCurrentConfiguration();

	Map<IConfigurable, Boolean> getCurrentConfigurationInclValues();

	void afterValueSelection(IDecision decision) throws RangeValueException;
}
