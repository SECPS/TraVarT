package at.jku.cps.travart.core.factory.impl;

import at.jku.cps.travart.core.factory.ICoreModelFactory;
import de.vill.model.FeatureModel;

public final class CoreModelFactory implements ICoreModelFactory {

	public static final String ID = "at.jku.cps.travart.core.factory.impl.CoreModelFactory";

	private static CoreModelFactory factory;

	public static CoreModelFactory getInstance() {
		if (factory == null) {
			factory = new CoreModelFactory();
		}
		return factory;
	}

	private CoreModelFactory() {

	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean initExtension() {
		return true;
	}

	@Override
	public FeatureModel create() {
		return new FeatureModel();
	}

}
