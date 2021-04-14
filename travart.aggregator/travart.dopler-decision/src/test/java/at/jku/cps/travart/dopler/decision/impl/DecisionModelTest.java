package at.jku.cps.travart.dopler.decision.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.factory.impl.DecisionModelFactory;
import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.IAction;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class DecisionModelTest {
	private DecisionModelFactory factory;
	private DecisionModel dm;

	@Before
	public void setUp() throws Exception {
		factory = DecisionModelFactory.getInstance();
		dm = factory.create();
		dm.setName("TestModel");
	}

	@Test(expected=NullPointerException.class)
	public void testDecisionModel() {
		dm = new DecisionModel(null);
	}

	@Test
	public void testGetName() {
		assertEquals("TestModel", dm.getName());
	}

	@Test
	public void testGetDecisionNames() {
		// tests if all decision names are returned.
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		dm.add(d1);
		dm.add(d2);
		assertTrue(DecisionModelUtils.getDecisionNames(dm).contains(d1.getId())
				&& DecisionModelUtils.getDecisionNames(dm).contains(d2.getId()));
	}

	@Test
	public void testGetBooleanDecisionsAsNames() {
		// tests if the name of the booleanDecision is returned, but not the name
		// of the enumdecision
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		dm.add(d1);
		dm.add(d2);
		assertTrue(DecisionModelUtils.getBooleanDecisionsAsNames(dm).contains(d1.getId())
				&& !DecisionModelUtils.getBooleanDecisionsAsNames(dm).contains(d2.getId()));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testGetBooleanDecisions() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		assertTrue(DecisionModelUtils.getBooleanDecisions(dm).contains(d1)
				&& DecisionModelUtils.getBooleanDecisions(dm).contains(d3)
				&& !DecisionModelUtils.getBooleanDecisions(dm).contains(d2));
	}

	@Test
	public void testGetEnumDecisionsAsNames() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		assertTrue(!DecisionModelUtils.getEnumDecisionsAsNames(dm).contains(d1.getId())
				&& !DecisionModelUtils.getEnumDecisionsAsNames(dm).contains(d3.getId())
				&& DecisionModelUtils.getEnumDecisionsAsNames(dm).contains(d2.getId()));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testGetEnumDecisions() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		assertTrue(!DecisionModelUtils.getEnumDecisions(dm).contains(d1)
				&& !DecisionModelUtils.getEnumDecisions(dm).contains(d3)
				&& DecisionModelUtils.getEnumDecisions(dm).contains(d2));
	}

	@Test
	public void testGetNumberDecisionsAsNames() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		assertTrue(!DecisionModelUtils.getNumberDecisionsAsNames(dm).contains(d1.getId())
				&& !DecisionModelUtils.getNumberDecisionsAsNames(dm).contains(d3.getId())
				&& !DecisionModelUtils.getNumberDecisionsAsNames(dm).contains(d2.getId())
				&& DecisionModelUtils.getNumberDecisionsAsNames(dm).contains(d4.getId()));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testGetNumberDecisions() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		assertTrue(!DecisionModelUtils.getNumberDecisions(dm).contains(d1)
				&& !DecisionModelUtils.getNumberDecisions(dm).contains(d3)
				&& !DecisionModelUtils.getNumberDecisions(dm).contains(d2)
				&& DecisionModelUtils.getNumberDecisions(dm).contains(d4));
	}

	@Test
	public void testGetStringDecisionsAsNames() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		assertTrue(DecisionModelUtils.getStringDecisionsAsNames(dm).contains(d5.getId())
				&& !DecisionModelUtils.getStringDecisionsAsNames(dm).contains(d1.getId())
				&& !DecisionModelUtils.getStringDecisionsAsNames(dm).contains(d3.getId())
				&& !DecisionModelUtils.getStringDecisionsAsNames(dm).contains(d2.getId())
				&& !DecisionModelUtils.getStringDecisionsAsNames(dm).contains(d4.getId()));

	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testGetStringDecisions() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		assertTrue(DecisionModelUtils.getStringDecisions(dm).contains(d5)
				&& !DecisionModelUtils.getStringDecisions(dm).contains(d1)
				&& !DecisionModelUtils.getStringDecisions(dm).contains(d3)
				&& !DecisionModelUtils.getStringDecisions(dm).contains(d2)
				&& !DecisionModelUtils.getStringDecisions(dm).contains(d4));
	}

	@Test
	public void testGetSelectableDecisionsAsNames() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		assertTrue(DecisionModelUtils.getSelectableDecisionsAsNames(dm).contains(d1.getId())
				&& DecisionModelUtils.getSelectableDecisionsAsNames(dm).contains(d2.getId())
				&& DecisionModelUtils.getSelectableDecisionsAsNames(dm).contains(d3.getId())
				&& DecisionModelUtils.getSelectableDecisionsAsNames(dm).contains(d4.getId())
				&& DecisionModelUtils.getSelectableDecisionsAsNames(dm).contains(d5.getId()));
	}

	@Test
	public void testGetSelectableDecisions() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		assertTrue(DecisionModelUtils.getSelectableDecisions(dm).contains(d1)
				&& DecisionModelUtils.getSelectableDecisions(dm).contains(d2)
				&& DecisionModelUtils.getSelectableDecisions(dm).contains(d3)
				&& DecisionModelUtils.getSelectableDecisions(dm).contains(d4)
				&& DecisionModelUtils.getSelectableDecisions(dm).contains(d5));
	}

	@Test
	public void testGetSelectedDecisionsAsNames() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		d2.setSelected(true);
		d4.setSelected(true);
		d5.setSelected(true);
		assertTrue(!DecisionModelUtils.getSelectedDecisionsAsNames(dm).contains(d1.getId())
				&& DecisionModelUtils.getSelectedDecisionsAsNames(dm).contains(d2.getId())
				&& !DecisionModelUtils.getSelectedDecisionsAsNames(dm).contains(d3.getId())
				&& DecisionModelUtils.getSelectedDecisionsAsNames(dm).contains(d4.getId())
				&& DecisionModelUtils.getSelectedDecisionsAsNames(dm).contains(d5.getId()));
	}

	@Test
	public void testGetSelectedDecisions() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		d2.setSelected(true);
		d4.setSelected(true);
		d5.setSelected(true);
		assertTrue(!DecisionModelUtils.getSelectedDecisions(dm).contains(d1)
				&& DecisionModelUtils.getSelectedDecisions(dm).contains(d2)
				&& !DecisionModelUtils.getSelectedDecisions(dm).contains(d3)
				&& DecisionModelUtils.getSelectedDecisions(dm).contains(d4)
				&& DecisionModelUtils.getSelectedDecisions(dm).contains(d5));
	}

	@Test
	public void testGetReachableDecisions() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		d4.setVisibility(ICondition.FALSE);
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		d2.setSelected(true);
		d4.setSelected(true);
		d5.setSelected(true);
		assertTrue(DecisionModelUtils.getReachableDecisions(dm).contains(d1)
				&& !DecisionModelUtils.getReachableDecisions(dm).contains(d2)
				&& DecisionModelUtils.getReachableDecisions(dm).contains(d3)
				&& !DecisionModelUtils.getReachableDecisions(dm).contains(d4)
				&& !DecisionModelUtils.getReachableDecisions(dm).contains(d5));
	}

	@Test
	public void testGetReachableDecisionsAsNames() {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		d4.setVisibility(ICondition.FALSE);
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		d2.setSelected(true);
		d4.setSelected(true);
		d5.setSelected(true);
		assertTrue(DecisionModelUtils.getReachableDecisionsAsNames(dm).contains(d1.getId())
				&& !DecisionModelUtils.getReachableDecisionsAsNames(dm).contains(d2.getId())
				&& DecisionModelUtils.getReachableDecisionsAsNames(dm).contains(d3.getId())
				&& !DecisionModelUtils.getReachableDecisionsAsNames(dm).contains(d4.getId())
				&& !DecisionModelUtils.getReachableDecisionsAsNames(dm).contains(d5.getId()));
	}

	@Test
	public void testHasReachableDecisions() {
		assertFalse(DecisionModelUtils.hasReachableDecisions(dm));
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		BooleanDecision d3 = new BooleanDecision("btest2");
		NumberDecision d4 = new NumberDecision("ntest");
		StringDecision d5 = new StringDecision("setest");
		d4.setVisibility(ICondition.FALSE);
		dm.add(d1);
		dm.add(d2);
		dm.add(d3);
		dm.add(d4);
		dm.add(d5);
		d2.setSelected(true);
		d4.setSelected(true);
		d5.setSelected(true);
		assertTrue(DecisionModelUtils.hasReachableDecisions(dm));
	}

//	@Test
//	public void testIsRoot() {
//		BooleanDecision d1 = new BooleanDecision("btest");
//		EnumDecision d2 = new EnumDecision("test");
//		BooleanDecision d3 = new BooleanDecision("btest2");
//		NumberDecision d4 = new NumberDecision("ntest");
//		StringDecision d5 = new StringDecision("setest");
//		d2.setVisibility(ICondition.FALSE);
//		d4.setVisibility(ICondition.FALSE);
//		dm.add(d1);
//		dm.add(d2);
//		dm.add(d3);
//		dm.add(d4);
//		dm.add(d5);
//		assertTrue(dm.isRoot(d1));
//		assertFalse(dm.isRoot(d2));
//		assertTrue(dm.isRoot(d3));
//		assertFalse(dm.isRoot(d4));
//		assertTrue(dm.isRoot(d5));
//	}
//
//	@Test
//	public void testGetRoots() {
//		BooleanDecision d1 = new BooleanDecision("btest");
//		EnumDecision d2 = new EnumDecision("test");
//		BooleanDecision d3 = new BooleanDecision("btest2");
//		NumberDecision d4 = new NumberDecision("ntest");
//		StringDecision d5 = new StringDecision("setest");
//		d2.setVisibility(ICondition.FALSE);
//		d4.setVisibility(ICondition.FALSE);
//		dm.add(d1);
//		dm.add(d2);
//		dm.add(d3);
//		dm.add(d4);
//		dm.add(d5);
//		assertTrue(dm.getRoots().contains(d1) && !dm.getRoots().contains(d2) && dm.getRoots().contains(d3)
//				&& !dm.getRoots().contains(d4) && dm.getRoots().contains(d5));
//	}

	@Test
	public void testSetDecisionValue() throws ActionExecutionException, RangeValueException {
		BooleanDecision d1 = new BooleanDecision("btest");
		EnumDecision d2 = new EnumDecision("test");
		dm.add(d1);
		dm.add(d2);
		d1.setValue(BooleanValue.getFalse());
		d1.executeRules();
		assertEquals("BooleanDecision value should be changed to false.", BooleanValue.getFalse(), d1.getValue());
		StringValue s1 = new StringValue("myVal");
		StringValue s2 = new StringValue("myVal2");
		StringValue s3 = new StringValue("myVal3");
		Range<String> r = new Range<>();
		r.add(s1);
		r.add(s2);
		d2.setRange(r);
		d2.setValue(s2);
		d2.executeRules();
		assertNotEquals("s2 was set as value, so s1 should not be the value here.", d2.getValue(), s1);
		assertEquals("s2 is the value set by the function and should therefore be the value of d2.", d2.getValue(), s2);
		ThrowingRunnable run = () -> d2.setValue(s3);
		assertThrows("Should throw a RangeValueException, because s3 is not part of d2's range.",
				RangeValueException.class, run);
	}

//	@Test
//	public void testIsValidNotRoot() {
//		BooleanDecision b1 = new BooleanDecision("test1");
//		BooleanDecision b2 = new BooleanDecision("test2");
//		dm.add(b1);
//		assertFalse("b1 did not get added to roots yet. Should therefore fail", dm.isValid());
//		dm.add(b2);
//		// add roots
//		dm.getRoots();
//		b2.setSelected(false);
//		assertFalse("b2 is not selected and should therefore make it invalid", dm.isValid());
//	}

	@Test
	public void testIsValidNoDecisions() {
		assertFalse("a decision model with no decisions should be invalid by default", dm.isValid());
	}

	@Test
	public void testIsValidSelectedDecisionsMustBeVisible() {
		BooleanDecision b1 = new BooleanDecision("test1");
		dm.add(b1);
		b1.setSelected(true);
		b1.setVisibility(new BooleanDecision("test"));
		assertFalse("b1 is a mandatory feature, that is based on a boolean decision and should therefore yield false",
				dm.isValid());
	}

	@Test
	public void testIsValidEmptyEnumDecision() {
		EnumDecision d = new EnumDecision("test");
		dm.add(d);
		d.setSelected(true);
		assertFalse("EnumDecision has no Values and should therefore yield false.", dm.isValid());
	}

	@Test
	public void isValidAllRulesMustHold() throws RangeValueException {
		EnumDecision d = new EnumDecision("test");
		StringValue s1 = new StringValue("s1");
		StringValue s2 = new StringValue("s2");
		Range<String> r = new Range<>();
		r.add(s1);
		r.add(s2);
		d.setRange(r);
		dm.add(d);
		d.setValue(s1);
		d.setSelected(true);
		d.addRule(new Rule(ICondition.TRUE, new SetValueAction(d, new StringValue("sValue1"))));
		d.addRule(new Rule(ICondition.FALSE, new SetValueAction(d, new StringValue("sValue2"))));
		assertFalse("Cannot perform SetValueAction because d does not have that value.", dm.isValid());
	}

	@Test
	public void isValidNoRules() throws RangeValueException {
		EnumDecision d = new EnumDecision("test");
		StringValue s1 = new StringValue("s1");
		StringValue s2 = new StringValue("s2");
		Range<String> r = new Range<>();
		r.add(s1);
		r.add(s2);
		d.setRange(r);
		dm.add(d);
		d.setValue(s1);
		d.setSelected(true);
		assertTrue("DecisionModel with an EnumDecision that is set and visible, is valid.", dm.isValid());
	}

	@Test
	public void isValidAllRulesSatisfied() throws RangeValueException, ActionExecutionException {
		EnumDecision d = new EnumDecision("test");
		StringValue s1 = new StringValue("s1");
		StringValue s2 = new StringValue("s2");
		Range<String> r = new Range<>();
		r.add(s1);
		r.add(s2);
		d.setRange(r);
		dm.add(d);
		d.setValue(s1);
		d.setSelected(true);
		IAction a = new DisAllowAction(d, s2);
		d.addRule(new Rule(ICondition.TRUE, a));
		a.execute();
		assertTrue("DecisionModel with an EnumDecision that is set and visible, an all rules satisfied is valid.",
				dm.isValid());
	}

	@Test
	public void isValidActionExecutionException() throws IllegalArgumentException, RangeValueException {
		EnumDecision d = new EnumDecision("test");
		StringValue s1 = new StringValue("s1");
		StringValue s2 = new StringValue("s2");
		Range<String> r = new Range<>();
		r.add(s1);
		r.add(s2);
		d.setRange(r);
		dm.add(d);
		d.setValue(s1);
		d.setSelected(true);
		@SuppressWarnings("unused")
		IAction a = new SetValueAction(d, s2);
		ThrowingRunnable run = () -> {
			d.addRule(new Rule(ICondition.TRUE, new DisAllowAction(d, new StringValue("NotInRange"))));
		};
		assertThrows("Should throw IllegalArgumentException because value is not part of decision range.",
				IllegalArgumentException.class, run);

	}

	@Test
	public void testFindString() {
		EnumDecision d = new EnumDecision("etest");
		BooleanDecision b = new BooleanDecision("btest");
		EnumDecision dn = new EnumDecision("notContained");
		dm.add(d);
		dm.add(b);
		assertEquals("d is in the model and should be found.", d, dm.find(d.getId()));
		assertEquals("b is in the model and should be found.", b, dm.find(b.getId()));
		assertNull("dn has not been added to the model and should not be found.", dm.find(dn.getId()));
	}

	@Test
	public void testReset() throws RangeValueException {
		EnumDecision d = new EnumDecision("etest");
		Range<String> r = new Range<>();
		StringValue s1 = new StringValue("s1");
		StringValue s2 = new StringValue("s2");
		r.add(s1);
		r.add(s2);
		d.setRange(r);
		d.setValue(s1);
		BooleanDecision b = new BooleanDecision("btest");
		b.setValue(BooleanValue.getTrue());
		dm.add(d);
		dm.add(b);
		dm.reset();
		assertTrue("EnumDecision should have no values after reset.", d.getValues().isEmpty());
		assertTrue("BooleanDecision should have false as value after reset.",
				b.getValue().equals(BooleanValue.getFalse()));
	}

	@Test
	public void testToStringNoDecisions() {
		assertEquals("ToString for an empty model didn't yield expected results.",
				"DecisionModel" + dm.getName() + "[]", dm.toString());
	}

	@Test
	public void testToStringWithDecisions() throws RangeValueException {
		EnumDecision d = new EnumDecision("test");
		StringValue s1 = new StringValue("s1");
		Range<String> r = new Range<>();
		r.add(s1);
		d.setRange(r);
		dm.add(d);
		d.setValue(s1);
		d.setSelected(true);
		assertEquals(
				"ToString for a model with a decision didn't yield expected results.", "DecisionModel" + dm.getName()
						+ "[" + d.toString() + "[selected=" + d.isSelected() + "; value=" + d.getValue() + "] ]",
				dm.toString());
	}

}
