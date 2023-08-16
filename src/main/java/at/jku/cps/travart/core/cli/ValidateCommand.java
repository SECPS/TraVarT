/**
 * The command validates a given variability artifact using its provided sampling algorithm.
 *
 * @author Kevin Feichtinger
*
* Copyright 2023 Johannes Kepler University Linz
* LIT Cyber-Physical Systems Lab
* All rights reserved
 */
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
