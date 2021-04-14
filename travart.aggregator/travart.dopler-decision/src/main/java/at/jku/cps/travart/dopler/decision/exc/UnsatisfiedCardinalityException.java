package at.jku.cps.travart.dopler.decision.exc;

/**
 * A {@code UnsatisfiedCardinalityException} indicates that while setting the
 * value the cardinality of the decision was not satisfied.
 *
 * @author Kevin Feichtinger
 *
 */
public class UnsatisfiedCardinalityException extends Exception {

	private static final long serialVersionUID = -8769506217565482281L;

	/**
	 * Creates a UnsatisfiedCardinalityException with the given message.
	 *
	 * @param message a string.
	 */
	public UnsatisfiedCardinalityException(String message) {
		super(message);
	}

	/**
	 * Creates a UnsatisfiedCardinalityException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public UnsatisfiedCardinalityException(Exception e) {
		super(e);
	}
}
