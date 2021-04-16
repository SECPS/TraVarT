package at.jku.cps.travart.core.common.exc;

public class NotSupportedVariablityTypeException extends Exception {

	private static final long serialVersionUID = -6029774119851673530L;

	/**
	 * Creates a new parser exception with the given message.
	 *
	 * @param message a string.
	 */
	public NotSupportedVariablityTypeException(String message) {
		super(message);
	}

	/**
	 * Creates a new parser exception with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public NotSupportedVariablityTypeException(Exception e) {
		super(e);
	}
}
