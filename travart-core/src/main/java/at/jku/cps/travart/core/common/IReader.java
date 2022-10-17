package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @param <T> The type of model which should be loaded, using this reader.
 * @author Kevin Feichtinger
 */
public interface IReader<T> {

    T read(Path filePath) throws IOException, NotSupportedVariabilityTypeException;

    default T read(File file) throws IOException, NotSupportedVariabilityTypeException {
        return this.read(file.toPath());
    }

    // batch of files / paths

    // look for success and failure reads
}
