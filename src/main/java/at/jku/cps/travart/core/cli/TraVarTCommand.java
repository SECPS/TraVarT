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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import at.jku.cps.travart.core.helpers.TraVarTPluginManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

@Command(name = "travart", subcommands = { TransformCommand.class, ValidateCommand.class,
		PluginCommand.class }, mixinStandardHelpOptions = true, version = "0.0.1", description = "TraVarT main command to transform and validate variability artifacts.")
public class TraVarTCommand {

	static {
		TraVarTPluginManager.startPlugins();
	}

	private static Set<String> availableTypes;
	private static Map<String, String> nameToKey;
	private static Map<String, String> keyToName;

	public static Set<String> getAvailableTypes() {
		if (availableTypes == null) {
			availableTypes = new HashSet<>();
			TraVarTPluginManager.getAvailablePlugins().values().forEach(p -> availableTypes.add(p.getName()));
		}
		return Collections.unmodifiableSet(availableTypes);
	}

	public static Map<String, String> nameToKeyMap() {
		if (nameToKey == null) {
			nameToKey = new HashMap<>();
			TraVarTPluginManager.getAvailablePlugins().entrySet()
					.forEach(e -> nameToKey.put(e.getValue().getName(), e.getKey()));
		}
		return Collections.unmodifiableMap(nameToKey);
	}

	public static Map<String, String> keyToNameMap() {
		if (keyToName == null) {
			keyToName = new HashMap<>();
			TraVarTPluginManager.getAvailablePlugins().entrySet()
					.forEach(e -> keyToName.put(e.getKey(), e.getValue().getName()));
		}
		return Collections.unmodifiableMap(keyToName);
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
		String[] arg = { "transform" };
		int exitCode = new CommandLine(new TraVarTCommand()).execute(arg);
		System.exit(exitCode);
	}
}
