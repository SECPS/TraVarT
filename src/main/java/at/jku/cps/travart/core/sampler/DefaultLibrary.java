package at.jku.cps.travart.core.sampler;

import de.ovgu.featureide.fm.core.JavaLogger;
import de.ovgu.featureide.fm.core.Logger;
import de.ovgu.featureide.fm.core.base.impl.ConfigurationFactoryManager;
import de.ovgu.featureide.fm.core.base.impl.CoreFactoryWorkspaceLoader;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.FMFactoryManager;
import de.ovgu.featureide.fm.core.base.impl.MultiFeatureModelFactory;
import de.ovgu.featureide.fm.core.cli.CLIFunctionManager;
import de.ovgu.featureide.fm.core.cli.ConfigurationGenerator;
import de.ovgu.featureide.fm.core.init.ILibrary;
import de.ovgu.featureide.fm.core.io.FileSystem;
import de.ovgu.featureide.fm.core.io.JavaFileSystem;
import de.ovgu.featureide.fm.core.job.LongRunningCore;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;

public class DefaultLibrary implements ILibrary {

	private static DefaultLibrary instance;

	public static DefaultLibrary getInstance() {
		if (instance == null) {
			instance = new DefaultLibrary();
		}
		return instance;
	}

	private DefaultLibrary() {
	}

	@Override
	public void install() {
		FileSystem.INSTANCE = new JavaFileSystem();
		LongRunningWrapper.INSTANCE = new LongRunningCore();
		Logger.logger = new JavaLogger();

		FMFactoryManager.getInstance().addExtension(DefaultFeatureModelFactory.getInstance());
		FMFactoryManager.getInstance().addExtension(MultiFeatureModelFactory.getInstance());
		FMFactoryManager.getInstance().setWorkspaceLoader(new CoreFactoryWorkspaceLoader());

//		FMFormatManager.getInstance().addExtension(new XmlFeatureModelFormat());
//		FMFormatManager.getInstance().addExtension(new SimpleVelvetFeatureModelFormat());
//		FMFormatManager.getInstance().addExtension(new DIMACSFormat());
//		FMFormatManager.getInstance().addExtension(new SXFMFormat());
//		FMFormatManager.getInstance().addExtension(new ConquererFMWriter());
//		FMFormatManager.getInstance().addExtension(new CNFFormat());
//		FMFormatManager.getInstance().addExtension(new UVLFeatureModelFormat());
//
//		ConfigurationFactoryManager.getInstance().addExtension(DefaultConfigurationFactory.getInstance());
//		ConfigurationFactoryManager.getInstance().setWorkspaceLoader(new CoreFactoryWorkspaceLoader());
//
//		ConfigFormatManager.getInstance().addExtension(new XMLConfFormat());
//		ConfigFormatManager.getInstance().addExtension(new DefaultFormat());
//		ConfigFormatManager.getInstance().addExtension(new FeatureIDEFormat());
//		ConfigFormatManager.getInstance().addExtension(new EquationFormat());
//		ConfigFormatManager.getInstance().addExtension(new ExpressionFormat());

		CLIFunctionManager.getInstance().addExtension(new ConfigurationGenerator());
	}

	@Override
	public void uninstall() {
		FMFactoryManager.getInstance().save();
		ConfigurationFactoryManager.getInstance().save();
	}

}
