package at.jku.cps.travart.ovm.format.impl.exc;

/**
 * This exception should be thrown whenever a specific type is expected however
 * it is found somewhere different.
 *
 * @author johannstoebich
 */
public class OvModelWrongSerialisationOrder extends OvModelSerialisationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public OvModelWrongSerialisationOrder(String name) {
		super("An element of type " + name + " was in the wrong order.");
	}
}
