package at.jku.cps.travart.ovm.model.constraint;

import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IPropertyContainer;

/**
 * Manages all additional properties of an {@link IOvModelConstraint} and
 * therefore for an {@link IOvModelExcludesConstraint} and
 * {@link IOvModelRequiresConstraint}. The metainformation stores all
 * information which is defined for a constraint in FeatureIDE however cannot be
 * represented by an {@link IOvModelConstraint}.
 *
 */
public interface IOvModelConstraintMetainformation {

	/**
	 * This method returns the custom properties of a constraint. The custom
	 * properties represent additional properties of a constraint overtaken from a
	 * feature model {@link IConstraint} during transformation. They have been added
	 * to a constraint so that no information will be lost during transformation.
	 *
	 * @return The returned properties.
	 */
	IPropertyContainer getCustomProperties();

	/**
	 * This method returns the description of the constraint.
	 *
	 * @return The description of the constraint
	 */
	String getDescription();

	/**
	 * This method sets the description of the constraint.
	 *
	 * @param description the description which will be set.
	 */
	void setDescription(String description);

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);
}
