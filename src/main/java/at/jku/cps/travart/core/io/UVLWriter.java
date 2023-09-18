/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Implements a writer for the core model of TraVarT.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import at.jku.cps.travart.core.common.IWriter;
import de.vill.model.FeatureModel;

/**
 * Writes a Universal Variability Language (UVL) model to the file system. UVL
 * is used as the core model and is developed by the MODEVAR initiative.
 *
 * @author Kevin Feichtinger
 * @see <a href="https://doi.org/10.1145/3461001.3471145">UVL SPLC Paper
 *      2021</a>
 * @see <a href="https://github.com/Universal-Variability-Language">UVL Github
 *      Repository</a>
 * @see <a href="https://modevar.github.io/">MODEVAR initiative</a>
 */
public class UVLWriter implements IWriter<FeatureModel> {

	@Override
	public void write(final FeatureModel uvlModel, final Path path) throws IOException {
		Files.write(path, uvlModel.toString().getBytes());
	}

	@Override
	public String getFileExtension() {
		return ".uvl";
	}
}
