package at.jku.cps.travart.ovm.model.impl;

import java.util.Objects;

import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModelElement;

/**
 * Represents a concrete implementation of an {@link IOvModelElement}.
 *
 * @see IOvModelElement
 *
 * @author johannstoebich
 */
public abstract class OvModelElement extends Identifiable implements IOvModelElement {

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelElement#getElement(de.ovgu.featureide.core.ovm.model.IIdentifiable)
	 */
	@Override
	public IOvModelElement getElement(IIdentifiable identifiable) {
		if (identifiable == null) {
			return null;
		} else if (identifiable == this) {
			return this;
		} else if (Objects.equals(identifiable.getName(), getName())) {
			return this;
		}
		return null;
	}

}
