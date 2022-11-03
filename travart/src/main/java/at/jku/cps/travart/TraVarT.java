package at.jku.cps.travart;

public class TraVarT {
    public static void main(final String[] args) {
        TraVarTPluginManager.startPlugins();
        TraVarTPluginManager.getAvailablePlugins();
        // whatever we want to do
        // TraVarTPluginManager.availablePlugins.get("ppr-dsl-plugin")
        TraVarTPluginManager.stopPlugins();
    }
}
