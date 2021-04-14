package at.jku.cps.travart.core.io;

import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.common.IWriter;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.manager.FeatureModelManager;

public abstract class AFeatureModelWriter implements IWriter<IFeatureModel> {

	private static final String ERROR_FORMAT_DOES_NOT_SUPPORT_WRITING = "File format %s can not be written";

	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
	}

	abstract IPersistentFormat<IFeatureModel> getPersistentFormat();

	@Override
	public void write(final IFeatureModel fm, final Path filePath)
			throws IOException, NotSupportedVariablityTypeException {
		IPersistentFormat<IFeatureModel> format = getPersistentFormat();
		if (format.supportsWrite()) {
			FeatureModelManager.save(fm, filePath, format);
		} else {
			throw new NotSupportedVariablityTypeException(new IOException(
					String.format(ERROR_FORMAT_DOES_NOT_SUPPORT_WRITING, format.getClass().getCanonicalName())));
		}
	}
}
