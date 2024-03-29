/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Prankur Agarwal
 *     @author Kevin Feichtinger
 *
 * Implements a base helper to load and manage available TraVarT plugins.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pf4j.DefaultPluginManager;
import org.pf4j.ManifestPluginDescriptorFinder;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginManager;

import at.jku.cps.travart.core.common.IPlugin;

/**
 * This is the helper class to load, start, use, and close the available plugins
 * in the system.
 *
 * @author Prankur Agarwal
 * @author Kevin Feichtinger
 */
public final class TraVarTPluginManager {
	private static final Map<String, IPlugin> availablePlugins = new HashMap<>();

	private static PluginManager pluginManager;

	private TraVarTPluginManager() {

	}

	/**
	 * A static function to start the available plugins in the system.
	 */
	public static void startPlugins() {
		// create the plugin manager
		pluginManager = new DefaultPluginManager() {
			@Override
			protected PluginDescriptorFinder createPluginDescriptorFinder() {
				return new ManifestPluginDescriptorFinder();
			}
		};
		// load the plugins
		pluginManager.loadPlugins();

		// start the plugins
		pluginManager.startPlugins();

		// find plugins
		findAvailablePlugins();
	}

	/**
	 * A static function to find the available plugins in the system.
	 */
	public static void findAvailablePlugins() {
		// retrieves the extensions for IPlugin extension point
		final List<IPlugin> plugins = pluginManager.getExtensions(IPlugin.class);
		for (final IPlugin plugin : plugins) {
			availablePlugins.put(plugin.getId(), plugin);
		}

	}

	/**
	 * A static function to get the available plugins in the system.
	 */
	public static Map<String, IPlugin> getAvailablePlugins() {
		return Collections.unmodifiableMap(availablePlugins);
	}

	/**
	 * A static function to stop the available plugins in the system.
	 */
	public static void stopPlugins() {
		pluginManager.stopPlugins();
	}
}
