package at.jku.cps.travart.core.io;

import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.io.IPersistentFormat;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;

public class FeatureModelXMLWriter extends AFeatureModelWriter {

    public static final String FM_XML_EXTENSION = ".xml";

    @Override
    IPersistentFormat<IFeatureModel> getPersistentFormat() {
        return new XmlFeatureModelFormat();
    }
}
