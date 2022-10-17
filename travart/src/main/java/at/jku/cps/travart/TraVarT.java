package at.jku.cps.travart;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.util.List;

public class TraVarT {
    public static void main(String[] args) {
        // create the plugin manager
        PluginManager pluginManager = new DefaultPluginManager();

        // load the plugins
        pluginManager.loadPlugins();

        // get the plugins
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
//        for (PluginWrapper plugin : startedPlugins) {
//            String pluginId = plugin.getDescriptor().getPluginId();
//            System.out.println("Extensions added by plugin '{}}':" + pluginId);
//            Set<String> extensionClassNames = pluginManager.getExtensionClassNames(pluginId);
//            for (String extension : extensionClassNames) {
//                System.out.println("   " + extension);
//            }
//        }

        // stop the plugins
        pluginManager.stopPlugins();
    }
}
