package at.jku.cps.travart.ovm.common;

import java.nio.file.Path;

import at.jku.cps.travart.ovm.factory.IOvModelFactory;
import at.jku.cps.travart.ovm.factory.impl.OvModelFactory;
import at.jku.cps.travart.ovm.model.IOvModel;
import de.ovgu.featureide.fm.core.Logger;
import de.ovgu.featureide.fm.core.base.IFactory;
import de.ovgu.featureide.fm.core.base.impl.FactoryManager;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;

/**
 *
 * This class represents a factory manager for {@link IOvModel}. It extends the
 * abstract factory manager of FeatureIDE. Only the method
 * <code>getFactory</code> is truly overridden. The other overrides are solely
 * for casting the return value correctly.
 *
 * @author johannstoebich
 */
public final class OvModelFactoryManager extends FactoryManager<IOvModel> {

	@Override
	protected String getDefaultID() {
		return OvModelFactory.ID;
	}

	private static OvModelFactoryManager instance = new OvModelFactoryManager();

	public static OvModelFactoryManager getInstance() {
		return instance;
	}

	private OvModelFactoryManager() {
	}

	/**
	 * Returns the OvModel factory that was used to create the given model (if the
	 * factory is not available the default factory is returned).
	 *
	 * @param object the OvModel.
	 *
	 * @return Returns the OvModel factory for the given {@link IOvModel}.
	 */
	@Override
	public IOvModelFactory getFactory(IOvModel object) {
		try {
			return getFactory(object.getFactoryId());
		} catch (final NoSuchExtensionException e) {
			Logger.logError(e);
			return OvModelFactory.getInstance();
		}
	}

	@Override
	public IOvModelFactory getFactory(String id) throws NoSuchExtensionException {
		return (IOvModelFactory) super.getFactory(id);
	}

	@Override
	public IOvModelFactory getFactory(Path path, IPersistentFormat<? extends IOvModel> format)
			throws NoSuchExtensionException {
		return (IOvModelFactory) super.getFactory(path, format);
	}

	@Override
	public IOvModelFactory getFactory(IPersistentFormat<? extends IOvModel> format) throws NoSuchExtensionException {
		return (IOvModelFactory) super.getFactory(format);
	}

	@Override
	public boolean addExtension(IFactory<IOvModel> extension) {
		return extension instanceof IOvModelFactory ? super.addExtension(extension) : false;
	}

}
