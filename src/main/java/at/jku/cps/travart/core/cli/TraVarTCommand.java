/**
 * The travart command is the main command of the cli.
 * It provides two subcommands: transform and validate.
 *
 * @author Kevin Feichtinger
*
* Copyright 2023 Johannes Kepler University Linz
* LIT Cyber-Physical Systems Lab
* All rights reserved
*/
package at.jku.cps.travart.core.cli;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import at.jku.cps.travart.core.helpers.TraVarTPluginManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

// ValidateCommand.class,
@Command(name = "travart", subcommands = { TransformCommand.class,
		PluginCommand.class }, mixinStandardHelpOptions = true, version = "0.0.1", description = "TraVarT main command to transform and validate variability artifacts.")
public class TraVarTCommand {

	static {
		TraVarTPluginManager.startPlugins();
	}

	@Option(names = { "-v",
			"--verbose" }, scope = ScopeType.INHERIT, description = "Enable verbose log information during execution.")
	public void setVerbose(final boolean[] verbose) {
		// Configure log4j.
		// (This is a simplistic example: a real application may use more levels and
		// perhaps configure only the ConsoleAppender level instead of the root log
		// level.)
		// see picocli documentation: https://picocli.info/#_inherited_options
		Configurator.setRootLevel(verbose.length > 0 ? Level.DEBUG : Level.INFO);
	}

	public static void main(final String[] args) {
		String[] arg = { "-h" };
//		String[] arg = { "transform", "-st=featureide", "-tt=uvl",
//		"C:\\Users\\AK117831\\git\\travart2.0\\resources\\FeatureIDE",
//		"C:\\Users\\AK117831\\git\\travart2.0\\output\\UVL" };
// 		String[] arg = { "plugin" };
		if (args.length != 0) {
			arg = args;
		}
		int exitCode = new CommandLine(new TraVarTCommand()).execute(arg);
		TraVarTPluginManager.stopPlugins();
		System.exit(exitCode);
	}
}
