/*******************************************************************************
 * The variability artifact transformed is not supported by TraVarT.
 *
 * @author Kevin Feichtinger
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.exception;

@SuppressWarnings("serial")
public class NotSupportedVariabilityTypeException extends Exception {

	/**
	 * Creates a new parser exception with the given message.
	 *
	 * @param message a string.
	 */
	public NotSupportedVariabilityTypeException(final String message) {
		super(message);
	}

	/**
	 * Creates a new parser exception with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public NotSupportedVariabilityTypeException(final Exception e) {
		super(e);
	}
}
