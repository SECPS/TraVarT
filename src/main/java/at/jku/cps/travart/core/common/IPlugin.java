package at.jku.cps.travart.core.common;

import java.util.List;

import org.pf4j.ExtensionPoint;

/**
 * The meta-data of the plugin being implemented
 *
 * @author Prankur Agarwal
 */
@SuppressWarnings("rawtypes")
public interface IPlugin extends ExtensionPoint {
	IModelTransformer getTransformer();

	IReader getReader();

	IWriter getWriter();

	String getName();

	String getVersion();

	String getId();

	List<String> getSupportedFileExtensions();
}
