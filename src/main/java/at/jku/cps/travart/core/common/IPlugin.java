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

import at.jku.cps.travart.core.basic.DefaultPrettyPrinter;

/**
 * A TraVarT plugin extension must provide access to a {@link IModelTransformer}
 * to transform a variability model, {@link IReader}/{@link IWriter} to
 * read/write the variability model and {@link IStatistics} to get the
 * statistics of the variability model. Additionally, some meta-data of the
 * plugin should be available.
 *
 * @author Prankur Agarwal
 * @author Kevin Feichtinger
 */
public interface IPlugin<T> extends ExtensionPoint {

	/**
	 * Returns the transformer of the plugin to transform the variability model.
	 *
	 * @return the transformer of the plugin to transform the variability model.
	 */
	IModelTransformer<T> getTransformer();

	/**
	 * Returns the deserializer of the plugin to deserializer the variability
	 * model.
	 *
	 * @return the deserializer of the plugin to deserializer the variability
	 * 	       model.
	 */
	IDeserializer<T> getDeserializer();

	/**
	 * Returns the statistics of the plugin to get the statistics the variability
	 * model.
	 *
	 * @return the statistics of the plugin to get the statistics the variability
	 *         model.
	 */
	IStatistics<T> getStatistics();

	/**
	 * Returns the serializer of the plugin to serialize the variability model.
	 *
	 * @return the serializer of the plugin to serialize the variability model.
	 */
	ISerializer<T> getSerializer();

	/**
	 * Returns the pretty printer of the plugin to create human-readable
	 * representations of the variability model.
	 *
	 * @return the pretty printer to create human-readable representations of
	 * 		   the variability model.
	 */
	default IPrettyPrinter<T> getPrinter() {
		return new DefaultPrettyPrinter<T>(getSerializer());
	}

	/**
	 * Returns the variability model type name.
	 *
	 * @return the name of the variability model type.
	 */
	String getName();

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

	/**
	 * Returns a iterable of file extensions for which this plugin is applicable.
	 *
	 * @return a unmodifiable list of file extensions.
	 */
	Iterable<String> getSupportedFileExtensions();
}
