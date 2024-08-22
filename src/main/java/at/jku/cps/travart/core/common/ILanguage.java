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
 * The base interface for a variability language.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.basic.DefaultPrettyPrinter;

/**
 * A variability language must provide access to a
 * {@link ISerializer}/{@link IDeserializer} to serialize/deserialize the
 * language's variability model and {@link IStatistics} to get the statistics of
 * the variability model.
 *
 * @author Prankur Agarwal
 * @author Kevin Feichtinger
 * @author Jakob Gretenkort
 */
public interface ILanguage<T> {
	/**
	 * Returns the deserializer of the language to deserializer the variability
	 * model.
	 *
	 * @return the deserializer of the language to deserializer the variability
	 * 	       model.
	 */
	IDeserializer<T> getDeserializer();

	/**
	 * Returns the statistics of the language to get the statistics the variability
	 * model.
	 *
	 * @return the statistics of the language to get the statistics the variability
	 *         model.
	 */
	IStatistics<T> getStatistics();

	/**
	 * Returns the serializer of the language to serialize the variability model.
	 *
	 * @return the serializer of the language to serialize the variability model.
	 */
	ISerializer<T> getSerializer();

	/**
	 * Returns the pretty printer of the language to create human-readable
	 * representations of the variability model.
	 *
	 * @return the pretty printer to create human-readable representations of
	 * 		   the variability model.
	 */
	default IPrettyPrinter<T> getPrinter() {
		return new DefaultPrettyPrinter<T>(this.getSerializer());
	}

	/**
	 * Returns the variability model type name.
	 *
	 * @return the name of the variability model type.
	 */
	String getName();

    /**
     * Returns an abbreviation, typically an acronym of the variability type name.
     *
     * @return the abbreviated version of the variability type name.
     */
    String getAbbreviation();

	/**
	 * Returns a iterable of file extensions for which this language is applicable.
	 *
	 * @return a unmodifiable list of file extensions.
	 */
	Iterable<String> getSupportedFileExtensions();
}
