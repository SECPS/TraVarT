package at.jku.cps.travart.dopler.decision.exc;

/**
 * A {@code RangeValueNotEnabledException} indicates that while a given value to
 * set in a decision was not enabled.
 *
 * @author Kevin Feichtinger
 *
 */
public class RangeValueNotEnabledException extends RangeValueException {

	private static final long serialVersionUID = -8575734434568473742L;

	/**
	 * Creates a RangeValueNotEnabledException with the given message.
	 *
	 * @param message a string.
	 */
	public RangeValueNotEnabledException(String message) {
		super(message);
	}

	/**
	 * Creates a RangeValueNotEnabledException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public RangeValueNotEnabledException(Exception e) {
		super(e);
	}
}
