package at.jku.cps.travart.ovm.format.impl.exc;

/**
 * This exception should be thrown whenever a specific type is expected however
 * another type has been given.
 *
 * @author johannstoebich
 */
public class OvModelWrongElementException extends OvModelSerialisationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public OvModelWrongElementException(String type) {
		super("An element of type " + type + " was not found.");
	}

	public OvModelWrongElementException(String type, String name) {
		super("The element \"" + name + "\" of type " + type + " was not found.");
	}
}
