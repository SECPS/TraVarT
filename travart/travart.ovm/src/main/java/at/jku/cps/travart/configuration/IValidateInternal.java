package at.jku.cps.travart.configuration;

/**
 * This represents an item which can be validated.
 *
 * @author johannstoebich
 */
public interface IValidateInternal {

	/**
	 * This method evaluates whether an item is true or not by respecting if it is
	 * mandatory or not. This interface should only be used for internal purpuses.
	 *
	 * @param isMandatory hands in whether the item is a mandatory item or not.
	 * @return
	 */
	boolean isValid(boolean isMandatory);
}
