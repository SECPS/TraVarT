/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Implements a own library for initializing the FeatureIDE samplers.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
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

public final class DefaultLibrary implements ILibrary {

	private static DefaultLibrary instance;

	private DefaultLibrary() {
	}

	public static DefaultLibrary getInstance() {
		if (instance == null) {
			instance = new DefaultLibrary();
		}
		return instance;
	}

	@Override
	public void install() {
		FileSystem.INSTANCE = new JavaFileSystem();
		LongRunningWrapper.INSTANCE = new LongRunningCore();
		Logger.logger = new JavaLogger();

		FMFactoryManager.getInstance().addExtension(DefaultFeatureModelFactory.getInstance());
		FMFactoryManager.getInstance().addExtension(MultiFeatureModelFactory.getInstance());
		FMFactoryManager.getInstance().setWorkspaceLoader(new CoreFactoryWorkspaceLoader());

		CLIFunctionManager.getInstance().addExtension(new ConfigurationGenerator());
	}

	@Override
	public void uninstall() {
		FMFactoryManager.getInstance().save();
		ConfigurationFactoryManager.getInstance().save();
	}
}
