package at.jku.cps.travart.dopler.decision.exc;

import at.jku.cps.travart.dopler.decision.model.ICondition;

/**
 * A {@code CircleInConditionException} indicates that a condition should be
 * created which would cause a circle in a condition.
 *
 * @author Kevin Feichtinger
 *
 */
public class CircleInConditionException extends Exception {

	private static final long serialVersionUID = -9211076567554989470L;

	private static final String CIRCLE_DETECTED_ERROR = "Circle in the Condition  %s";

	/**
	 * Creates a CircleInConditionException with the given message.
	 *
	 * @param message a string.
	 */
	public CircleInConditionException(String message) {
		super(message);
	}

	/**
	 * Creates a CircleInConditionException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public CircleInConditionException(Exception e) {
		super(e);
	}

	/**
	 * Creates a CircleInConditionException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public CircleInConditionException(ICondition toAdd) {
		this(String.format(CIRCLE_DETECTED_ERROR, toAdd.toString()));
	}
}
