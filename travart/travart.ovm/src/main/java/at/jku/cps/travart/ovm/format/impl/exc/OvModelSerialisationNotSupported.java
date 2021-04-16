package at.jku.cps.travart.ovm.format.impl.exc;

/**
 * This exception should be thrown whenever a class should be serialized where
 * no serialization algorithm exists.
 *
 * @author johannstoebich
 */
public class OvModelSerialisationNotSupported extends OvModelSerialisationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public OvModelSerialisationNotSupported(Class<?> class1) {
		super("The serialisation of " + class1.getName() + " is not supported.");
	}

}
