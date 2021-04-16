package at.jku.cps.travart.dopler.decision.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.UnsatisfiedCardinalityException;
import at.jku.cps.travart.dopler.decision.factory.impl.DecisionModelFactory;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class DecisionModel2Test {
	
	DecisionModel dm;
	BooleanDecision bd;
	NumberDecision nd;
	EnumDecision ed;
	StringDecision sd;
	private static final String factoryId="myFactoryId";
	private static final String modelName="DecisionModel";

	@Before
	public void setUp() throws Exception {
		dm=DecisionModelFactory.getInstance().create();
		bd=new BooleanDecision("testBool");
		nd=new NumberDecision("testNumber");
		ed=new EnumDecision("testEnum");
		sd=new StringDecision("testString");
		dm.add(bd);
		dm.add(nd);
		dm.add(ed);
		dm.add(sd);
	}

	@Test
	public void testDecisionModelString() {
		dm=new DecisionModel(factoryId);
		assertEquals(factoryId, dm.getFactoryId());
		assertEquals(modelName,dm.getName());
		assertTrue(dm.isAddPrefix());
	}
	
	@Test(expected= NullPointerException.class)
	public void testDecisionModelStringNull() {
		dm=new DecisionModel(null);
	}

	@Test
	public void testDecisionModelStringStringBoolean() {
		dm=new DecisionModel(factoryId,modelName,true);
		assertEquals(factoryId, dm.getFactoryId());
		assertEquals(modelName,dm.getName());
		assertTrue(dm.isAddPrefix());
	}
	
	@Test(expected= NullPointerException.class)
	public void testDecisionModelStringNullStringBoolean() {
		dm=new DecisionModel(null,modelName,true);
	}
	
	@Test(expected= NullPointerException.class)
	public void testDecisionModelStringStringNullBoolean() {
		dm=new DecisionModel(factoryId,null,true);
	}

	@Test
	public void testAfterValueSelectionNoRange() throws RangeValueException {
		dm.afterValueSelection(ed);
	}
	
	@Test
	public void testAfterValueSelection() throws RangeValueException {
		StringValue s1=new StringValue("svalue1");
		StringValue s2=new StringValue("svalue2");
		
		ed.getRange().add(s1);
		ed.getRange().add(s2);
		
		EnumDecision ed2=new EnumDecision("svalue1");
		StringValue sv1=new StringValue("TheChoseValue1");
		ed2.getRange().add(sv1);
		ed2.setValue(sv1);
		dm.add(ed2);
		EnumDecision ed3=new EnumDecision("svalue2");
		StringValue sv2=new StringValue("TheChoseValue2");
		ed3.getRange().add(sv2);
		ed3.setValue(sv2);
		dm.add(ed3);
		dm.afterValueSelection(ed3);
		assertEquals(s2,ed.getValue());
	}

	@Test
	public void testExecuteRules() throws ActionExecutionException {
		StringValue sv=new StringValue("aValue");
		ed.getRange().add(sv);
		bd.addRule(new Rule(ICondition.TRUE,new SetValueAction(ed,sv)));
		bd.setSelected(true);
		dm.executeRules();
		assertEquals(sv, ed.getValue());
	}

	@Test
	public void testFindPrefix() {
		assertEquals(bd,dm.find(bd.getId()));
	}
	
	@Test
	public void testFindNoPrefix() {
		dm.setAddPrefix(false);
		assertEquals(bd,dm.find(bd.getId()));
	}

	@Test
	public void testFindWithRangeValue() {
		StringValue sv=new StringValue("value");
		ed.getRange().add(sv);
		EnumDecision ed2=new EnumDecision("anotherEnum");
		dm.add(ed2);
		ed.getRange().add(new StringValue("anotherEnum"));
		assertTrue(dm.findWithRangeValue(ed2).contains(ed));
	}

	@Test
	public void testFindWithVisibility() {
		ed.setVisibility(bd);
		assertTrue(dm.findWithVisibility(bd).contains(ed));
	}
	
	@Test
	public void testFindWithVisibilityRequiredVisible1() throws RangeValueException {
		BooleanDecision bd1=new BooleanDecision("bd1");
		bd1.setValue(BooleanValue.getFalse());
		And and=new And(bd1,ICondition.FALSE);
		ed.setVisibility(and);
		assertTrue(dm.findWithVisibility(bd1).contains(ed));
	}
	
	@Test
	public void testFindWithVisibilityRequiredVisible2() throws RangeValueException {
		BooleanDecision bd1=new BooleanDecision("bd1");
		bd1.setValue(BooleanValue.getFalse());
		And and=new And(ICondition.FALSE,bd1);
		ed.setVisibility(and);
		assertTrue(dm.findWithVisibility(bd1).contains(ed));
	}
	
	@Test
	public void testFindWithVisibilityRequiredVisible3() throws RangeValueException {
		BooleanDecision bd1=new BooleanDecision("bd1");
		bd1.setValue(BooleanValue.getFalse());
		And and=new And(ICondition.FALSE,bd1);
		ed.setVisibility(and);
		assertTrue(dm.findWithVisibility(sd).isEmpty());
	}
	

	@Test
	public void testGetFactoryId() {
		assertEquals("at.jku.cps.vmt.dopler.decision.factory.DecisionModelFactory",dm.getFactoryId());
	}

	@Test
	public void testGetName() {
		assertEquals(modelName,dm.getName());
	}

	@Test
	public void testGetSourceFile() {
		assertNull(dm.getSourceFile());
	}

	@Test
	public void testIsAddPrefix() {
		assertTrue("Default behaviour should add prefix",dm.isAddPrefix());
	}

	@Test
	public void testIsValidNoSelectedDecisions() {
		assertFalse(dm.isValid());
	}
	
	@Test
	public void testIsValid1() {
		bd.setSelected(true);
		ed.setSelected(true);
		nd.setSelected(true);
		sd.setSelected(true);
		assertFalse("EnumDecision has no values in Range yet, so invalid.",dm.isValid());
		ed.getRange().add(ed.getNoneOption());
		assertTrue("EnumDecision now has a value in its Range so it's valid.",dm.isValid());
	}
	
	@Test
	public void testIsValidSelectedDecisionNotVisible() throws RangeValueException {
		bd.setSelected(true);
		ed.setSelected(true);
		nd.setSelected(true);
		sd.setSelected(true);
		ed.getRange().add(ed.getNoneOption());
		BooleanDecision bd1=new BooleanDecision("bd1");
		bd1.setValue(BooleanValue.getFalse());
		And and=new And(ICondition.FALSE,ICondition.FALSE);
		ed.setVisibility(and);
		assertFalse("EnumDecision is selected, but not visible and not mandatory, so should be invalid",dm.isValid());
	}
	
	@Test
	public void testIsValidUnselectedVisibilityDecision() throws RangeValueException {
		bd.setSelected(true);
		ed.setSelected(true);
		nd.setSelected(true);
		sd.setSelected(true);
		ed.getRange().add(ed.getNoneOption());
		BooleanDecision bd1=new BooleanDecision("bd1");
		bd1.setValue(BooleanValue.getFalse());
		bd1.setSelected(false);
		And and=new And(bd1,ICondition.FALSE);
		ed.setVisibility(and);
		assertFalse("A decision required for a visibility is not selected, therefore false",dm.isValid());
	}
	
	@Test
	public void testIsValidUnperformedDisallowActionEnumDecision() throws RangeValueException {
		bd.setSelected(true);
		ed.getRange().add(ed.getNoneOption());
		bd.addRule(new Rule(ICondition.TRUE,new DisAllowAction(ed,ed.getNoneOption())));
		
		assertFalse("A Rule's condition is met, but its Action is not satisfied.",dm.isValid());
	}
	
	@Test
	public void testIsValidDisallowActionEnumDecision() throws RangeValueException {
		bd.setSelected(true);
		ARangeValue<String> ev=ed.getNoneOption();
		StringValue sv=new StringValue("aValue");
		ed.getRange().add(ev);
		ed.getRange().add(sv);
		ed.setValue(sv);
		bd.addRule(new Rule(ICondition.TRUE,new DisAllowAction(ed,ed.getNoneOption())));
		
		assertTrue("A Rule's condition is met, and its action is satisfied.",dm.isValid());
	}
	
	@Test
	public void testIsValidUnperformedDisallowActionNumberDecision() throws RangeValueException {
		nd.setSelected(true);
		bd.setSelected(true);
		DoubleValue dv=new DoubleValue(3);
		nd.getRange().add(dv);
		bd.addRule(new Rule(ICondition.TRUE,new DisAllowAction(nd,dv)));
		
		assertFalse("A Rule's condition is met, but its Action is not satisfied.",dm.isValid());
	}
	
	@Test
	public void testIsValidDisallowActionNumberDecision() throws RangeValueException {
		nd.setSelected(true);
		bd.setSelected(true);
		DoubleValue dv=new DoubleValue(3);
		nd.getRange().add(dv);
		nd.setValue(dv);
		bd.addRule(new Rule(ICondition.TRUE,new DisAllowAction(nd,dv)));
		
		assertTrue("A Rule's condition is met, but its Action is satisfied.",dm.isValid());
	}
	
	@Test
	public void testIsValidDeSelectDecisionEnumDecision() throws RangeValueException {
		ed.setSelected(true);
		BooleanDecision bd2= new BooleanDecision("bd2");
		bd2.setSelected(true);
		bd.setSelected(true);
		StringValue dv=new StringValue("val");
		ed.getRange().add(dv);
		ed.setValue(dv);
		bd.addRule(new Rule(ICondition.TRUE,new DeSelectDecisionAction(bd2)));
		
		assertFalse("A Rule's condition is met, but its Action is not satisfied.",dm.isValid());
	}

	@Test
	public void testSetName() {
		String newName="newName";
		dm.setName(newName);
		assertEquals(newName,dm.getName());
	}

	@Test
	public void testSetSourceFile() {
		String file= "Just any string will do.";
		dm.setSourceFile(file);
		assertEquals(file,dm.getSourceFile());
	}

	@Test
	public void testGetCurrentConfigurationCorrect() {
		Map<IConfigurable,Boolean> controlMap = new HashMap<>();
		bd.setSelected(true);
		nd.setSelected(true);
		controlMap.put(bd, true);
		controlMap.put(nd, true);
		controlMap.put(ed, false);
		controlMap.put(sd, false);
		assertEquals(controlMap.toString()+ " should equal "+ dm.getCurrentConfiguration().toString(),controlMap,dm.getCurrentConfiguration());
		bd.setSelected(false);
		assertNotEquals(controlMap.toString()+ " should not equal "+ dm.getCurrentConfiguration().toString(),controlMap,dm.getCurrentConfiguration());
	}
	
	@Test
	public void testGetCurrentConfigurationIncorrect() {
		Map<IConfigurable,Boolean> controlMap = new HashMap<>();
		bd.setSelected(true);
		nd.setSelected(true);
		controlMap.put(bd, true);
		controlMap.put(nd, true);
		controlMap.put(ed, false);
		controlMap.put(sd, false);
		bd.setSelected(false);
		assertNotEquals(controlMap.toString()+ " should not equal "+ dm.getCurrentConfiguration().toString(),controlMap,dm.getCurrentConfiguration());
	}
	
	@Ignore //TODO some fine details probably break the equals check in the end. investigate
	@Test
	public void testGetCurrentConfigurationInclValuesEqual() throws RangeValueException, UnsatisfiedCardinalityException {
		Map<IConfigurable,Boolean> controlMap = new HashMap<>();
		nd.setSelected(true);
		ed.setSelected(true);
		ed.setCardinality(new Cardinality(0,2));
		StringValue[] sv= new StringValue[2];
		Set<ARangeValue<String>> edvals=new HashSet<>();
		for(int i=0;i<2;i++) {
			sv[i]=new StringValue("sv"+(i+1));
			ed.getRange().add(sv[i]);
			edvals.add(sv[i]);
		}
		ed.setValues(edvals);
		for(ARangeValue val:ed.getValues()) {
			IConfigurable configurable = new IConfigurable() {
				boolean selected;

				@Override
				public void setSelected(final boolean selected) {
					this.selected = selected;
				}

				@Override
				public boolean isSelected() {
					return selected;
				}

				@Override
				public String getName() {
					return val.toString();
				}
			};
			configurable.setSelected(ed.isSelected());
			controlMap.put(configurable, ed.isSelected());	
		}
		
		DoubleValue[] dv= new DoubleValue[2];
		for(int i=0;i<2;i++) {
			dv[i]=new DoubleValue(Double.valueOf(i));
			nd.getRange().add(dv[i]);
		}
		nd.setValue(dv[0]);
			IConfigurable configurable = new IConfigurable() {
				boolean selected;

				@Override
				public void setSelected(final boolean selected) {
					this.selected = selected;
				}

				@Override
				public boolean isSelected() {
					return selected;
				}

				@Override
				public String getName() {
					return nd+"_"+dv[0].toString();
				}
			};
			configurable.setSelected(nd.isSelected());
			controlMap.put(configurable, nd.isSelected());	
		
		controlMap.put(bd, false);
		controlMap.put(nd, true);
		controlMap.put(ed, true);
		controlMap.put(sd, false);
		assertEquals(controlMap.toString()+" should equal "+dm.getCurrentConfigurationInclValues(),controlMap,dm.getCurrentConfigurationInclValues());
		
	}

}
