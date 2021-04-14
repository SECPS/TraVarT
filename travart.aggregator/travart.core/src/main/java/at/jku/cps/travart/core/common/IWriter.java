package at.jku.cps.travart.core.common;

import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;

/**
 * @author Kevin Feichtinger
 *
 * @param <T> The type of model which should be persisted, using this writer.
 */
public interface IWriter<T> {

	void write(T fm, Path filePath) throws IOException, NotSupportedVariablityTypeException;
}
