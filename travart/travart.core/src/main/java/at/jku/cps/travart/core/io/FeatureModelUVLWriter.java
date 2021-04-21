package at.jku.cps.travart.core.io;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;

public class FeatureModelUVLWriter extends AFeatureModelWriter {

	@Override
	IPersistentFormat<IFeatureModel> getPersistentFormat() {
		return null; // new UVLFeatureModelFormat();
	}
}
