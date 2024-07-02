/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *     @author Prankur Agarwal
 * 	   @author Jakob Gretenkort
 *
 * The base interface for a TraVarT plugin extension.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

import org.pf4j.ExtensionPoint;

/**
 * A TraVarT plugin must implement a variability language and provide access to
 * a {@link IModelTransformer} to transform models in that language.
 * Additionally, some meta-data of the plugin should be available.
 *
 * @author Prankur Agarwal
 * @author Kevin Feichtinger
 * @author Jakob Gretenkort
 */
public interface IPlugin<T> extends ExtensionPoint, ILanguage<T> {
	/**
	 * Returns the transformer of the plugin to transform the variability model.
	 *
	 * @return the transformer of the plugin to transform the variability model.
	 */
	IModelTransformer<T> getTransformer();

	/**
	 * Returns the version of the plugin.
	 *
	 * @return the version of the plugin.
	 */
	String getVersion();

	/**
	 * Returns a unique ID of the plugin, such that it can be identified.
	 *
	 * @return the unique ID of the plugin.
	 */
	String getId();
}
