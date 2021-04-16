package at.jku.cps.travart.ovm.model;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IPropertyContainer;

/**
 * Manages all additional properties of an {@link IOvModel}. The metainformation
 * stores all information which is defined for a feature model in FeatureIDE
 * however cannot be represented by an {@link IOvModel}. In this case it is
 * solely the description and the properties are stored.
 *
 */
public interface IOvModelMetainformation {

	/**
	 * This method returns the custom properties of the {@link IOvModel}. The custom
	 * properties represent additional properties of an {@link IOvModel} overtaken
	 * from the {@link IFeatureModel} during transformation. They have been added so
	 * that no information will be lost during transformation.
	 *
	 * @return the custom properties of the ovModel.
	 */
	IPropertyContainer getCustomProperties();

	/**
	 * This method returns the description of an {@link IOvModel}.
	 *
	 * @return The description of the OvModel.
	 */
	String getDescription();

	/**
	 * This method sets the description of an {@link IOvModel}.
	 *
	 * @param description the description which will be set.
	 */
	void setDescription(String description);

}
