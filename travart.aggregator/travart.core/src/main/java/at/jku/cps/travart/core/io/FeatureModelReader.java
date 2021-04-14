package at.jku.cps.travart.core.io;

import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public class FeatureModelReader implements IReader<IFeatureModel> {

	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
	}

	@Override
	public IFeatureModel read(final Path path) throws IOException, NotSupportedVariablityTypeException {
		return FeatureModelManager.load(path);
	}
}
