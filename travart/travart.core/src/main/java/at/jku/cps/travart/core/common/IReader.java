package at.jku.cps.travart.core.common;

import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;

/**
 * @author Kevin Feichtinger
 *
 * @param <T> The type of model which should be loaded, using this reader.
 */
public interface IReader<T> {

	T read(Path filePath) throws IOException, NotSupportedVariablityTypeException;

}
