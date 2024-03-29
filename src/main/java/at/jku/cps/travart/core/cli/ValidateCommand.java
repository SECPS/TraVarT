/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Command line tool command to validate a transformed variability artifact.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.cli;

import java.util.concurrent.Callable;

import picocli.CommandLine.Command;

@Command(name = "validate", version = "0.0.1", description = "Validates a given variability artifact with its given sampler.")
public class ValidateCommand implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		return 0;
	}

}
