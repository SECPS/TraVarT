package at.jku.cps.travart.core.common;

/**
 * The meta-data of the plugin being implemented
 *
 * @author Prankur Agarwal
 */
public interface IPlugin {
    String getName();

    String getVersion();

    String getId();
}
