package at.jku.cps.travart.ovm.model.impl;

import at.jku.cps.travart.ovm.model.IOvModelMetainformation;
import de.ovgu.featureide.fm.core.base.IPropertyContainer;
import de.ovgu.featureide.fm.core.base.impl.MapPropertyContainer;

/**
 * Represents a concrete implementation of an {@link IOvModelMetainformation}.
 *
 * @see IOvModelMetainformation
 *
 * @author johannstoebich
 */
public class OvModelMetainformation implements IOvModelMetainformation {

	protected IPropertyContainer customProperties;

	protected String description;

	public OvModelMetainformation() {
		customProperties = new MapPropertyContainer();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelMetainformation#getCustomProperties()
	 */
	@Override
	public IPropertyContainer getCustomProperties() {
		return customProperties;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelMetainformation#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelMetainformation#setDescription(String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
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
		final OvModelMetainformation other = (OvModelMetainformation) obj;
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
