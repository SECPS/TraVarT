package at.jku.cps.travart.core.exception;

public class NotSupportedVariabilityTypeException extends Exception {

    private static final long serialVersionUID = -6029774119851673530L;

    /**
     * Creates a new parser exception with the given message.
     *
     * @param message a string.
     */
    public NotSupportedVariabilityTypeException(final String message) {
        super(message);
    }

    /**
     * Creates a new parser exception with the given sub exception.
     *
     * @param e an exception.
     */
    public NotSupportedVariabilityTypeException(final Exception e) {
        super(e);
    }
}
