package at.jku.cps.travart.ovm.model;

/**
 * An identifiable represents a basic interface for all OvModel elements which
 * must be uniquely identified. Currently the name must be unique and therefore
 * each element can be identified by a name. However this might change in
 * future. Therefore an internalId is provided.
 *
 *
 * @author johannstoebich
 */
public interface IIdentifiable {

	/**
	 * Returns the internal id of an identifiable.
	 *
	 * @return
	 */
	long getInternalId();

	String getName();

	/**
	 * This method sets the name of an IIdentifiable. The name must be unique within
	 * an {@link IOvModel}.
	 *
	 * @param name the name.
	 */
	void setName(String name);

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

}
