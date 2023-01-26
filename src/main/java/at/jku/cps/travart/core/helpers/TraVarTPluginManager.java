package at.jku.cps.travart.core.helpers;

import at.jku.cps.travart.core.common.IPlugin;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ManifestPluginDescriptorFinder;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginManager;

public final class TraVarTPluginManager {
    private static final Map<String, IPlugin> availablePlugins = new HashMap<>();

    private static PluginManager pluginManager;

    private TraVarTPluginManager() {

    }

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

    public static void findAvailablePlugins() {
        // retrieves the extensions for IPlugin extension point
        final List<IPlugin> plugins = pluginManager.getExtensions(IPlugin.class);
        for (final IPlugin plugin : plugins) {
            availablePlugins.put(plugin.getId(), plugin);
        }

    }

    public static Map<String, IPlugin> getAvailablePlugins() {
        return Collections.unmodifiableMap(availablePlugins);
    }

    public static void stopPlugins() {
        pluginManager.stopPlugins();
    }
}
