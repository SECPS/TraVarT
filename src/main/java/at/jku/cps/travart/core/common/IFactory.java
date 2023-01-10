package at.jku.cps.travart.core.common;

/**
 *
 * This represents a base factory to create elements for the variability
 * artifact of type <T>.
 *
 * @author Kevin Feichtinger
 *
 * @param <T> The type of the variability artifact.
 */
public interface IFactory<T> {

	/**
	 * A unique ID to identify the factory.
	 *
	 * @return a unique ID of the factory.
	 */
	String getId();

	/**
	 * Creates an empty variability model of type <T>.
	 *
	 * @return an empty variability model.
	 */
	T create();
}
