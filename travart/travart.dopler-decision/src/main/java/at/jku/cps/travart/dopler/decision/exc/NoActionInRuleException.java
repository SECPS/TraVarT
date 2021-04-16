package at.jku.cps.travart.dopler.decision.exc;

public class NoActionInRuleException extends ParserException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new NoActionInRuleException parser exception with the given
	 * message.
	 *
	 * @param message a string.
	 */
	public NoActionInRuleException(String message) {
		super(message);
	}

	/**
	 * Creates a new NoActionInRuleException parser exception with the given sub
	 * exception.
	 *
	 * @param e an exception.
	 */
	public NoActionInRuleException(Exception e) {
		super(e);
	}
}
