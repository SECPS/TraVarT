/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 * 	   @author Johann Stoebich
 *
 * The base interface for a configurable.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

/**
 * This represents a configurable item of an variablilty model. An item
 * implementing this interface can be selected.
 *
 * @author Johann Stoebich
 * @author Kevin Feichtinger
 */
public interface IConfigurable {
	/**
	 * Returns whether the feature is selected or not.
	 *
	 * @return whether the feature is selected or not.
	 */
	boolean isSelected();

	/**
	 * De-/Selects the feature.
	 *
	 * @param selected De-/Selects the configurable.
	 */
	void setSelected(boolean selected);

	/**
	 * Returns the name of an IConfigurable of a variability model
	 *
	 * @return the name of the identifiable.
	 */
	String getName();
}
