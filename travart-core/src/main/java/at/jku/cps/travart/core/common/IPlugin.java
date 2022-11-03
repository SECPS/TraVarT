package at.jku.cps.travart.core.common;

import org.pf4j.ExtensionPoint;

import java.util.List;

/**
 * The meta-data of the plugin being implemented
 *
 * @author Prankur Agarwal
 */
public interface IPlugin extends ExtensionPoint {
    IModelTransformer getTransformer();

    IReader getReader();

    IWriter getWriter();

    String getName();

    String getVersion();

    String getId();

    List<String> getSupportedFileExtensions();
}
