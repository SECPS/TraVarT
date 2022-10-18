package at.jku.cps.travart.core.io;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.dimacs.DIMACSFormat;

public class FeatureModelDIMACSWriter extends AFeatureModelWriter {

    public static final String FM_DIMACS_EXTENSION = ".dimacs";

    @Override
    IPersistentFormat<IFeatureModel> getPersistentFormat() {
        return new DIMACSFormat();
    }

}
