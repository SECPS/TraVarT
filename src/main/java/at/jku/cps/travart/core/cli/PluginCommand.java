/**
 * Defines the plugin command for the TraVarT CLI. Lists the available plugins of the current installation.
 *
 * @author Kevin Feichtinger
*
* Copyright 2023 Johannes Kepler University Linz
* LIT Cyber-Physical Systems Lab
* All rights reserved
*/
package at.jku.cps.travart.core.cli;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import at.jku.cps.travart.core.helpers.TraVarTPluginManager;
import picocli.CommandLine.Command;

@Command(name = "plugin", version = "0.0.1", description = "Lists the available plugins for the TraVarT command")
public class PluginCommand implements Callable<Integer> {

	private static final Logger LOGGER = LogManager.getLogger(PluginCommand.class);

	@Override
	public Integer call() throws Exception {
		if (!TraVarTPluginManager.getAvailablePlugins().isEmpty()) {
			LOGGER.debug(String.format("Installed Plugins: %s.", toPluginNameString()));
			System.out.println(String.format("Installed Plugins: %s.", toPluginNameString()));
			return 0;
		}
		return -1;
	}

	private static String toPluginNameString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{ ");
		for (String pluginName : TraVarTCommand.getAvailableTypes()) {
			builder.append(pluginName).append(",");
		}
		builder.append(" UVL }");
		return builder.toString();
	}

}
