/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * The base interface for writing a variability artifact.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

/**
 * The interface defines a writer, which enables the serialization of a
 * variability model of type <T> to the file system.
 *
 * @param <T> The type of model which should be serialized.
 * @author Kevin Feichtinger
 */
public interface IWriter<T> {
	/**
	 * Writes a variability model of type <T> to the given path.
	 *
	 * @param model    the model to write.
	 * @param filePath the path to which the file should be written.
	 * @throws IOException                          if the writing operation throws
	 *                                              any kind of error.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              can not be serialized.
	 */
	void write(T model, Path filePath) throws IOException, NotSupportedVariabilityTypeException;

	/**
	 * The file extension this writer uses.
	 *
	 * @return the file extension this writer uses.
	 */
	String getFileExtension();
}
