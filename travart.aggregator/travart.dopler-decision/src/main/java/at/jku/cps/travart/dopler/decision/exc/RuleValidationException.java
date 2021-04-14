package at.jku.cps.travart.dopler.decision.exc;

/**
 * A {@code RuleValidationException} indicates that during rule validation an
 * error occurred, which makes the decision model invalid.
 *
 * @author Kevin Feichtinger
 *
 */
public class RuleValidationException extends RangeValueException {

	private static final long serialVersionUID = -4744990597648698937L;

	/**
	 * Creates a RuleValidationException with the given message.
	 *
	 * @param message a string.
	 */
	public RuleValidationException(String message) {
		super(message);
	}

	/**
	 * Creates a RuleValidationException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public RuleValidationException(Exception e) {
		super(e);
	}
}
