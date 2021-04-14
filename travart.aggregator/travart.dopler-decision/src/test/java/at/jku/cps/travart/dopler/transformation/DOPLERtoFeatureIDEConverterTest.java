package at.jku.cps.travart.dopler.transformation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

public class DOPLERtoFeatureIDEConverterTest {
	private DecisionModelFactory factory;
	private DecisionModel dm;

	@Before
	public void setUp() throws Exception {
		factory = DecisionModelFactory.getInstance();
		dm = factory.create();
		dm.setName("TestModel");
//		List<EnumDecision> enumList = new LinkedList<>();
//		for (int i = 0; i < 3; i++) {
//			enumList.add(new EnumDecision("enum" + i));
//		}
//		enumList.get(0).setCardinality(new Cardinality(2, 3));
//		List<StringValue> svals = new LinkedList<>();
//		for (int i = 0; i < 3; i++) {
//			svals.add(new StringValue("value" + i));
//		}
//		Range<String> r = new Range<>();
//		r.addAll(svals);
//		enumList.get(0).setRange(r);
//
//		BooleanDecision bd1 = new BooleanDecision("bd1");
//		NumberDecision nd1 = new NumberDecision("bd1");
//		DoubleValue dv1=new DoubleValue(2);
//		nd1.getRange().add(dv1);
//		StringDecision sd1 = new StringDecision("sd1");
//		
//		EnumDecision ed1= new EnumDecision("ed1");
//		EnumDecision ed2= new EnumDecision("ed2");
//		EnumDecision ed3= new EnumDecision("ed3");
//		
//		//alternative
//		ed1.setCardinality(new Cardinality(1,1));
//		StringValue sv1=new StringValue("sv1");
//		StringValue sv2=new StringValue("sv2");
//		StringValue sv3=new StringValue("sv3");
//		StringValue sv4=new StringValue("sv4");
//		StringValue sv5=new StringValue("sv5");
//		StringValue sv6=new StringValue("sv6");
//		
//		
//		
//		ed3.getRange().add(sv6);
//		ed3.setValue(sv6);
//		And a=new And(ICondition.FALSE,bd1);
//		ed3.setVisibility(a);
//		
//		ed2.setVisibility(bd1);
//		
//		
//		ed1.getRange().add(sv1);
//		ed1.getRange().add(sv2);
//
//		ed2.getRange().add(sv3);
//		ed2.getRange().add(sv4);
//		ed2.getRange().add(sv5);
//				
//		//orConstraint
//		ed2.setCardinality(new Cardinality(1,3));
//		
//		
//		dm.addAll(enumList);
//		dm.add(bd1);
//		dm.add(nd1);
//		dm.add(sd1);
//		dm.add(ed1);
//		dm.add(ed2);
//		dm.add(ed3);
	}
	
	@Test
	public void testTransformOrDecision() throws NotSupportedVariablityTypeException {
		EnumDecision ed1=new EnumDecision("ed1");
		StringValue sv1=new StringValue("sv1");
		StringValue sv2=new StringValue("sv2");
		StringValue sv3=new StringValue("sv3");
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1,3));
		dm.add(ed1);
		DecisionModeltoFeatureModelConverter conv=new DecisionModeltoFeatureModelConverter();
		
		IFeatureModel fm=conv.transform(dm);
		Collection<IFeature> f=fm.getFeatures();
		IFeature theFeature=f.stream().filter(e-> e.getName().equals(ed1.getId().substring(2))).findAny().get();
		assertTrue("Multiple selections should be allowed for Or.",theFeature.getStructure().isMultiple());
		assertFalse("And should not be set for Or connection",theFeature.getStructure().isAnd());
		assertTrue(theFeature.getStructure().getChildren().size()==3);
	}
	
	@Test
	public void testTransformAlternative() throws NotSupportedVariablityTypeException {
		EnumDecision ed1=new EnumDecision("ed1");
		StringValue sv1=new StringValue("sv1");
		StringValue sv2=new StringValue("sv2");
		StringValue sv3=new StringValue("sv3");
		ed1.getRange().add(sv1);
		ed1.getRange().add(sv2);
		ed1.getRange().add(sv3);
		ed1.setCardinality(new Cardinality(1,1));
		dm.add(ed1);
		DecisionModeltoFeatureModelConverter conv=new DecisionModeltoFeatureModelConverter();
		
		IFeatureModel fm=conv.transform(dm);
	}

	@Test
	public void testTransform() throws NotSupportedVariablityTypeException {
		DecisionModeltoFeatureModelConverter conv=new DecisionModeltoFeatureModelConverter();
		conv.transform(dm);
	}

}
