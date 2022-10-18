package at.jku.cps.travart.core.io;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.fama.FAMAFormat;

public class FeatureModelFAMAWriter extends AFeatureModelWriter {

    public static final String FM_FAMA_EXTENSION = ".fama";

    @Override
    IPersistentFormat<IFeatureModel> getPersistentFormat() {
        return new FAMAFormat();
    }

}
