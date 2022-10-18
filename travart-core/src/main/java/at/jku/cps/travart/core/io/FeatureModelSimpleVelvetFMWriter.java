package at.jku.cps.travart.core.io;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.velvet.SimpleVelvetFeatureModelFormat;

public class FeatureModelSimpleVelvetFMWriter extends AFeatureModelWriter {

    public static final String FM_SIMPLE_VELVET_EXTENSION = ".simplevelvet";

    @Override
    IPersistentFormat<IFeatureModel> getPersistentFormat() {
        return new SimpleVelvetFeatureModelFormat();
    }

}
