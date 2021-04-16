package at.jku.cps.travart.ovm.model.impl;

import java.util.ArrayList;
import java.util.List;

import at.jku.cps.travart.ovm.model.IOvModelVariationBaseMetainformation;
import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraint;
import de.ovgu.featureide.fm.core.base.IPropertyContainer;
import de.ovgu.featureide.fm.core.base.impl.MapPropertyContainer;

/**
 * Represents a concrete implementation of an
 * {@link IOvModelVariationBaseMetainformation}.
 *
 * @see IOvModelVariationBaseMetainformation
 *
 * @author johannstoebich
 */
public class OvModelVariationBaseMetainformation implements IOvModelVariationBaseMetainformation {

	protected boolean isAbstract;
	protected boolean hidden;
	protected boolean partOfOvModelRoot;
	protected String description;
	protected List<IOvModelConstraint> referencedConstraints;
	protected IPropertyContainer customProperties;

	public OvModelVariationBaseMetainformation() {
		referencedConstraints = new ArrayList<IOvModelConstraint>();
		customProperties = new MapPropertyContainer();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#getCustomProperties()
	 */
	@Override
	public IPropertyContainer getCustomProperties() {
		return customProperties;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#setDescription(java.lang.CharSequence)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#isAbstract()
	 */
	@Override
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#setAbstract(boolean)
	 */
	@Override
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#isHidden()
	 */
	@Override
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#setHidden(boolean)
	 */
	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#isPartOfOvModelRoot()
	 */
	@Override
	public boolean isPartOfOvModelRoot() {
		return partOfOvModelRoot;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#setPartOfOvModelRoot(boolean)
	 */
	@Override
	public void setPartOfOvModelRoot(boolean partOfOvModelRoot) {
		this.partOfOvModelRoot = partOfOvModelRoot;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#getReferencedConstraints()
	 */
	@Override
	public List<IOvModelConstraint> getReferencedConstraints() {
		return referencedConstraints;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationBaseMetainformation#setReferencedConstraints(java.util.List)
	 */
	@Override
	public void setReferencedConstraints(List<IOvModelConstraint> referencedConstraints) {
		this.referencedConstraints = referencedConstraints;
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
		result = prime * result + (hidden ? 1231 : 1237);
		result = prime * result + (isAbstract ? 1231 : 1237);
		result = prime * result + (partOfOvModelRoot ? 1231 : 1237);
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
		final OvModelVariationBaseMetainformation other = (OvModelVariationBaseMetainformation) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (hidden != other.hidden) {
			return false;
		}
		if (isAbstract != other.isAbstract) {
			return false;
		}
		if (partOfOvModelRoot != other.partOfOvModelRoot) {
			return false;
		}
		return true;
	}

}
