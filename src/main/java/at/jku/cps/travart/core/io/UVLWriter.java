package at.jku.cps.travart.core.io;

import at.jku.cps.travart.core.common.IWriter;
import de.vill.model.FeatureModel;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes a Universal Variability Language (UVL) model to the file system. UVL
 * is used as the core model and is developed by the MODEVAR initiative.
 *
 * @author Kevin Feichtinger
 * @see <a href="https://doi.org/10.1145/3461001.3471145">UVL SPLC Paper
 * 2021</a>
 * @see <a href="https://github.com/Universal-Variability-Language">UVL Github
 * Repository</a>
 * @see <a href="https://modevar.github.io/">MODEVAR initiative</a>
 */
public class UVLWriter implements IWriter<FeatureModel> {

  @Override
  public void write(final FeatureModel uvlModel, final Path path) throws IOException {
    Files.write(path, uvlModel.toString().getBytes());
  }
}
