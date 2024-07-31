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
 * implementing this interface can be configured.
 *
 * @author Johann Stoebich
 * @author Kevin Feichtinger
 */
public interface IConfigurable {
	/**
	 * Returns whether this object is configured.
	 * 
	 * This usually refers to the existence of some concrete value configuration.
	 * For the example of integer features (in the scope of FOP), the setting of any
	 * value is a configuration. The lack of a configuration occurs when the value
	 * field of the respective feature is not initialised, e.g., set to `null`.
	 *
	 * @return whether the object is configured or not
	 */
	boolean isConfigured();

	/**
	 * Configures this object.
	 * 
	 * What exactly "configure" means in this context, depends on the class
	 * implementing IConfigurable. For feature-like value objects, to "configure" an
	 * unconfigured object might just be setting the value field to a default
	 * reference value, e.g., 0. If the object is already configured, "configuring"
	 * the object again should not reset the configuration, but instead do nothing.
	 * For the re-configuration, use the getter/setter of the configuration-specific
	 * field, in case of a value object, the value field.
	 * 
	 * Unconfiguring a configured object should remove the configuration-specific
	 * values from the respective fields, usually by setting this fields to `null`.
	 *
	 * @param selected Toggle "configuredless" of this object
	 */
	void setConfigured(boolean selected);

	/**
	 * Returns the name of an IConfigurable of a variability model
	 *
	 * @return the name of the identifiable.
	 */
	String getName();
}
