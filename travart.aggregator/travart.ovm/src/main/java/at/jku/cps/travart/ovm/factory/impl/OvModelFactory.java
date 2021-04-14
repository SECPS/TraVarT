package at.jku.cps.travart.ovm.factory.impl;

import at.jku.cps.travart.ovm.factory.IOvModelFactory;
import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModel;
import at.jku.cps.travart.ovm.model.IOvModelVariant;
import at.jku.cps.travart.ovm.model.IOvModelVariationPoint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelExcludesConstraint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelRequiresConstraint;
import at.jku.cps.travart.ovm.model.constraint.impl.OvModelExcludesConstraint;
import at.jku.cps.travart.ovm.model.constraint.impl.OvModelRequiresConstraint;
import at.jku.cps.travart.ovm.model.impl.Identifiable;
import at.jku.cps.travart.ovm.model.impl.OvModel;
import at.jku.cps.travart.ovm.model.impl.OvModelVariant;
import at.jku.cps.travart.ovm.model.impl.OvModelVariationPoint;

/**
 * This repents a concrete implementation of the {@link IOvModelFactory}.
 *
 * @author johannstoebich
 */
public class OvModelFactory implements IOvModelFactory {

	public static final String ID = "de.ovgu.featureide.core.ovm.factory.OvmModelFactory";

	public static OvModelFactory getInstance() {
		return new OvModelFactory();
	}

	public OvModelFactory() {
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean initExtension() {
		return true;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.fm.core.base.IFactory#create()
	 */
	@Override
	public IOvModel create() {
		return new OvModel(ID);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.factory.IOvModelFactory#createVariant(de.ovgu.featureide.core.ovm.model.IOvModel,
	 *      java.lang.String)
	 */
	@Override
	public IOvModelVariant createVariant(IOvModel ovModel, String name) {
		final OvModelVariant ovModelVariant = new OvModelVariant();
		ovModelVariant.setName(name);
		return ovModelVariant;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.factory.IOvModelFactory#createVariationPoint(de.ovgu.featureide.core.ovm.model.IOvModel,
	 *      java.lang.String)
	 */
	@Override
	public IOvModelVariationPoint createVariationPoint(IOvModel ovModel, String name) {
		final OvModelVariationPoint ovModelVariationPoint = new OvModelVariationPoint();
		ovModelVariationPoint.setName(name);
		return ovModelVariationPoint;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.factory.IOvModelFactory#createRequiresConstraint(de.ovgu.featureide.core.ovm.model.IOvModel)
	 */
	@Override
	public IOvModelRequiresConstraint createRequiresConstraint(IOvModel ovModel) {
		final OvModelRequiresConstraint ovModelRequiresConstraint = new OvModelRequiresConstraint();
		return ovModelRequiresConstraint;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.factory.IOvModelFactory#createExcludesConstraint(de.ovgu.featureide.core.ovm.model.IOvModel)
	 */
	@Override
	public IOvModelExcludesConstraint createExcludesConstraint(IOvModel ovModel) {
		final OvModelExcludesConstraint ovModelExcludesConstraint = new OvModelExcludesConstraint();
		return ovModelExcludesConstraint;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.factory.IOvModelFactory#createIdentifiable(int,
	 *      java.lang.String)
	 */
	@Override
	public IIdentifiable createIdentifiable(int internalIdGiven, String nameGiven) {
		return new Identifiable() {

			{
				setInternalId(internalIdGiven);
				setName(nameGiven);
			}
		};
	}

}
