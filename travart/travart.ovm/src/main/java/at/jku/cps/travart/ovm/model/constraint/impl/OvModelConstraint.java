package at.jku.cps.travart.ovm.model.constraint.impl;

import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModelElement;
import at.jku.cps.travart.ovm.model.IOvModelVariationBase;
import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraintMetainformation;
import at.jku.cps.travart.ovm.model.impl.OvModelElement;

/**
 * Represents a concrete implementation of an {@link IOvModelConstraint}.
 *
 * @see IOvModelConstraint
 *
 * @author johannstoebich
 */
public abstract class OvModelConstraint extends OvModelElement implements IOvModelConstraint {

	protected IOvModelConstraintMetainformation metanformation;

	protected IOvModelVariationBase source;

	protected IOvModelVariationBase target;

	public OvModelConstraint() {
		metanformation = new OvModelConstraintMetainformation();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraint#getSource()
	 */
	@Override
	public IOvModelVariationBase getSource() {
		return source;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraint#setSource(IOvModelVariationBase)
	 */
	@Override
	public void setSource(IOvModelVariationBase source) {
		this.source = source;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraint#getTarget()
	 */
	@Override
	public IOvModelVariationBase getTarget() {
		return target;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraint#setTarget(IOvModelVariationBase)
	 */
	@Override
	public void setTarget(IOvModelVariationBase target) {
		this.target = target;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.configuration.IValidate#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean isValid = true;
		isValid = isValid && source != null && target != null;
		return isValid;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraint#getMetainformation()
	 */
	@Override
	public IOvModelConstraintMetainformation getMetainformation() {
		return metanformation;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.constraint.IOvModelConstraint#getElement(de.ovgu.featureide.core.ovm.model.IIdentifiable)
	 */
	@Override
	public IOvModelElement getElement(IIdentifiable identifiable) {
		IOvModelElement element;
		if ((element = super.getElement(identifiable)) != null) {
			return element;
		} else if (getSource() != null && (element = getSource().getElement(identifiable)) != null) {
			return element;
		} else if (getTarget() != null && (element = getTarget().getElement(identifiable)) != null) {
			return element;
		}
		return null;
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
		result = prime * result + (metanformation == null ? 0 : metanformation.hashCode());
		result = prime * result + (source == null ? 0 : source.hashCode());
		result = prime * result + (target == null ? 0 : target.hashCode());
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
		final OvModelConstraint other = (OvModelConstraint) obj;
		if (metanformation == null) {
			if (other.metanformation != null) {
				return false;
			}
		} else if (!metanformation.equals(other.metanformation)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (target == null) {
			if (other.target != null) {
				return false;
			}
		} else if (!target.equals(other.target)) {
			return false;
		}
		return true;
	}
}
