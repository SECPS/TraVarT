package at.jku.cps.travart.core.printer;

import at.jku.cps.travart.core.basic.DefaultPrettyPrinter;
import at.jku.cps.travart.core.common.ISerializer;
import at.jku.cps.travart.core.io.UVLSerializer;
import de.vill.model.FeatureModel;

public class UVLPrettyPrinter extends DefaultPrettyPrinter<FeatureModel> {

    public UVLPrettyPrinter() {
        this(new UVLSerializer());
    }

    public UVLPrettyPrinter(ISerializer<FeatureModel> serializer) {
        super(serializer);
    }
}
