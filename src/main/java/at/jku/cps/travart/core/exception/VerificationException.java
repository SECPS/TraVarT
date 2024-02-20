/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Dario Romano
 *
 * Implements various utilities to verify transformation/optimization results
 *
 * Copyright 2024 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.exception;

public class VerificationException extends Exception {
	/**
	 * Creates a VerificationException with the given message.
	 *
	 * @param message a string.
	 */
	public VerificationException(final String message) {
		super(message);
	}

}
