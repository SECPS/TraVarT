/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * The base interface for reading a variability artifact.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

/**
 * The interface defines a reader to read a variability model of type <T> from
 * the file system.
 *
 * @param <T> The type of model which should read from the file system.
 * @author Kevin Feichtinger
 */
public interface IReader<T> {

	/**
	 * Reads a variability model of type <T> from the given path.
	 *
	 * @param filePath the path to read from.
	 * @return a variability model of type <T>.
	 * @throws IOException                          if the writing operation throws
	 *                                              any kind of error.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	T read(Path filePath) throws IOException, NotSupportedVariabilityTypeException;

	/**
	 * Reads a variability model of type <T> from the given file @see
	 * {@link #read(Path)}.
	 *
	 * @param file the file to read.
	 * @return a variability model of type <T>.
	 * @throws IOException                          if the writing operation throws
	 *                                              any kind of error.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	default T read(final File file) throws IOException, NotSupportedVariabilityTypeException {
		return this.read(file.toPath());
	}

	/**
	 * Creates an iterable of file extensions supported with this reader.
	 *
	 * @return a interable representation of file extensions supported with this
	 *         reader.
	 */
	Iterable<String> fileExtensions();
}
