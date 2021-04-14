package at.jku.cps.travart.ovm.model.constraint.impl;

import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraintMetainformation;
import de.ovgu.featureide.fm.core.base.IPropertyContainer;
import de.ovgu.featureide.fm.core.base.impl.MapPropertyContainer;

/**
 * Represents a concrete implementation of an
 * {@link IOvModelConstraintMetainformation}.
 *
 * @see IOvModelConstraintMetainformation
 *
 * @author johannstoebich
 */
public class OvModelConstraintMetainformation implements IOvModelConstraintMetainformation {

	protected String description;
	protected IPropertyContainer customProperties;

	public OvModelConstraintMetainformation() {
		customProperties = new MapPropertyContainer();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraintMetainformation#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraintMetainformation#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelConstraintMetainformation#getCustomProperties()
	 */
	@Override
	public IPropertyContainer getCustomProperties() {
		return customProperties;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (description == null ? 0 : description.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OvModelConstraintMetainformation other = (OvModelConstraintMetainformation) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		return true;
	}

}
