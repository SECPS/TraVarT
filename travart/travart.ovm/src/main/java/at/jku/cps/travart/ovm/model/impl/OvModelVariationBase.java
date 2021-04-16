package at.jku.cps.travart.ovm.model.impl;

import at.jku.cps.travart.ovm.model.IOvModelVariationBase;
import at.jku.cps.travart.ovm.model.IOvModelVariationBaseMetainformation;

/**
 * Represents a concrete implementation of an {@link IOvModelVariationBase}.
 *
 * @see IOvModelVariationBase
 *
 * @author johannstoebich
 */
public abstract class OvModelVariationBase extends OvModelElement implements IOvModelVariationBase {

	protected IOvModelVariationBaseMetainformation metainformation;
	protected boolean optional;
	private boolean selected;

	public OvModelVariationBase() {
		super();
		metainformation = new OvModelVariationBaseMetainformation();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBase#getMetaInformation()
	 */
	@Override
	public IOvModelVariationBaseMetainformation getMetainformation() {
		return metainformation;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBase#setMetaInformation(IOvModelVariationBaseMetainformation)
	 */
	@Override
	public void setMetaInformation(IOvModelVariationBaseMetainformation metaInformation) {
		metainformation = metaInformation;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBase#isOptional()
	 */
	@Override
	public boolean isOptional() {
		return optional;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBase#setOptional(boolean)
	 */
	@Override
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see at.jku.cps.vmt.core.common.core.configuration.IConfigurable#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see at.jku.cps.vmt.core.common.core.configuration.IConfigurable#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.configuration.IValidateInternal#isValid(boolean)
	 */
	@Override
	public boolean isValid(boolean isMandatory) {
		return !isMandatory || isSelected();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (optional ? 1231 : 1237);
		result = prime * result + (metainformation == null ? 0 : metainformation.hashCode());
		return result;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OvModelVariationBase other = (OvModelVariationBase) obj;
		if (optional != other.optional) {
			return false;
		}
		if (metainformation == null) {
			if (other.metainformation != null) {
				return false;
			}
		} else if (!metainformation.equals(other.metainformation)) {
			return false;
		}
		return true;
	}

}
