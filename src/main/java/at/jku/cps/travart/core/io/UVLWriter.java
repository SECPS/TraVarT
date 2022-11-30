package at.jku.cps.travart.core.io;

import at.jku.cps.travart.core.common.IWriter;
import de.vill.model.FeatureModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UVLWriter implements IWriter<FeatureModel> {
    @Override
    public void write(final FeatureModel uvlModel, final Path path) throws IOException {
        Files.write(
                path,
                uvlModel.toString().getBytes()
        );
    }
}
