/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 * 	   @author Jakob Gretenkort
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

/**
 * The interface defines a deserializer to deserialize a variability model of
 * type <T>.
 *
 * @param <T> The type of model which should read from the file system.
 * @author Kevin Feichtinger
 */
public interface IDeserializer<T> {

	/**
	 * Reads and deserializes a variability model of type <T> from the given path.
	 *
	 * @param filePath the path to read from.
	 * @return a variability model of type <T>.
	 * @throws IOException                          if the writing operation throws
	 *                                              any kind of error.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	default T deserializeFromFile(Path filePath) throws IOException, NotSupportedVariabilityTypeException {
		for (Format format : this.supportedFormats()) {
			if (filePath.toString().endsWith(format.extension()) && format.isText()) {
				String content = Files.readString(filePath);
				return deserialize(content, format);
			}
		}
		throw new NotSupportedVariabilityTypeException("No supported text format found that matches the given file's extension.");
	}

	/**
	 * Reads and deserializes a variability model of type <T> from the given
	 * file @see {@link #deserializeFromFile(Path)}.
	 *
	 * @param file the file to read.
	 * @return a variability model of type <T>.
	 * @throws IOException                          if the writing operation
	 *                                              throws any kind of error.
	 * @throws NotSupportedVariabilityTypeException if the given variability model
	 *                                              is not a valid.
	 */
	default T deserializeFromFile(final File file) throws IOException, NotSupportedVariabilityTypeException {
		return this.deserializeFromFile(file.toPath());
	}

	/**
	 * Deserializes a veriability model of type <T> from the given string,
	 * interpreting the string as having the given format.
	 * Only supported for text-based formats.
	 *
	 * @param serial The serialized variability model.
	 * @param format The format of the serialized variability model.
	 * @return The deserialized variability model.
	 *
	 * @throws NotSupportedVariabilityTypeException if the given variability
	 *                                              model is not a valid.
	 */
	T deserialize(String serial, Format format) throws NotSupportedVariabilityTypeException;

	/**
	 * Creates an iterable of formats supported by this deserializer.
	 *
	 * @return an iterable of formats supported by this deserializer.
	 */
	Iterable<Format> supportedFormats();

	/**
	 * Creates an iterable of file extensions supported with this reader.
	 *
	 * @return a interable representation of file extensions supported with this
	 *         reader.
	 */
	default Iterable<String> fileExtensions() {
		List<String> extensions = new ArrayList<>();
		supportedFormats().forEach(f -> extensions.add(f.extension()));
		return extensions;
	}
}
