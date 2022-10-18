package at.jku.cps.travart.core.io;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.splconquerer.ConquererFMWriter;

public class FeatureModelConquererFMWriter extends AFeatureModelWriter {

    public static final String FM_CONQUERFM_EXTENSION = ".conquer";

    @Override
    IPersistentFormat<IFeatureModel> getPersistentFormat() {
        return new ConquererFMWriter();
    }

}
