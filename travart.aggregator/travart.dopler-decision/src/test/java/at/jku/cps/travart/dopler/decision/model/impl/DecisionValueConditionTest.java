package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class DecisionValueConditionTest {
	private DecisionValueCondition dvc;
	private BooleanDecision bd;

	@Before
	public void setUp() throws Exception {
		bd= new BooleanDecision("test");
		bd.setValue(BooleanValue.getTrue());
		dvc= new DecisionValueCondition(bd, BooleanValue.getTrue());
	}

	@Test(expected=NullPointerException.class)
	public void testDecisionValueConditionNullARangeValue() {
		dvc=new DecisionValueCondition(null,BooleanValue.getTrue());
	}
	
	@Test(expected=NullPointerException.class)
	public void testDecisionValueConditionIDecisionNull() {
		dvc=new DecisionValueCondition(bd,null);
	}

	@Test
	public void testGetDecision() {
		assertEquals(bd,dvc.getDecision());
	}

	@Test
	public void testGetValue() {
		assertEquals(BooleanValue.getTrue(),dvc.getValue());
	}

	@Test
	public void testEvaluateEquals() {
		assertTrue(dvc.evaluate());
	}
	
	@Test
	public void testEvaluateNotEquals() throws RangeValueException {
		bd.setValue(BooleanValue.getFalse());
		assertFalse(dvc.evaluate());
	}
	
	@Test
	public void testEvaluateNoValueSet() throws RangeValueException {
		StringDecision sd= new StringDecision("stest");
		dvc=new DecisionValueCondition(sd,new StringValue("AValue"));
		assertFalse(dvc.evaluate());
	}

	@Test
	public void testToString() {
		assertEquals(dvc.getDecision()+"."+dvc.getValue(),dvc.toString());
	}

}
