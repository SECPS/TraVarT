package at.jku.cps.travart.ovm.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModelElement;
import at.jku.cps.travart.ovm.model.IOvModelVariationBase;
import at.jku.cps.travart.ovm.model.IOvModelVariationPoint;
import at.jku.cps.travart.ovm.transformation.DefaultOvModelTransformationProperties;
import de.ovgu.featureide.fm.core.functional.Functional;

/**
 * Represents a concrete implementation of an {@link IOvModelVariationPoint}.
 *
 * @see IOvModelVariationPoint
 *
 * @author johannstoebich
 */
public class OvModelVariationPoint extends OvModelVariationBase implements IOvModelVariationPoint {

	public static final int EMPTY_VALUE = -1;

	protected boolean alternative;

	protected int minChoices;

	protected int maxChoices;

	protected final List<IOvModelVariationBase> mandatoryChildren = new ArrayList<>();

	protected final List<IOvModelVariationBase> optionalChildren = new ArrayList<>();

	public OvModelVariationPoint() {
		minChoices = EMPTY_VALUE;
		maxChoices = EMPTY_VALUE;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#isAlternative()
	 */
	@Override
	public boolean isAlternative() {
		return alternative;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#setAlternative(boolean)
	 */
	@Override
	public void setAlternative(final boolean alternative) {
		this.alternative = alternative;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#hasMinChoices()
	 */
	@Override
	public boolean hasMinChoices() {
		return minChoices != EMPTY_VALUE;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#getMinChoices()
	 */
	@Override
	public int getMinChoices() {
		return minChoices;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#setMinChoices(int)
	 */
	@Override
	public void setMinChoices(final int minChoices) {
		this.minChoices = minChoices;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#hasMaxChoices()
	 */
	@Override
	public boolean hasMaxChoices() {
		return maxChoices != EMPTY_VALUE;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#getMaxChoices()
	 */
	@Override
	public int getMaxChoices() {
		return maxChoices;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#setMaxChoices(int)
	 */
	@Override
	public void setMaxChoices(final int maxChoices) {
		this.maxChoices = maxChoices;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#hasMandatoryChildren()
	 */
	@Override
	public boolean hasMandatoryChildren() {
		return mandatoryChildren.size() > 0;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#getMandatoryChildrenCount()
	 */
	@Override
	public int getMandatoryChildrenCount() {
		return mandatoryChildren.size();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#getMandatoryChildren()
	 */
	@Override
	public List<IOvModelVariationBase> getMandatoryChildren() {
		return Collections.unmodifiableList(mandatoryChildren);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#addMandatoryChild(de.ovgu.featureide.core.ovm.model.IOvModelVariationBase)
	 */
	@Override
	public boolean addMandatoryChild(final IOvModelVariationBase mandatoryChild) {
		return mandatoryChildren.add(mandatoryChild);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#removeMandatoryChild(de.ovgu.featureide.core.ovm.model.IOvModelVariationBase)
	 */
	@Override
	public boolean removeMandatoryChild(final IOvModelVariationBase mandatoryChild) {
		return mandatoryChildren.remove(mandatoryChild);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#setMandatoryChildren(java.util.List)
	 */
	@Override
	public void setMandatoryChildren(final List<IOvModelVariationBase> mandatoryChildren) {
		this.mandatoryChildren.clear();
		this.mandatoryChildren.addAll(Functional.toList(mandatoryChildren));
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#hasOptionalChildren()
	 */
	@Override
	public boolean hasOptionalChildren() {
		return optionalChildren.size() > 0;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#getOptionalChildrenCount()
	 */
	@Override
	public int getOptionalChildrenCount() {
		return optionalChildren.size();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#getOptionalChildren()
	 */
	@Override
	public List<IOvModelVariationBase> getOptionalChildren() {
		return Collections.unmodifiableList(optionalChildren);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#addOptionalChild(de.ovgu.featureide.core.ovm.model.IOvModelVariationBase)
	 */
	@Override
	public boolean addOptionalChild(final IOvModelVariationBase optionalChild) {
		return optionalChildren.add(optionalChild);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#removeOptionalChild(de.ovgu.featureide.core.ovm.model.IOvModelVariationBase)
	 */
	@Override
	public boolean removeOptionalChild(final IOvModelVariationBase optionalChild) {
		return optionalChildren.remove(optionalChild);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint#setOptionalChildren(java.util.List)
	 */
	@Override
	public void setOptionalChildren(final List<IOvModelVariationBase> optionalChildren) {
		this.optionalChildren.clear();
		this.optionalChildren.addAll(Functional.toList(optionalChildren));
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see at.jku.cps.vmt.core.common.core.configuration.IConfigurable#setSelected(boolean)
	 */
	@Override
	public void setSelected(final boolean selected) {
		super.setSelected(selected);
		if (mandatoryChildren.size() == 1) {
			IOvModelVariationBase mandatoryChild = mandatoryChildren.get(0);
			String mandatoryChildName = mandatoryChild.getName();
			if (mandatoryChildName.contains(DefaultOvModelTransformationProperties.VARIANT_PREFIX)
					&& mandatoryChildName.endsWith(getName())) {
				mandatoryChild.setSelected(selected);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.configuration.IValidateInternal#isValid(boolean)
	 */
	@Override
	public boolean isValid(final boolean isMandatory) {
		boolean isValid = super.isValid(isMandatory);
		if (isValid && isSelected()) { // execute check only if the variation point is selected
			// Shortcut (do not check artificial variants)
			if (mandatoryChildren.size() == 1 && mandatoryChildren.get(0) != null
					&& mandatoryChildren.get(0).getName() != null
					&& (mandatoryChildren.get(0).getName().startsWith(DefaultOvModelTransformationProperties.VARIANT_PREFIX)
							|| mandatoryChildren.get(0).getName()
									.startsWith(DefaultOvModelTransformationProperties.CONSTRAINT_VARIATION_POINT_PREFIX
											+ DefaultOvModelTransformationProperties.VARIANT_PREFIX))
					&& mandatoryChildren.get(0).getName().endsWith(getName())) {
				return isValid;
			}
			// all mandatory children have to be checked
			for (final IOvModelVariationBase mandatoryChild : mandatoryChildren) {
				isValid = isValid && mandatoryChild.isSelected();
				if (!isValid) {
					return false;
				}
			}

			long mandatoryCount = mandatoryChildren.stream().filter(IOvModelVariationBase::isSelected).count();
			long optionalCount = optionalChildren.stream().filter(IOvModelVariationBase::isSelected).count();
			long selected = mandatoryCount + optionalCount;
			// check the ranges
			if (isAlternative()) {
				if (selected < minChoices || selected > maxChoices) {
					isValid = false;
				}
				isValid = isValid && minChoices != EMPTY_VALUE && maxChoices != EMPTY_VALUE;
			} else {
				isValid = isValid && (minChoices == EMPTY_VALUE && maxChoices == EMPTY_VALUE
						|| minChoices <= mandatoryCount && maxChoices <= selected);
			}
			if (!isValid) {
				return false;
			}

			// check mandatory children
			for (final IOvModelVariationBase mandatoryChild : mandatoryChildren) {
				isValid = isValid && mandatoryChild.isValid(true);
				if (!isValid) {
					return false;
				}
			}

			// check optional children
			for (final IOvModelVariationBase optionalChild : optionalChildren) {
				isValid = isValid && optionalChild.isValid(false);
				if (!isValid) {
					return false;
				}
			}

			isValid = isValid && optionalChildren.size() + mandatoryChildren.size() > 0;
		} else if (isValid) {
			// Shortcut (do not check artificial variants)
			if (mandatoryChildren.size() == 1 && mandatoryChildren.get(0) != null
					&& mandatoryChildren.get(0).getName() != null
					&& (mandatoryChildren.get(0).getName().startsWith(DefaultOvModelTransformationProperties.VARIANT_PREFIX)
							|| mandatoryChildren.get(0).getName()
									.startsWith(DefaultOvModelTransformationProperties.CONSTRAINT_VARIATION_POINT_PREFIX
											+ DefaultOvModelTransformationProperties.VARIANT_PREFIX))
					&& mandatoryChildren.get(0).getName().endsWith(getName())) {
				return isValid;
			}
			if (getName().contains(DefaultOvModelTransformationProperties.CONSTRAINT_VARIATION_POINT_PREFIX)) {
				return isValid;
			}
			// all mandatory children have to be checked
			for (final IOvModelVariationBase mandatoryChild : mandatoryChildren) {
				isValid = isValid && !mandatoryChild.isSelected();
				if (!isValid) {
					return false;
				}
			}
			for (final IOvModelVariationBase optionalChild : optionalChildren) {
				isValid = isValid && !optionalChild.isSelected();
				if (!isValid) {
					return false;
				}
			}
		}
		return isValid;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModelElement#getElement(de.ovgu.featureide.core.ovm.model.IIdentifiable)
	 */
	@Override
	public IOvModelElement getElement(final IIdentifiable identifiable) {
		IOvModelElement element = super.getElement(identifiable);
		if (element != null) {
			return element;
		}

		for (final IOvModelVariationBase mandatoryChild : mandatoryChildren) {
			element = mandatoryChild.getElement(identifiable);
			if (element != null) {
				return element;
			}
		}

		for (final IOvModelVariationBase optionalChild : optionalChildren) {
			element = optionalChild.getElement(identifiable);
			if (element != null) {
				return element;
			}
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
		result = prime * result + (alternative ? 1231 : 1237);
		result = prime * result + (mandatoryChildren == null ? 0 : mandatoryChildren.hashCode());
		result = prime * result + maxChoices;
		result = prime * result + minChoices;
		result = prime * result + (optionalChildren == null ? 0 : optionalChildren.hashCode());
		return result;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OvModelVariationPoint other = (OvModelVariationPoint) obj;
		if (alternative != other.alternative) {
			return false;
		}
		if (mandatoryChildren == null) {
			if (other.mandatoryChildren != null) {
				return false;
			}
		} else if (!mandatoryChildren.equals(other.mandatoryChildren)) {
			return false;
		}
		if (maxChoices != other.maxChoices) {
			return false;
		}
		if (minChoices != other.minChoices) {
			return false;
		}
		if (optionalChildren == null) {
			if (other.optionalChildren != null) {
				return false;
			}
		} else if (!optionalChildren.equals(other.optionalChildren)) {
			return false;
		}
		return true;
	}

}
