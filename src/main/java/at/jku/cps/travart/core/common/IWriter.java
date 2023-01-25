package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface defines a writer, which enables the serialization of a
 * variability model of type <T> to the file system.
 *
 * @param <T> The type of model which should be serialized.
 * @author Kevin Feichtinger
 */
public interface IWriter<T> {
  /**
   * Writes a variability model of type <T> to the given path.
   *
   * @param model    the model to write.
   * @param filePath the path to which the file should be written.
   * @throws IOException                          if the writing operation throws
   *                                              any kind of error.
   * @throws NotSupportedVariabilityTypeException if the given variability model
   *                                              can not be serialized.
   */
  void write(T model, Path filePath) throws IOException, NotSupportedVariabilityTypeException;
}
