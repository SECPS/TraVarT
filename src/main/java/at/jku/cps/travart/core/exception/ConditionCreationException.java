package at.jku.cps.travart.core.exception;

/**
 * A {@code ConditionCreationException} indicates that an error occurred during
 * creating a condition.
 *
 * @author Kevin Feichtinger
 */
public class ConditionCreationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a ConditionCreationException with the given message.
     *
     * @param message a string.
     */
    public ConditionCreationException(final String message) {
        super(message);
    }

    /**
     * Creates a ConditionCreationException with the given sub exception.
     *
     * @param e an exception.
     */
    public ConditionCreationException(final Exception e) {
        super(e);
    }

}
