package at.jku.cps.travart.dopler.transformation;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.core.common.ISampler;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.core.sampling.FeatureModelSampler;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.factory.impl.DecisionModelFactory;
import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;
import at.jku.cps.travart.dopler.sampling.DecisionModelSampler;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

public class FeatureModeltoDecisionModelConverterTest {
	private FeatureModel fm;
	private DecisionModel controlModel;
	
	@Before
	public void setUp() throws Exception {
		fm=new FeatureModel("TestModel");
		DecisionModelFactory factory = DecisionModelFactory.getInstance();
		controlModel = factory.create();
		controlModel.setName("TestModel");
	}

	@Test
	public void testTransformOrDecision() throws NotSupportedVariablityTypeException {
//		EnumDecision ed1 = new EnumDecision("ed1");
//		StringValue sv1 = new StringValue("sv1");
//		StringValue sv2 = new StringValue("sv2");
//		StringValue sv3 = new StringValue("sv3");
//		ed1.getRange().add(sv1);
//		ed1.getRange().add(sv2);
//		ed1.getRange().add(sv3);
//		ed1.setCardinality(new Cardinality(1, 3));
//		controlModel.add(ed1);
		
		IFeature cmed1 = new Feature(fm, "ed1");
		FeatureUtils.setRoot(fm, cmed1);
		FeatureUtils.setOr(cmed1);
		
		IFeature cmsv1 = new Feature(fm, "sv1");
		IFeature cmsv2 = new Feature(fm, "sv2");
		IFeature cmsv3 = new Feature(fm, "sv3");
		
		FeatureUtils.addFeature(fm, cmed1);
		FeatureUtils.addFeature(fm, cmsv1);
		FeatureUtils.addFeature(fm, cmsv2);
		FeatureUtils.addFeature(fm, cmsv3);
		
		FeatureUtils.addChild(cmed1, cmsv1);
		FeatureUtils.addChild(cmed1, cmsv2);
		FeatureUtils.addChild(cmed1, cmsv3);
		
		ISampler<IDecisionModel> dsamp= new DecisionModelSampler();
		ISampler<IFeatureModel> fsamp= new FeatureModelSampler();
		FeatureModeltoDecisionModelConverter conv = new FeatureModeltoDecisionModelConverter();

		DecisionModel dm = conv.transform(fm);
		Set<Map<IConfigurable, Boolean>> dSamples= dsamp.sample(dm);
		Set<Map<IConfigurable, Boolean>> fSamples=fsamp.sample(fm);
		assertTrue(fSamples.containsAll(dSamples));
		
        // TODO way too many configurations after transformation... FIXME please
	}

}
