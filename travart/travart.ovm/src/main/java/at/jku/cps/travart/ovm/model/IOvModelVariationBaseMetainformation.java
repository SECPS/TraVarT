package at.jku.cps.travart.ovm.model;

import java.util.List;

import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraint;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IPropertyContainer;

/**
 * Manages all additional properties of an {@link IOvModelVariationBase} and
 * therefore for an {@link IOvModelVariationPoint} and {@link IOvModelVariant}.
 * The metainformation stores all information which is defined for a feature in
 * FeatureIDE however cannot be represented by an {@link IOvModelVariationBase}.
 *
 */
public interface IOvModelVariationBaseMetainformation {

	/**
	 * This method returns the custom properties of a variation base (variation
	 * point or variant). The custom properties represent additional properties of a
	 * variation base overtaken from an {@link IFeature} during transformation. They
	 * have been added to a variation base so that no information will be lost
	 * during transformation.
	 *
	 * @return The returned properties.
	 */
	IPropertyContainer getCustomProperties();

	/**
	 * This method returns the description of a variation base (variation point or
	 * variant).
	 *
	 * @return The description of the constraint.
	 */
	String getDescription();

	/**
	 * This method sets the description of a variation base (variation point or
	 * variant).
	 *
	 * @param description the description which will be set.
	 */
	void setDescription(String description);

	/**
	 * This method returns the property abstract.
	 *
	 * @return the property abstract.
	 */
	boolean isAbstract();

	/**
	 * This method sets the property abstract of an {@link IOvModelVariationBase}.
	 *
	 * @param isAbstract the value which will be set.
	 */
	void setAbstract(boolean isAbstract);

	/**
	 * This method returns the property hidden.
	 *
	 * @return the property hidden.
	 */
	boolean isHidden();

	/**
	 * This method sets the property hidden of an {@link IOvModelVariationBase}.
	 *
	 * @param hidden the value which will be set.
	 */
	void setHidden(boolean hidden);

	/**
	 * This method returns the property partOfModelRoot. This property determines if
	 * the variation base (variation point or variant) came from a feature or a
	 * feature model constraint.
	 *
	 * @return the property isPartOfModelRoot.
	 */
	boolean isPartOfOvModelRoot();

	/**
	 * This method sets the property partOfModel of an
	 * {@link IOvModelVariationBase}. This property determines if the variation base
	 * (variation point or variant) came from a feature or a feature model
	 * constraint.
	 *
	 * @param partOfOvModelRoot the value of the parameter.
	 */
	void setPartOfOvModelRoot(boolean partOfOvModelRoot);

	/**
	 * This method returns the referenced constraints. If a variation point came
	 * from a feature model {@link IConstraint} and its child have been transformed
	 * to an {@link IOvModel} constraint, this constraint is referenced here. This
	 * is required for reconstructing the feature model from the corresponding
	 * OvModel correctly.
	 *
	 * @return the constraints which are referenced.
	 * @see #isPartOfOvModelRoot() to determine if a variation point came from a
	 *      feature model {@link IConstraint}.
	 */
	List<IOvModelConstraint> getReferencedConstraints();

	/**
	 * This method sets (overwrites) the referenced constraints of a variation base
	 * (variation point or variant). If a variation point came from a feature model
	 * {@link IConstraint} and its child have been transformed to an
	 * {@link IOvModel} constraint, this constraint is referenced here. This is
	 * required for reconstructing the feature model from the corresponding OvModel
	 * correctly.
	 *
	 * @param referencedConstraints the constraints which will be set.
	 * @see #isPartOfOvModelRoot() to determine if a variation point came from a
	 *      feature model {@link IConstraint}.
	 */
	void setReferencedConstraints(List<IOvModelConstraint> referencedConstraints);

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);
}
