package at.jku.cps.travart.dopler.decision.exc;

/**
 * A {@code ActionExecutionException} indicates that during executing a action
 * of a rule an error occurred.
 *
 * @author Kevin Feichtinger
 *
 */
public class ActionExecutionException extends Exception {

	private static final long serialVersionUID = 3007866414974173894L;

	/**
	 * Creates a ActionExecutionException with the given message.
	 *
	 * @param message a string.
	 */
	public ActionExecutionException(String message) {
		super(message);
	}

	/**
	 * Creates a ActionExecutionException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public ActionExecutionException(Exception e) {
		super(e);
	}
}
