package at.jku.cps.travart.dopler.decision.exc;

import at.jku.cps.travart.dopler.decision.model.ICondition;

/**
 * A {@code ConditionCreationException} indicates that an error occured during
 * creating a condition.
 *
 * @author Kevin Feichtinger
 *
 */
public class ConditionCreationException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String CONDITION_CREATION_ERROR = "Error creating a condition in  %s";

	/**
	 * Creates a ConditionCreationException with the given message.
	 *
	 * @param message a string.
	 */
	public ConditionCreationException(String message) {
		super(message);
	}

	/**
	 * Creates a ConditionCreationException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public ConditionCreationException(Exception e) {
		super(e);
	}

	/**
	 * Creates a ConditionCreationException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public ConditionCreationException(ICondition toAdd) {
		this(String.format(CONDITION_CREATION_ERROR, toAdd.toString()));
	}
}
