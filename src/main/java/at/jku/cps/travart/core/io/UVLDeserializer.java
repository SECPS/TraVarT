/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Implements a reader for the core model of TraVarT.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.io;

import java.util.List;

import at.jku.cps.travart.core.common.Format;
import at.jku.cps.travart.core.common.IDeserializer;
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
public class UVLDeserializer implements IDeserializer<FeatureModel> {
	@Override
	public FeatureModel deserialize(String serial, Format format) throws NotSupportedVariabilityTypeException {
		final UVLModelFactory uvlModelFactory = new UVLModelFactory();
		try {
			return uvlModelFactory.parse(serial);
		} catch (final ParseError error) {
			throw new NotSupportedVariabilityTypeException("Error in reading UVL Model file");
		}
	}

	@Override
	public Iterable<Format> supportedFormats() {
		return List.of(UVLSerializer.UVL_FORMAT);
	}
}
