package at.jku.cps.travart;

import at.jku.cps.travart.core.common.IPlugin;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ManifestPluginDescriptorFinder;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class TraVarT {
    private static final Logger log = LoggerFactory.getLogger(TraVarT.class);

    public static void main(String[] args) {
        // create the plugin manager
        PluginManager pluginManager = new DefaultPluginManager() {

            @Override
            protected PluginDescriptorFinder createPluginDescriptorFinder() {
                return new ManifestPluginDescriptorFinder();
            }

        };

        // load the plugins
        pluginManager.loadPlugins();

        // start (active/resolved) the plugins
        pluginManager.startPlugins();

        // retrieves the extensions for Greeting extension point
        List<IPlugin> greetings = pluginManager.getExtensions(IPlugin.class);
        log.info("Found {} extensions for extension point '{}'", greetings.size(), IPlugin.class.getName());
        for (IPlugin greeting : greetings) {
            log.info(">>> {}", greeting.getName());
        }

        // print extensions from classpath (non plugin)
        log.info("Extensions added by classpath:");
        Set<String> extensionClassNames = pluginManager.getExtensionClassNames(null);
        for (String extension : extensionClassNames) {
            log.info("   {}", extension);
        }

        log.info("Extension classes by classpath:");
        List<Class<? extends IPlugin>> greetingsClasses = pluginManager.getExtensionClasses(IPlugin.class);
        for (Class<? extends IPlugin> greeting : greetingsClasses) {
            log.info("   Class: {}", greeting.getCanonicalName());
        }

        // print extensions ids for each started plugin
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            log.info("Extensions added by plugin '{}}':", pluginId);
            extensionClassNames = pluginManager.getExtensionClassNames(pluginId);
            for (String extension : extensionClassNames) {
                log.info("   {}", extension);
            }
        }

        // print the extensions instances for Greeting extension point for each started plugin
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            log.info("Extensions instances added by plugin '{}' for extension point '{}':", pluginId, IPlugin.class.getName());
            List<IPlugin> extensions = pluginManager.getExtensions(IPlugin.class, pluginId);
            for (Object extension : extensions) {
                log.info("   {}", extension);
            }
        }

        // print extensions instances from classpath (non plugin)
        log.info("Extensions instances added by classpath:");
        List<?> extensions = pluginManager.getExtensions((String) null);
        for (Object extension : extensions) {
            log.info("   {}", extension);
        }

        // print extensions instances for each started plugin
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            log.info("Extensions instances added by plugin '{}':", pluginId);
            extensions = pluginManager.getExtensions(pluginId);
            for (Object extension : extensions) {
                log.info("   {}", extension);
            }
        }
        // stop the plugins
        pluginManager.stopPlugins();
    }
}
