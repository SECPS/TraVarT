package at.jku.cps.travart.ovm.model.impl;

import at.jku.cps.travart.ovm.model.IIdentifiable;

/**
 * Represents a concrete implementation of an {@link IIdentifiable}.
 *
 * @see IIdentifiable
 *
 * @author johannstoebich
 */
public abstract class Identifiable implements IIdentifiable {

	private static long NEXT_ID = 0;

	protected long internalId;
	protected String name;

	public Identifiable() {
		internalId = getNextId();
	}

	public Identifiable(long internalId) {
		this.internalId = internalId;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IIdentifiable#getInternalId()
	 */
	@Override
	public long getInternalId() {
		return internalId;
	}

	public void setInternalId(long internalId) {
		this.internalId = internalId;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IIdentifiable#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see de.ovgu.featureide.core.ovm.model.IIdentifiable#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	protected static final synchronized long getNextId() {
		return NEXT_ID++;
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
		result = prime * result + (name == null ? 0 : name.hashCode());
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
		final Identifiable other = (Identifiable) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
