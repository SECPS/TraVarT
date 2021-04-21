package at.jku.cps.travart.dopler.transformation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.decision.factory.impl.DecisionModelFactory;
import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

public class DecisionModeltoFeatureModelConverterTest {
	private DecisionModelFactory factory;
	private DecisionModel dm;

	@Before
	public void setUp() throws Exception {
		factory = DecisionModelFactory.getInstance();
		dm = factory.create();
		dm.setName("TestModel");
	}

	@Test
	public void testTransformOrDecision() throws NotSupportedVariablityTypeException {
		EnumDecision ed1 = new EnumDecision("ed1");
		StringValue sv1 = new StringValue("sv1");
		StringValue sv2 = new StringValue("sv2");
		StringValue sv3 = new StringValue("sv3");
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1, 3));
		dm.add(ed1);
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel fm = conv.transform(dm);
		Collection<IFeature> f = fm.getFeatures();
		IFeature theFeature = f.stream().filter(e -> e.getName().equals(ed1.getId().substring(2))).findAny().get();
		assertTrue("Multiple selections should be allowed for Or.", theFeature.getStructure().isMultiple());
		assertFalse("And should not be set for Or connection", theFeature.getStructure().isAnd());
		assertTrue(theFeature.getStructure().getChildren().size() == 3);
	}

	@Test
	public void testTransformAlternative() throws NotSupportedVariablityTypeException {
		EnumDecision ed1 = new EnumDecision("ed1");
		StringValue sv1 = new StringValue("sv1");
		StringValue sv2 = new StringValue("sv2");
		StringValue sv3 = new StringValue("sv3");
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1, 1));
		dm.add(ed1);
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel controlModel = new FeatureModel("TestModel");

		IFeature cmed1 = new Feature(controlModel, "ed1");
		FeatureUtils.setRoot(controlModel, cmed1);
		FeatureUtils.setMandatory(cmed1, true);
		FeatureUtils.setAlternative(cmed1);
		IFeature cmsv1 = new Feature(controlModel, "sv1");
		IFeature cmsv2 = new Feature(controlModel, "sv2");
		IFeature cmsv3 = new Feature(controlModel, "sv3");

		FeatureUtils.addChild(cmed1, cmsv1);
		FeatureUtils.addChild(cmed1, cmsv2);
		FeatureUtils.addChild(cmed1, cmsv3);

		FeatureUtils.addFeature(controlModel, cmed1);
		FeatureUtils.addFeature(controlModel, cmsv1);
		FeatureUtils.addFeature(controlModel, cmsv2);
		FeatureUtils.addFeature(controlModel, cmsv3);
		IFeatureModel fm = conv.transform(dm);
		System.out.println("fm Id: " + fm.getId());
		System.out.println("controlModel Id: " + controlModel.getId());
		
		
		assertTrue(areFMEqual(controlModel,fm));

	}

	@Test
	public void testTransformOrFullModelCheck() throws NotSupportedVariablityTypeException {
		EnumDecision ed1 = new EnumDecision("ed1");
		StringValue sv1 = new StringValue("sv1");
		StringValue sv2 = new StringValue("sv2");
		StringValue sv3 = new StringValue("sv3");
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1, 3));
		dm.add(ed1);
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel controlModel = new FeatureModel("TestModel");

		IFeature cmed1 = new Feature(controlModel, "ed1");
		FeatureUtils.setRoot(controlModel, cmed1);
		IFeature cmsv1 = new Feature(controlModel, "sv1");
		IFeature cmsv2 = new Feature(controlModel, "sv2");
		IFeature cmsv3 = new Feature(controlModel, "sv3");

		FeatureUtils.addChild(cmed1, cmsv1);
		FeatureUtils.addChild(cmed1, cmsv2);
		FeatureUtils.addChild(cmed1, cmsv3);

		FeatureUtils.addFeature(controlModel, cmed1);
		FeatureUtils.addFeature(controlModel, cmsv1);
		FeatureUtils.addFeature(controlModel, cmsv2);
		FeatureUtils.addFeature(controlModel, cmsv3);
		FeatureUtils.setOr(cmed1);
		IFeatureModel fm = conv.transform(dm);
			
		assertTrue(areFMEqual(controlModel,fm));
	}
	@Test
	public void testTransformOrFullModelCheck2Enums() throws NotSupportedVariablityTypeException {
		EnumDecision ed1 = new EnumDecision("ed1");
		EnumDecision ed2 = new EnumDecision("ed2");
		
		StringValue sv1 = new StringValue("sv1");
		StringValue sv2 = new StringValue("sv2");
		StringValue sv3 = new StringValue("sv3");
		ed2.getRange().add(sv1);
		ed2.getRange().add(ed2.getNoneOption());
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1, 3));
		dm.add(ed1);
		dm.add(ed2);
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel controlModel = new FeatureModel("TestModel");
		//more than one decision requires virtual root
		IFeature vr = new Feature(controlModel, "VIRTUAL_ROOT");
		FeatureUtils.setOr(vr);
		IFeature cmed1 = new Feature(controlModel, "ed1");
		IFeature cmed2 = new Feature(controlModel, "ed2");
		FeatureUtils.setRoot(controlModel, vr);
		IFeature cmsv1 = new Feature(controlModel, "sv1");
		IFeature cmsv2 = new Feature(controlModel, "sv2");
		IFeature cmsv3 = new Feature(controlModel, "sv3");

		
		FeatureUtils.addChild(vr, cmed1);
		FeatureUtils.addChild(vr, cmed2);
		
		FeatureUtils.addChild(cmed1, cmsv1);
		FeatureUtils.addChild(cmed1, cmsv2);
		FeatureUtils.addChild(cmed1, cmsv3);
		
		FeatureUtils.addChild(cmed2, cmsv1);
		

		FeatureUtils.addFeature(controlModel, vr);
		FeatureUtils.addFeature(controlModel, cmed1);
		FeatureUtils.addFeature(controlModel, cmed2);
		FeatureUtils.addFeature(controlModel, cmsv1);
		FeatureUtils.addFeature(controlModel, cmsv2);
		FeatureUtils.addFeature(controlModel, cmsv3);
		FeatureUtils.setOr(cmed1);
		IFeatureModel fm = conv.transform(dm);
		
		assertTrue(areFMEqual(controlModel,fm));
	}
	
	@Test
	public void testTransformBooleanDecision() throws NotSupportedVariablityTypeException {
		IFeatureModel controlModel = new FeatureModel("TestModel");
		BooleanDecision bd1=new BooleanDecision("bd1");
		dm.add(bd1);
		
		IFeature cmbd1= new Feature(controlModel, "bd1");
		FeatureUtils.addFeature(controlModel, cmbd1);
		
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel fm=conv.transform(dm);
		assertTrue(areFMEqual(controlModel,fm));
	}
	
	@Test
	public void testTransformNumberDecision() throws NotSupportedVariablityTypeException {
		IFeatureModel controlModel = new FeatureModel("TestModel");
		NumberDecision bd1=new NumberDecision("nd1");
		DoubleValue dv1=new DoubleValue(1);
		DoubleValue dv2=new DoubleValue(2);
		DoubleValue dv3=new DoubleValue(3);
		bd1.getRange().add(dv1);
		bd1.getRange().add(dv2);
		bd1.getRange().add(dv3);
		dm.add(bd1);
		
		IFeature cmbd1= new Feature(controlModel, "nd1");
		
		IFeature cmdv1 = new Feature(controlModel, "nd1_"+dv1.getValue().toString());
		IFeature cmdv2 = new Feature(controlModel, "nd1_"+dv2.getValue().toString());
		IFeature cmdv3 = new Feature(controlModel, "nd1_"+dv3.getValue().toString());
		
		FeatureUtils.addChild(cmbd1, cmdv1);
		FeatureUtils.addChild(cmbd1, cmdv2);
		FeatureUtils.addChild(cmbd1, cmdv3);
		FeatureUtils.addFeature(controlModel, cmdv1);
		FeatureUtils.addFeature(controlModel, cmdv2);
		FeatureUtils.addFeature(controlModel, cmdv3);

		FeatureUtils.addFeature(controlModel, cmbd1);
		//Numberdecisions are always alternative
		FeatureUtils.setAlternative(cmbd1);
		
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel fm=conv.transform(dm);
		assertTrue(areFMEqual(controlModel,fm));
	}
	
	@Test
	public void testTransformStringDecision() throws NotSupportedVariablityTypeException {
		IFeatureModel controlModel = new FeatureModel("TestModel");
		StringDecision sd1=new StringDecision("sd1");
		dm.add(sd1);
		IFeature cmsd1= new Feature(controlModel, "sd1");
		FeatureUtils.addFeature(controlModel, cmsd1);
		
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel fm=conv.transform(dm);
		assertTrue(areFMEqual(controlModel,fm));
	}
	
	@Test
	public void testTransformMandatoryVisibilityCondition() throws NotSupportedVariablityTypeException {
		EnumDecision ed1 = new EnumDecision("ed1");
		EnumDecision ed2 = new EnumDecision("ed2");
		
		StringValue sv1 = new StringValue("sv1");
		StringValue sv2 = new StringValue("sv2");
		StringValue sv3 = new StringValue("sv3");
		ed2.getRange().add(sv1);
		ed2.getRange().add(ed2.getNoneOption());
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1, 3));
		And a=new And(ICondition.FALSE,ed2);
		ed1.setVisibility(a);
		dm.add(ed1);
		dm.add(ed2);
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel controlModel = new FeatureModel("TestModel");
		IFeature cmed1 = new Feature(controlModel, "ed1");
		IFeature cmed2 = new Feature(controlModel, "ed2");
		IFeature cmsv1 = new Feature(controlModel, "sv1");
		IFeature cmsv2 = new Feature(controlModel, "sv2");
		IFeature cmsv3 = new Feature(controlModel, "sv3");

		FeatureUtils.setMandatory(cmed1, true);
		
		FeatureUtils.addChild(cmed1, cmsv1);
		FeatureUtils.addChild(cmed1, cmsv2);
		FeatureUtils.addChild(cmed1, cmsv3);
		
		FeatureUtils.addChild(cmed2, cmsv1);
		
		FeatureUtils.addFeature(controlModel, cmed1);
		FeatureUtils.addFeature(controlModel, cmed2);
		FeatureUtils.addFeature(controlModel, cmsv1);
		FeatureUtils.addFeature(controlModel, cmsv2);
		FeatureUtils.addFeature(controlModel, cmsv3);
		FeatureUtils.setOr(cmed1);
		IFeatureModel fm = conv.transform(dm);
		
		assertTrue(areFMEqual(controlModel,fm));
	}
	
	@Test
	public void testTransformDecisionVisibilityCondition() throws NotSupportedVariablityTypeException {
		EnumDecision ed1 = new EnumDecision("ed1");
		EnumDecision ed2 = new EnumDecision("ed2");
		BooleanDecision bd1=new BooleanDecision("bd1");
		
		StringValue sv1 = new StringValue("sv1");
		StringValue sv2 = new StringValue("sv2");
		StringValue sv3 = new StringValue("sv3");
		ed2.getRange().add(sv1);
		ed2.getRange().add(ed2.getNoneOption());
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1, 3));
		ed1.setVisibility(bd1);
		dm.add(ed1);
		dm.add(ed2);
		dm.add(bd1);
		dm.setAddPrefix(true);
		DecisionModeltoFeatureModelConverter conv = new DecisionModeltoFeatureModelConverter();

		IFeatureModel controlModel = new FeatureModel("TestModel");
		IFeature vr = new Feature(controlModel, "VIRTUAL_ROOT");
		FeatureUtils.setOr(vr);
		
		IFeature cmed1 = new Feature(controlModel, "ed1");
		IFeature cmed2 = new Feature(controlModel, "ed2");
		IFeature cmbd1 = new Feature(controlModel, "bd1");
		IFeature cmsv1 = new Feature(controlModel, "sv1");
		IFeature cmsv2 = new Feature(controlModel, "sv2");
		IFeature cmsv3 = new Feature(controlModel, "sv3");
		
		FeatureUtils.setRoot(controlModel, vr);
		FeatureUtils.addChild(vr, cmed1);
		FeatureUtils.addChild(vr, cmed2);
		FeatureUtils.addChild(vr, cmbd1);
		
		FeatureUtils.addChild(cmed1, cmsv1);
		FeatureUtils.addChild(cmed1, cmsv2);
		FeatureUtils.addChild(cmed1, cmsv3);
		
		FeatureUtils.addChild(cmed2, cmsv1);

		FeatureUtils.addFeature(controlModel, vr );
		FeatureUtils.addFeature(controlModel, cmbd1);
		FeatureUtils.addFeature(controlModel, cmed1);
		FeatureUtils.addFeature(controlModel, cmed2);
		FeatureUtils.addFeature(controlModel, cmsv1);
		FeatureUtils.addFeature(controlModel, cmsv2);
		FeatureUtils.addFeature(controlModel, cmsv3);
		FeatureUtils.setOr(cmed1);
		IFeatureModel fm = conv.transform(dm);
		
		assertTrue(areFMEqual(controlModel,fm));
	}

	private boolean areFMEqual(IFeatureModel fm1, IFeatureModel fm2) {
		if (!fm1.getConstraints().equals(fm2.getConstraints()))
			return false;
		if (!fm1.isFeatureOrderUserDefined() == fm2.isFeatureOrderUserDefined())
			return false;
		if (!areFeatureTablesEqual(fm1.getFeatureTable(),fm2.getFeatureTable()))
			return false;
		if (!fm1.getProperty().equals(fm2.getProperty()))
			return false;
		if(!FeatureUtils.getConstraints(fm1).equals(FeatureUtils.getConstraints(fm2)))
			return false;
		return true;
	}
	
	private boolean areFeatureTablesEqual(Map<String,IFeature> m1,Map<String,IFeature> m2) {
		if(m1.keySet().size() != m2.keySet().size()) {
			return false;
		}
		if(!m1.keySet().containsAll(m2.keySet())) {
			return false;
		}
		for(String k:m1.keySet()) {
			if(!areIFeaturesEqual(m1.get(k),m2.get(k))) {
				return false;
			}
		}		
		return true;
	}

	private boolean areIFeaturesEqual(IFeature f1, IFeature f2) {
		if (!f1.getName().equals(f2.getName()))
			return false;
		if(FeatureUtils.isOr(f1)!=FeatureUtils.isOr(f2)) return false;
		if(FeatureUtils.isAlternative(f1)!=FeatureUtils.isAlternative(f2)) return false;
		return true;
	}

}
