package at.jku.cps.travart.ovm.model;

/**
 * This class represents an element. Elements are constraints, variants or
 * variation points.
 *
 * @author johannstoebich
 */
public interface IOvModelElement extends IIdentifiable {

	/**
	 * This method searches for an element identified by an {@link IIdentifiable}.
	 * The constraint, variation point or variant which matches the idenfiable's
	 * name is returned. Each element's name is unique for all elements in an
	 * {@link IOvModel}.
	 *
	 * @param identifiable the identifiable which should be found.
	 * @return the element which should be found, otherwise <code>null</code>.
	 */
	IOvModelElement getElement(IIdentifiable identifiable);

}
