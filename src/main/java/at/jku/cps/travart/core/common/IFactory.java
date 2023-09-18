/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * The base interface for a factory.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

/**
 * This represents a base factory to create elements for the variability model
 * of type <T>.
 *
 * @param <T> The type of the variability model.
 * @author Kevin Feichtinger
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
