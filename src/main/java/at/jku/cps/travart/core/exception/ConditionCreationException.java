/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Represents an exception when creating a condition.
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
