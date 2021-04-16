package at.jku.cps.travart.ovm.factory;

import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModel;
import at.jku.cps.travart.ovm.model.IOvModelVariant;
import at.jku.cps.travart.ovm.model.IOvModelVariationPoint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelExcludesConstraint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelRequiresConstraint;
import de.ovgu.featureide.fm.core.base.IFactory;

/**
 * The factories which produce new OvModels or items of an {@link IOvModel}
 * should implement this interface.
 *
 * @author johannstoebich
 */
public interface IOvModelFactory extends IFactory<IOvModel> {

	/**
	 * Create a new {@link IIdentifiable} for an {@link IOvModel}. Each element of
	 * an {@link IOvModel} is uniquely identified with an identfyable.
	 *
	 * @param internalId the internal id of the {@link IIdentifiable}.
	 * @param name       the name of the {@link IIdentifiable}
	 * @return the new created {@link IIdentifiable}
	 */
	IIdentifiable createIdentifiable(int internalId, String name);

	/**
	 * This method creates a new variant.
	 *
	 * @param ovModel the model for which the variant should be created.
	 * @param name    the name of the new variant.
	 * @return the newly created variant.
	 */
	IOvModelVariant createVariant(IOvModel ovModel, String name);

	/**
	 * This method creates a new variation point.
	 *
	 * @param ovModel the OvModel for which the variation point should be created.
	 * @param name    the name of the variation point.
	 * @return the newly created variation point.
	 */
	IOvModelVariationPoint createVariationPoint(IOvModel ovModel, String name);

	/**
	 * This method creates a requires constraint.
	 *
	 * @param ovModel the OvModel for which the requires constraint is created.
	 * @return the newly created requires constraint.
	 */
	IOvModelRequiresConstraint createRequiresConstraint(IOvModel ovModel);

	/**
	 * The method creates an excludes constraint.
	 *
	 * @param ovModel the OvModel for which the excludes constraint is created.
	 * @return the newly created excludes constraint.
	 */
	IOvModelExcludesConstraint createExcludesConstraint(IOvModel ovModel);
}
