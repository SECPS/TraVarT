package at.jku.cps.travart.ppr.dsl.transformation;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
import de.ovgu.featureide.fm.core.init.FMCoreLibrary;
import de.ovgu.featureide.fm.core.init.LibraryManager;

public class FeatureIDEtoGabelCSVConverterTest {
	static {
		LibraryManager.registerLibrary(FMCoreLibrary.getInstance());
	}
	private IFeatureModel model;
	private FeatureModelToPprDslTransformer converter;

	@Before
	public void setUp() throws Exception {
		converter = new FeatureModelToPprDslTransformer();
		model = new FeatureModel("testModel");
		Feature f1 = new Feature(model, "Feature 1");
		Feature f2 = new Feature(model, "Feature 2");
		model.addFeature(f1);
		model.addFeature(f2);
	}

	@Ignore("needs adjustments in tested class to be implementable")
	@Test
	public void testConvertFeature() {
	}

	@Ignore("needs adjustments in tested class to be implementable")
	@Test
	public void testConvertConstraints() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore("needs adjustments in tested class to be implementable")
	@Test
	public void testConvertIFeatureModel() {
	}

	@Ignore("needs adjustments in tested class to be implementable")
	@Test
	public void testTransform() throws NotSupportedVariablityTypeException {
	}

	@Ignore("needs adjustments in tested class to be implementable")
	@Test
	public void testGetOriginModel() {
		fail("Not yet implemented"); // TODO
	}

}
