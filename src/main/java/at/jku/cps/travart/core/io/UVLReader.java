/*******************************************************************************
 * TODO: explanation what the class does
 *
 * @author Kevin Feichtinger
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import de.vill.exception.ParseError;
import de.vill.main.UVLModelFactory;
import de.vill.model.FeatureModel;

/**
 * Reads a Universal Variability Language (UVL) model from the file system. UVL
 * is used as the core model and is developed by the MODEVAR initiative.
 *
 * @author Kevin Feichtinger
 * @see <a href="https://doi.org/10.1145/3461001.3471145">UVL SPLC Paper
 *      2021</a>
 * @see <a href="https://github.com/Universal-Variability-Language">UVL Github
 *      Repository</a>
 * @see <a href="https://modevar.github.io/">MODEVAR initiative</a>
 */
public class UVLReader implements IReader<FeatureModel> {

	@Override
	public Iterable<String> fileExtensions() {
		return List.of("uvl", "xml");
	}

	@Override
	public FeatureModel read(final Path path) throws IOException, NotSupportedVariabilityTypeException {
		final String content = new String(Files.readAllBytes(path));
		final UVLModelFactory uvlModelFactory = new UVLModelFactory();
		try {
			return uvlModelFactory.parse(content);
		} catch (final ParseError error) {
			throw new NotSupportedVariabilityTypeException("Error in reading UVL Model file");
		}
	}
}
