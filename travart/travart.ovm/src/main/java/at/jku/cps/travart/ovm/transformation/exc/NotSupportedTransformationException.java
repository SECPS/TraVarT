package at.jku.cps.travart.ovm.transformation.exc;

/**
 * This exception is thrown whenever a transformation is not valid.
 *
 * @author johannstoebich
 */
public class NotSupportedTransformationException extends IllegalStateException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the exception with the respective parameters.
	 *
	 * @param from the class from which the model should be transformed.
	 * @param to   the class to which the model should be transformed.
	 */
	public NotSupportedTransformationException(Class<?> from, Class<?> to) {
		super("The transformation from " + from.getName() + " to " + to.getName() + " is not supported.");
	}

}
