package at.jku.cps.travart.ovm.model;

import java.util.List;

import at.jku.cps.travart.core.common.IValidate;
import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraint;

/**
 *
 * This class represents an OVM as it is defined by K. Pohl, G. Böckle, and F.
 * Linden in Software Product Line Engineering: Foundations, Principles, and
 * Techniques in 2005. It stores all referenced constraint and variation points.
 *
 * @author johannstoebich
 *
 */
public interface IOvModel extends IIdentifiable, IValidate {

	/**
	 * Returns the factoryId which created this {@link IOvModel}.
	 *
	 * @return the factoryId
	 */
	String getFactoryId();

	/**
	 * Returns the file location where the model is stored.
	 *
	 * @return the file location.
	 */
	String getSourceFile();

	/**
	 * Update the storage location of this {@link IOvModel}.
	 *
	 * @param sourceFile the location of this {@link IOvModel}.
	 */
	void setSourceFile(String sourceFile);

	/**
	 * Returns the number of variation points stored in this {@link IOvModel}.
	 *
	 * @return number of variation points in this model
	 */
	int getNumberOfVariationPoints();

	/**
	 * This method returns all variation points stored in this {@link IOvModel}.
	 *
	 * @return the list of variation points.
	 */
	List<IOvModelVariationPoint> getVariationPoints();

	/**
	 * This method adds a variation point to an {@link IOvModel}.
	 *
	 * @param variationPoint the variation point which is added.
	 */
	boolean addVariationPoint(IOvModelVariationPoint variationPoint);

	/**
	 * This method removes a variation point from OvModel. The variation point is
	 * given. The equals method is used to determine the correct variation point to
	 * remove.
	 *
	 * @param variationPoint the variation point which should be removed.
	 */
	boolean removeVariationPoint(IOvModelVariationPoint variationPoint);

	/**
	 * This method returns the number of constraints contained in this
	 * {@link IOvModel}.
	 *
	 * @return the number of constraints contained in this {@link IOvModel}.
	 *
	 */
	int getConstraintCount();

	/**
	 * This method adds a constraint to an {@link IOvModel}. A constraint represents
	 * a restriction on the model.
	 *
	 * @param constraint the constraint which is added.
	 */
	boolean addConstraint(IOvModelConstraint constraint);

	/**
	 * This method adds a constraint to an {@link IOvModel}. A constraint represents
	 * a restriction on the model. The constraint is added at a specific index where
	 * all later occurenecs are shifted to re right.
	 *
	 * @param constraint the constraint which is added.
	 * @param index      the index where the constraint is added.
	 */
	void addConstraint(IOvModelConstraint constraint, int index);

	/**
	 * Returns the index of the first occurrence of <code>constraint</code> in the
	 * collection of constraints, or <b>-1</b> if <code>constraint</code> is not
	 * contained. <br>
	 * <br>
	 * <b>Note</b>:
	 *
	 * @param constraint the element to be removed. It is assumed that this
	 *                   parameter is <i>non-null</i>
	 * @throws NullPointerException - if <code>constraint</code> is null (optional)
	 *
	 * @return the index of the first occurrence of <code>constraint</code> in the
	 *         collection of constraints, or <b>-1</b> otherwise.
	 */
	int getConstraintIndex(IOvModelConstraint constraint);

	/**
	 * Returns the list of constraints stored in this {@link IOvModel}. <br>
	 * <br>
	 * <b>Note</b>: The returned list should be <b>unmodifiable</b> to avoid
	 * external access to internal data
	 *
	 * @return All constraints stored in this {@link IOvModel}.
	 */
	List<IOvModelConstraint> getConstraints();

	/**
	 * Returns the model properties attached to this {@link IOvModel}. These
	 * properties contain at least comments and properties. The properties returned
	 * by this model is implementation specific and might contain additional
	 * properties.
	 *
	 * @return OvModel properties
	 */
	IOvModelMetainformation getMetainformation();

	/**
	 * Removes the first occurrence of <code>constraint</code> from the collection
	 * of constraints in this {@link IOvModel}, if it is present. Otherwise there is
	 * no effect to this {@link IOvModel}.
	 *
	 * @param constraint The constraint to be removed
	 */
	boolean removeConstraint(IOvModelConstraint constraint);

	/**
	 * Removes the constraint at the specified position <code>index</code> in this
	 * collection of constraints in this {@link IOvModel}. When a constraint was
	 * removed, the remaining constraints to the right are shifted one position to
	 * the left.
	 *
	 * @param index position of the constraint to be removed
	 *
	 * @throws IndexOutOfBoundsException If the index is out of range
	 */
	void removeConstraint(int index);

	/**
	 * Replaces the constraint <code>constraint</code> at the specified position
	 * <code>index</code> in the collection of constraints of this {@link IOvModel}.
	 *
	 * @param constraint constraint which should be stored at <code>index</code>
	 * @param index      position for replacement
	 *
	 * @throws NullPointerException      if <code>constraint</code> is <b>null</b>
	 * @throws IndexOutOfBoundsException if the index is out of range
	 *
	 */
	void replaceConstraint(IOvModelConstraint constraint, int index);

	/**
	 * Sets the collections of constraints to the ones yielded by
	 * <code>constraints</code>. Existing constraint in the collection will be
	 * removed before this operation.
	 *
	 * @param constraints Source of constraints which should be copied into this
	 *                    {@link IOvModel}.
	 */
	void setConstraints(final Iterable<IOvModelConstraint> constraints);

	/**
	 * This method searches for an element identified by an identifiable. If the
	 * name of the identifiable matches the constraint's, variation point's or
	 * variant's name it is returned.
	 *
	 * @param identifiable the identifiable which should be found.
	 * @return the element which should be found, otherwise <code>null</code>.
	 */
	IOvModelElement getElement(IIdentifiable identifiable);

	/**
	 * This method finalizes the selection so that all virtual points will be set
	 * correctly. It should be called every time the selection has changed.
	 */
	void afterSelection();
}
