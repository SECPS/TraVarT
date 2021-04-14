package at.jku.cps.travart.core.common;

/**
 * This represents a configurable item of an variablilty model. An item
 * implementing this interface can be selected.
 *
 * @author johannstoebich
 * @author kevinfeichtinger
 */
public interface IConfigurable {

	/**
	 * Returns whether the feature is selected or not.
	 *
	 * @return whether the feature is selected or not.
	 */
	boolean isSelected();

	/**
	 * Selects the feature.
	 *
	 * @param selected
	 */
	void setSelected(boolean selected);

	/**
	 * This method returns the name of an IConfigurable of a variability model
	 *
	 * @return the name of the identifiable.
	 */
	String getName();
}
