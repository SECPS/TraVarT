package at.jku.cps.travart.ovm.format.impl.exc;

/**
 * This exception should be thrown whenever a certain amount of required
 * elements has not been matched.
 *
 * @author johannstoebich
 */
public class OvModelWrongCountOfElements extends OvModelSerialisationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public OvModelWrongCountOfElements(String type, int expected, int actual) {
		super("The wrong size of " + type + ". Expected: " + expected + "; Actual: " + actual + ".");
	}

}
