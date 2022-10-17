package at.jku.cps.travart.core.common;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @param <T> The type of model which should be persisted, using this writer.
 * @author Kevin Feichtinger
 */
public interface IWriter<T> {
    void write(T model, Path filePath) throws IOException, NotSupportedVariabilityTypeException;
}
