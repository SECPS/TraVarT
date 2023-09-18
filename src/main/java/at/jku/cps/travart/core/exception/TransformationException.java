/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Represents an exception when transforming a variability artifact.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
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
