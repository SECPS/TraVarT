package at.jku.cps.travart.core.io;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.propositionalModel.MODELFormat;

public class FeatureModelMODELWriter extends AFeatureModelWriter {

	@Override
	IPersistentFormat<IFeatureModel> getPersistentFormat() {
		return new MODELFormat();
	}

}
