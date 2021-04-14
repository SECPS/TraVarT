package at.jku.cps.travart.dopler.decision.exc;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = -1246283446216719836L;

	/**
	 * Creates a new parser exception with the given message.
	 *
	 * @param message a string.
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Creates a new parser exception with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public ParserException(Exception e) {
		super(e);
	}
}
