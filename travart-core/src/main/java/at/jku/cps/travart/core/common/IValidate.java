package at.jku.cps.travart.core.common;

/**
 * This is an interface defines a method in order to validate the variability
 * representation implementing it.
 *
 * @author johannstoebich
 */
public interface IValidate {

    /**
     * This method returns whether an configuration is valid or not.
     *
     * @return true whenever the current configuration is valid.
     */
    boolean isValid();
}
