package at.jku.cps.travart.core.common;

import java.util.List;
import org.pf4j.ExtensionPoint;

/**
 * A TraVarT plugin must provide access to a {@link IModelTransformer} to
 * transform a variability model, {@link IReader}/{@link IWriter} to
 * read/write the variability model and {@link IStatistics} to get the statistics
 * of the variability model. Additionally, some meta-data of the plugin
 * should be available.
 *
 * @author Prankur Agarwal
 * @author Kevin Feichtinger
 */
@SuppressWarnings("rawtypes")
public interface IPlugin extends ExtensionPoint {

  /**
   * Returns the transformer of the plugin to transform the variability model.
   *
   * @return the transformer of the plugin to transform the variability model.
   */
  IModelTransformer getTransformer();

  /**
   * Returns the reader of the plugin to read the variability model from the file
   * system.
   *
   * @return the reader of the plugin to read the variability model from the file
   * system.
   */
  IReader getReader();

  /**
   * Returns the statistics of the plugin to get the statistics the variability model.
   *
   * @return the statistics of the plugin to get the statistics the variability model.
   */
  IStatistics getStatistics();

  /**
   * Returns the writer of the plugin to write the variability model to the file
   * system.
   *
   * @return the writer of the plugin to write the variability model to the file
   * system.
   */
  IWriter getWriter();

  /**
   * Returns the variability model type name.
   *
   * @return the name of the variability model type.
   */
  String getName();

  /**
   * Returns the version of the plugin.
   *
   * @return the version of the plugin.
   */
  String getVersion();

  /**
   * Returns a unique ID of the plugin, such that it can be identified.
   *
   * @return the unique ID of the plugin.
   */
  String getId();

  /**
   * Returns a unmodifiable list of file extensions including the {@code .} for
   * which this plugin is applicable.
   *
   * @return a unmodifiable list of file extensions.
   */
  List<String> getSupportedFileExtensions();
}
