/*******************************************************************************
 * A condition in an variability artifact could not be created.
 *
 * @author Kevin Feichtinger
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.exception;

/**
 * A {@code ConditionCreationException} indicates that an error occurred during
 * creating a condition.
 *
 * @author Kevin Feichtinger
 */
@SuppressWarnings("serial")
public class ConditionCreationException extends Exception {

	/**
	 * Creates a ConditionCreationException with the given message.
	 *
	 * @param message a string.
	 */
	public ConditionCreationException(final String message) {
		super(message);
	}

	/**
	 * Creates a ConditionCreationException with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public ConditionCreationException(final Exception e) {
		super(e);
	}

}
