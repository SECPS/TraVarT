package at.jku.cps.travart;

import at.jku.cps.travart.core.common.IPlugin;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ManifestPluginDescriptorFinder;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraVarT {

    public static void main(final String[] args) {
        // create the plugin manager
        final PluginManager pluginManager = new DefaultPluginManager() {

            @Override
            protected PluginDescriptorFinder createPluginDescriptorFinder() {
                return new ManifestPluginDescriptorFinder();
            }
        };

        // load the plugins
        pluginManager.loadPlugins();

        // start the plugins
        pluginManager.startPlugins();

        // retrieves the extensions for IPlugin extension point
        final List<IPlugin> plugins = pluginManager.getExtensions(IPlugin.class);
        final Map<String, IPlugin> pluginMap = new HashMap<>();
        for (final IPlugin plugin : plugins) {
            pluginMap.put(plugin.getId(), plugin);
            System.out.println(plugin.getName());
        }

        // stop the plugins
        pluginManager.stopPlugins();
    }
}
