package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface defines a reader to read a variability model of type <T> from
 * the file system.
 *
 * @param <T> The type of model which should read from the file system.
 * @author Kevin Feichtinger
 */
public interface IReader<T> {

    /**
     * Reads a variability model of type <T> from the given path.
     *
     * @param filePath the path to read from.
     * @return a variability model of type <T>.
     * @throws IOException                          if the writing operation throws
     *                                              any kind of error.
     * @throws NotSupportedVariabilityTypeException if the given variability model
     *                                              is not a valid.
     */
    T read(Path filePath) throws IOException, NotSupportedVariabilityTypeException;

    /**
     * Reads a variability model of type <T> from the given file @see
     * {@link #read(Path)}.
     *
     * @param file the file to read.
     * @return a variability model of type <T>.
     * @throws IOException                          if the writing operation throws
     *                                              any kind of error.
     * @throws NotSupportedVariabilityTypeException if the given variability model
     *                                              is not a valid.
     */
    default T read(final File file) throws IOException, NotSupportedVariabilityTypeException {
        return this.read(file.toPath());
    }
}
