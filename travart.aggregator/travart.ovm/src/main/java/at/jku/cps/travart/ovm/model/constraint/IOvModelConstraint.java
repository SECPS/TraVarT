package at.jku.cps.travart.ovm.model.constraint;

import at.jku.cps.travart.core.common.IValidate;
import at.jku.cps.travart.ovm.model.IOvModel;
import at.jku.cps.travart.ovm.model.IOvModelElement;
import at.jku.cps.travart.ovm.model.IOvModelVariationBase;

/**
 * This is the base interface for an {@link IOvModelExcludesConstraint} and for
 * an {@link IOvModelRequiresConstraint}. It contains the common properties of
 * these two constraints.
 *
 * @author johannstoebich
 */
public interface IOvModelConstraint extends IOvModelElement, IValidate {

	/**
	 * This method returns the metainformation of this constraint. It stores all
	 * information which is defined for a constraint in FeatureIDE however cannot be
	 * represented by an OVM.
	 *
	 * @return the metainformation.
	 */
	IOvModelConstraintMetainformation getMetainformation();

	/**
	 * This method returns the well defined source of this constraint as variation
	 * base (variation point or variant).
	 *
	 * @return the source of the variation base (variation point or variant).
	 */
	IOvModelVariationBase getSource();

	/**
	 * This method sets the source of this {@link IOvModel} constraint. An OvModel
	 * constrain has always a well defined source.
	 *
	 * @param source the source as variation base (variation point or variant) of
	 *               the constraint.
	 */
	void setSource(IOvModelVariationBase source);

	/**
	 * This method returns the well defined target of this constraint as variation
	 * base (variation point or variant).
	 *
	 * @return the target of the constraint as variation base (variation point or
	 *         variant).
	 */
	IOvModelVariationBase getTarget();

	/**
	 * This method sets the target of this {@link IOvModel} constraint. An OvModel
	 * constrain has always a well defined target.
	 *
	 * @param target the target variation base (variation point or variant) of the
	 *               constraint.
	 */
	void setTarget(IOvModelVariationBase target);
}
