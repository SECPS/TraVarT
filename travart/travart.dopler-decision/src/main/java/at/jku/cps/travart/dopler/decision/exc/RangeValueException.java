package at.jku.cps.travart.dopler.decision.exc;

/**
 * A {@code RangeValueException} indicates that while setting the value of a
 * decision an error occurred. This means that either, the given RangeValue is
 * not part of the Range of the decision or RangeValue was set even though it
 * was disabled.
 *
 * @author Kevin Feichtinger
 *
 */
public class RangeValueException extends Exception {

	private static final long serialVersionUID = -578805809122054851L;

	/**
	 * Creates a RangeValueException with the given message.
	 *
	 * @param message a string.
	 */
	public RangeValueException(String message) {
		super(message);
	}

	/**
	 * Creates a RangeValueException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public RangeValueException(Exception e) {
		super(e);
	}
}
