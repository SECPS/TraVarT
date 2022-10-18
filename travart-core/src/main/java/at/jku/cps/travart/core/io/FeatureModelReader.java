package at.jku.cps.travart.core.io;

import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

import java.io.IOException;
import java.nio.file.Path;

public class FeatureModelReader implements IReader<IFeatureModel> {

    static {
        LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
    }

    @Override
    public IFeatureModel read(final Path path) throws IOException, NotSupportedVariabilityTypeException {
        return FeatureModelManager.load(path);
    }
}