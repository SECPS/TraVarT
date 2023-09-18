/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Johann Stoebich
 *
 * The base interface for validating a variability element.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

/**
 * This is an interface defines a method in order to validate the variability
 * representation implementing it.
 *
 * @author Johann Stoebich
 */
public interface IValidate {

	/**
	 * This method returns whether an configuration is valid or not.
	 *
	 * @return true whenever the current configuration is valid.
	 */
	boolean isValid();
}
