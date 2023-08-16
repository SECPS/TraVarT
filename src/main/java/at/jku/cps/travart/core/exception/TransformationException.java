/**
 * Defines a generic transformation exception, indicating something went wrong during transformation.
 *
 * @author Kevin Feichtinger
*
* Copyright 2023 Johannes Kepler University Linz
* LIT Cyber-Physical Systems Lab
* All rights reserved
*/
package at.jku.cps.travart.core.exception;

@SuppressWarnings("serial")
public class TransformationException extends Exception {

	/**
	 * Creates a new exception with the given message.
	 *
	 * @param message a string.
	 */
	public TransformationException(final String message) {
		super(message);
	}

	/**
	 * Creates a new exception with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public TransformationException(final Exception e) {
		super(e);
	}
}
