package at.jku.cps.travart.ovm.model.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModel;
import at.jku.cps.travart.ovm.model.IOvModelElement;
import at.jku.cps.travart.ovm.model.IOvModelMetainformation;
import at.jku.cps.travart.ovm.model.IOvModelVariant;
import at.jku.cps.travart.ovm.model.IOvModelVariationBase;
import at.jku.cps.travart.ovm.model.IOvModelVariationPoint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelExcludesConstraint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelRequiresConstraint;
import at.jku.cps.travart.ovm.transformation.DefaultOvModelTransformationProperties;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.functional.Functional;

/**
 * Legal Notice: Some of this code or comments are overtaken from the
 * FeatrueIDE's {@link FeatureModel}.
 *
 * Represents a concrete implementation of an {@link IOvModel}.
 *
 * @see IOvModel
 *
 * @author johannstoebich
 */
public class OvModel extends Identifiable implements IOvModel {

	protected final String factoryId;

	protected String sourceFile;

	protected final IOvModelMetainformation metainformation;

	protected final List<IOvModelVariationPoint> variationPoints = new ArrayList<>();

	protected final List<IOvModelConstraint> constraints = new ArrayList<>();

	public OvModel(final String factoryId) {
		super();
		this.factoryId = factoryId;
		metainformation = new OvModelMetainformation();
	}

	public OvModel(final String factoryId, final long id) {
		super(id);
		this.factoryId = factoryId;
		metainformation = new OvModelMetainformation();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#addConstraint(de.ovgu.featureide.core.ovm.model.base.IOvmConstraint)
	 */
	@Override
	public boolean addConstraint(final IOvModelConstraint constraint) {
		return constraints.add(constraint);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#addConstraint(de.ovgu.featureide.core.ovm.model.base.IOvmConstraint,
	 *      int)
	 */
	@Override
	public void addConstraint(final IOvModelConstraint constraint, final int index) {
		constraints.add(index, constraint);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#addVariationPoint(de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint)
	 */
	@Override
	public boolean addVariationPoint(final IOvModelVariationPoint variationPoint) {
		return variationPoints.add(variationPoint);
	}

	private enum CanAssume {
		ASSUME_SELECTED_TRUE, ASSUME_SELECTED_FALSE, CANT_ASSUME;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#afterSelection()
	 */
	@Override
	public void afterSelection() {
		final List<IOvModelVariationBase> configurables = getIConfigurable(this);
		Map<IOvModelVariationBase, Boolean> optionalMap = getOptionalMap(this);
		Map<IOvModelVariationBase, CanAssume> store = new HashMap<>();
		for (IOvModelVariationBase vb : configurables) {
			if (!isVirtual(vb)) {
				store.put(vb, vb.isSelected() ? CanAssume.ASSUME_SELECTED_TRUE : CanAssume.ASSUME_SELECTED_FALSE);
				continue;
			} else if (optionalMap.containsKey(vb) && optionalMap.get(vb)
					|| !optionalMap.containsKey(vb) && vb.isOptional()) {
				store.put(vb, CanAssume.CANT_ASSUME);
				continue;
			}
			if (vb instanceof IOvModelVariant) {
				store.put(vb, optionalMap.get(vb) ? CanAssume.ASSUME_SELECTED_TRUE : CanAssume.CANT_ASSUME);
			}
			if (vb instanceof IOvModelVariationPoint) {
				IOvModelVariationPoint vp = (IOvModelVariationPoint) vb;
				boolean canAssumeTrue = true;
				for (IOvModelVariationBase mandatoryChild : vp.getMandatoryChildren()) {
					CanAssume stored = store.get(mandatoryChild);
					// must be already part of the store as bottom-up approach
					assert stored != null;
					canAssumeTrue = canAssumeTrue && stored == CanAssume.ASSUME_SELECTED_TRUE;
				}
				store.put(vp, canAssumeTrue ? CanAssume.ASSUME_SELECTED_TRUE : CanAssume.CANT_ASSUME);
			}
		}
		for (IOvModelConstraint constraint : getConstraints()) {
			if (constraint instanceof IOvModelExcludesConstraint) {
//					s: FALSE -> don't care
//					s: True -> false
				if (!isVirtual(constraint.getSource()) && isVirtual(constraint.getTarget())) {
					CanAssume canAssume = store.get(constraint.getTarget());
					CanAssume newAssume = constraint.getSource().isSelected() ? CanAssume.ASSUME_SELECTED_FALSE
							: CanAssume.CANT_ASSUME;

					if (canAssume == CanAssume.ASSUME_SELECTED_TRUE && newAssume == CanAssume.ASSUME_SELECTED_FALSE) {
						return;
					}
					store.put(constraint.getTarget(), newAssume);
				} else if (isVirtual(constraint.getSource()) && !isVirtual(constraint.getTarget())) {
					CanAssume canAssume = store.get(constraint.getSource());
					CanAssume newAssume = constraint.getTarget().isSelected() ? CanAssume.ASSUME_SELECTED_FALSE
							: CanAssume.CANT_ASSUME;

					if (canAssume == CanAssume.ASSUME_SELECTED_TRUE && newAssume == CanAssume.ASSUME_SELECTED_FALSE) {
						return;
					}
					store.put(constraint.getSource(), newAssume);
				}
			} else if (constraint instanceof IOvModelRequiresConstraint) {
//					s: TRUE -> True
//					s: FALSE -> don't care
				if (!isVirtual(constraint.getSource()) && isVirtual(constraint.getTarget())) {
					CanAssume canAssume = store.get(constraint.getTarget());
					CanAssume newAssume = constraint.getSource().isSelected() ? CanAssume.ASSUME_SELECTED_TRUE
							: CanAssume.CANT_ASSUME;

					if (canAssume == CanAssume.ASSUME_SELECTED_FALSE && newAssume == CanAssume.ASSUME_SELECTED_TRUE) {
						return;
					}
					store.put(constraint.getTarget(), newAssume);
				} else if (isVirtual(constraint.getSource()) && !isVirtual(constraint.getTarget())) {
					CanAssume canAssume = store.get(constraint.getSource());
					CanAssume newAssume = constraint.getTarget().isSelected() ? CanAssume.ASSUME_SELECTED_TRUE
							: CanAssume.CANT_ASSUME;

					if (canAssume == CanAssume.ASSUME_SELECTED_FALSE && newAssume == CanAssume.ASSUME_SELECTED_TRUE) {
						return;
					}
					store.put(constraint.getSource(), newAssume);
				}
			}
		}
		for (IOvModelVariationPoint vp : getVariationPoints()) {
			isPossibleToSelect(vp, store);
		}
		for (Entry<IOvModelVariationBase, CanAssume> entry : store.entrySet()) {
			if (entry.getValue() == CanAssume.ASSUME_SELECTED_TRUE) {
				entry.getKey().setSelected(true);
			}
			if (entry.getValue() == CanAssume.ASSUME_SELECTED_FALSE) {
				entry.getKey().setSelected(false);
			}
		}
	}

	private void isPossibleToSelect(final IOvModelVariationBase vb, final Map<IOvModelVariationBase, CanAssume> store) {
		if (vb instanceof IOvModelVariationPoint) {
			IOvModelVariationPoint vp = (IOvModelVariationPoint) vb;
			vp.getMandatoryChildren().forEach(child -> isPossibleToSelect(child, store));
			vp.getOptionalChildren().forEach(child -> isPossibleToSelect(child, store));
		}
		if (vb instanceof IOvModelVariationPoint && store.get(vb) == CanAssume.CANT_ASSUME) {
			IOvModelVariationPoint vp = (IOvModelVariationPoint) vb;
			List<IOvModelVariationBase> cantDecideMandatoryVbs = getVariationBaseElements(vp.getMandatoryChildren(),
					store, CanAssume.CANT_ASSUME);
			List<IOvModelVariationBase> selectedMandatoryVbs = getVariationBaseElements(vp.getMandatoryChildren(),
					store, CanAssume.ASSUME_SELECTED_TRUE);
			List<IOvModelVariationBase> selectedOptionalVbs = getVariationBaseElements(vp.getOptionalChildren(), store,
					CanAssume.ASSUME_SELECTED_TRUE);
			List<IOvModelVariationBase> cantDecideOptionalVbs = getVariationBaseElements(vp.getOptionalChildren(),
					store, CanAssume.CANT_ASSUME);
			long mandatoryCount = selectedMandatoryVbs.size() + cantDecideMandatoryVbs.size();
			if (vp.isAlternative()) {
				if (mandatoryCount + selectedOptionalVbs.size() > vp.getMaxChoices()) {
					store.put(vp, CanAssume.ASSUME_SELECTED_FALSE);
					return;
				}
				if (mandatoryCount + selectedOptionalVbs.size() + cantDecideOptionalVbs.size() < vp.getMinChoices()) {
					store.put(vp, CanAssume.ASSUME_SELECTED_FALSE);
					return;
				}
				if (mandatoryCount + selectedOptionalVbs.size() < vp.getMinChoices()) {
					int decided = 0;
					while (mandatoryCount + selectedOptionalVbs.size() + decided < vp.getMinChoices()) {
						store.put(cantDecideOptionalVbs.get(decided), CanAssume.ASSUME_SELECTED_TRUE);
						decided++;
					}
				}
			} else { // potential optional check for vp?
				if (mandatoryCount + selectedOptionalVbs.size() + cantDecideOptionalVbs.size() == 0) {
					store.put(vp, CanAssume.ASSUME_SELECTED_FALSE);
					return;
				}
				if (mandatoryCount + selectedOptionalVbs.size() == 0) {
					store.put(cantDecideOptionalVbs.get(0), CanAssume.ASSUME_SELECTED_TRUE);
				}
			}
		} else if (vb instanceof IOvModelVariationPoint && store.get(vb) == CanAssume.ASSUME_SELECTED_TRUE) {
			IOvModelVariationPoint vp = (IOvModelVariationPoint) vb;
			List<IOvModelVariationBase> cantDecideMandatoryVbs = getVariationBaseElements(vp.getMandatoryChildren(),
					store, CanAssume.CANT_ASSUME);
			List<IOvModelVariationBase> selectedMandatoryVbs = getVariationBaseElements(vp.getMandatoryChildren(),
					store, CanAssume.ASSUME_SELECTED_TRUE);
			List<IOvModelVariationBase> selectedOptionalVbs = getVariationBaseElements(vp.getOptionalChildren(), store,
					CanAssume.ASSUME_SELECTED_TRUE);
			List<IOvModelVariationBase> cantDecideOptionalVbs = getVariationBaseElements(vp.getOptionalChildren(),
					store, CanAssume.CANT_ASSUME);
			long mandatoryCount = selectedMandatoryVbs.size() + cantDecideMandatoryVbs.size();
			if (vp.isAlternative()) {
				if (mandatoryCount + selectedOptionalVbs.size() > vp.getMaxChoices()) {
					return;
				}
				if (mandatoryCount + selectedOptionalVbs.size() + cantDecideOptionalVbs.size() < vp.getMinChoices()) {
					return;
				}
				if (mandatoryCount + selectedOptionalVbs.size() < vp.getMinChoices()) {
					int decided = 0;
					while (mandatoryCount + selectedOptionalVbs.size() + decided < vp.getMinChoices()) {
						store.put(cantDecideOptionalVbs.get(decided), CanAssume.ASSUME_SELECTED_TRUE);
						decided++;
					}
				}
			} else { // potential optional check for vp?
				if (mandatoryCount + selectedOptionalVbs.size() + cantDecideOptionalVbs.size() == 0) {
					return;
				}
				if (mandatoryCount + selectedOptionalVbs.size() == 0) {
					store.put(cantDecideOptionalVbs.get(0), CanAssume.ASSUME_SELECTED_TRUE);
				}
			}
		} else if (vb instanceof IOvModelVariationPoint && store.get(vb) == CanAssume.ASSUME_SELECTED_FALSE) {
			return;
		}
	}

	private List<IOvModelVariationBase> getVariationBaseElements(final List<? extends IOvModelVariationBase> vb,
			final Map<IOvModelVariationBase, CanAssume> store, final CanAssume enumValue) {
		return vb.stream().filter(child -> store.get(child) == enumValue).collect(Collectors.toList());
	}

	private boolean isVirtual(final IConfigurable configurable) {
		return configurable.getName().contains(DefaultOvModelTransformationProperties.CONSTRAINT_VARIATION_POINT_PREFIX);
	}

	private static Map<IOvModelVariationBase, Boolean> getOptionalMap(final IOvModel ovModel) {
		final Map<IOvModelVariationBase, Boolean> optionalMap = new HashMap<>();
		fillListOptional(ovModel.getVariationPoints(), optionalMap);
		for (IOvModelConstraint constraint : ovModel.getConstraints()) {
			fillListOptional(Arrays.asList(constraint.getSource()), optionalMap);
			fillListOptional(Arrays.asList(constraint.getTarget()), optionalMap);
		}
		return optionalMap;
	}

	private static void fillListOptional(final List<? extends IOvModelVariationBase> ovModelElements,
			final Map<IOvModelVariationBase, Boolean> optionalMap) {
		for (final IOvModelVariationBase ovModelVariationBase : ovModelElements) {
			if (ovModelVariationBase instanceof IOvModelVariationPoint) {
				((IOvModelVariationPoint) ovModelVariationBase).getMandatoryChildren().forEach(mandatoryChild -> {
					optionalMap.put(mandatoryChild, false);
				});
				((IOvModelVariationPoint) ovModelVariationBase).getOptionalChildren().forEach(optionalChild -> {
					optionalMap.put(optionalChild, true);
				});
				fillListOptional(((IOvModelVariationPoint) ovModelVariationBase).getMandatoryChildren(), optionalMap);
				fillListOptional(((IOvModelVariationPoint) ovModelVariationBase).getOptionalChildren(), optionalMap);
			}
		}
	}

	private static List<IOvModelVariationBase> getIConfigurable(final IOvModel ovModel) {
		final List<IOvModelVariationBase> configurables = new ArrayList<>();
		fillListConfigurable(ovModel.getVariationPoints(), configurables);
		for (IOvModelConstraint constraint : ovModel.getConstraints()) {
			fillListConfigurable(Arrays.asList(constraint.getSource()), configurables);
			fillListConfigurable(Arrays.asList(constraint.getTarget()), configurables);
		}
		return configurables;
	}

	private static void fillListConfigurable(final List<? extends IOvModelVariationBase> ovModelElements,
			final List<IOvModelVariationBase> configurables) {
		for (final IOvModelVariationBase ovModelVariationBase : ovModelElements) {

			if (ovModelVariationBase instanceof IOvModelVariationPoint) {
				fillListConfigurable(((IOvModelVariationPoint) ovModelVariationBase).getMandatoryChildren(),
						configurables);
				fillListConfigurable(((IOvModelVariationPoint) ovModelVariationBase).getOptionalChildren(),
						configurables);
			}

			if (!configurables.contains(ovModelVariationBase)) {
				configurables.add(ovModelVariationBase);
			}
		}
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
		final OvModel other = (OvModel) obj;
		if (constraints == null) {
			if (other.constraints != null) {
				return false;
			}
		} else if (!constraints.equals(other.constraints)) {
			return false;
		}
		if (metainformation == null) {
			if (other.metainformation != null) {
				return false;
			}
		} else if (!metainformation.equals(other.metainformation)) {
			return false;
		}
		if (variationPoints == null) {
			if (other.variationPoints != null) {
				return false;
			}
		} else if (!variationPoints.equals(other.variationPoints)) {
			return false;
		}
		return true;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#getConstraintCount()
	 */
	@Override
	public int getConstraintCount() {
		return constraints.size();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#getConstraintIndex(de.ovgu.featureide.core.ovm.model.base.IOvmConstraint)
	 */
	@Override
	public int getConstraintIndex(final IOvModelConstraint constraint) {
		return constraints.indexOf(constraint);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#getConstraints()
	 */
	@Override
	public List<IOvModelConstraint> getConstraints() {
		return Collections.unmodifiableList(constraints);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#getElement(de.ovgu.featureide.core.ovm.model.IIdentifiable)
	 */
	@Override
	public IOvModelElement getElement(final IIdentifiable identifiable) {
		IOvModelElement element;
		for (final IOvModelVariationPoint variationPoint : variationPoints) {
			element = variationPoint.getElement(identifiable);
			if (element != null) {
				return element;
			}
		}
		for (final IOvModelConstraint constraint : constraints) {
			element = constraint.getElement(identifiable);
			if (element != null) {
				return element;
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#getFactoryId()
	 */
	@Override
	public String getFactoryId() {
		return factoryId;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#getMetainformation()
	 */
	@Override
	public IOvModelMetainformation getMetainformation() {
		return metainformation;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#getNumberOfVariationPoints()
	 */
	@Override
	public int getNumberOfVariationPoints() {
		return variationPoints.size();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#getSourceFile()
	 */
	@Override
	public String getSourceFile() {
		return sourceFile;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#getVariationPoints()
	 */
	@Override
	public List<IOvModelVariationPoint> getVariationPoints() {
		return Collections.unmodifiableList(variationPoints);
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
		result = prime * result + (constraints == null ? 0 : constraints.hashCode());
		result = prime * result + (metainformation == null ? 0 : metainformation.hashCode());
		result = prime * result + (variationPoints == null ? 0 : variationPoints.hashCode());
		return result;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.configuration.IValidate#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean isValid = true;
		for (final IOvModelVariationPoint variationPoint : variationPoints) {
			isValid = isValid && variationPoint.isValid(!variationPoint.isOptional());
			if (!isValid) {
				return false;
			}
		}
		for (final IOvModelConstraint constraint : constraints) {
			isValid = isValid && constraint.isValid();
			if (!isValid) {
				return false;
			}
		}
		return isValid;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#removeConstraint(int)
	 */
	@Override
	public void removeConstraint(final int index) {
		constraints.remove(index);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#removeConstraint(de.ovgu.featureide.core.ovm.model.base.IOvmConstraint)
	 */
	@Override
	public boolean removeConstraint(final IOvModelConstraint constraint) {
		return constraints.remove(constraint);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#deleteVariationPoint(de.ovgu.featureide.core.ovm.model.IOvModelVariationPoint)
	 */
	@Override
	public boolean removeVariationPoint(final IOvModelVariationPoint variationPoint) {
		return variationPoints.remove(variationPoint);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#replaceConstraint(de.ovgu.featureide.core.ovm.model.base.IOvmConstraint,
	 *      int)
	 */
	@Override
	public void replaceConstraint(final IOvModelConstraint constraint, final int index) {
		if (constraint == null) {
			throw new NullPointerException();
		}
		constraints.remove(constraints.get(index));
		constraints.set(index, constraint);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.base.IOvmModel#setConstraints(java.lang.Iterable)
	 */
	@Override
	public void setConstraints(final Iterable<IOvModelConstraint> constraints) {
		this.constraints.clear();
		this.constraints.addAll(Functional.toList(constraints));
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IOvModel#setSourceFile(java.lang.String)
	 */
	@Override
	public void setSourceFile(final String sourceFile) {
		this.sourceFile = sourceFile;
	}
}
