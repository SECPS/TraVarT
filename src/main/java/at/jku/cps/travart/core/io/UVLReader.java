package at.jku.cps.travart.core.io;

import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import de.vill.exception.ParseError;
import de.vill.main.UVLModelFactory;
import de.vill.model.FeatureModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UVLReader implements IReader<FeatureModel> {
    @Override
    public FeatureModel read(final Path path) throws IOException, NotSupportedVariabilityTypeException {
        final String content = new String(Files.readAllBytes(path));
        final UVLModelFactory uvlModelFactory = new UVLModelFactory();
        try {
            return uvlModelFactory.parse(content);
        } catch (final ParseError error) {
            throw new NotSupportedVariabilityTypeException("Error in reading UVL Model file");
        }
    }
}
