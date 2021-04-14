package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class GreaterEqualsTest {
	private GreaterEquals ge;
	
	@Before
	public void setUp() throws Exception {
		ge=new GreaterEquals(ICondition.FALSE,ICondition.TRUE);
	}

	@Test
	public void testGreaterEqualsGetValueFunctionAndARangeValue() throws RangeValueException {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		ed.setValue(s);
		GetValueFunction gvf=new GetValueFunction(ed);
		ge=new GreaterEquals(gvf,s);
		assertTrue("StringValue s is same as StringValue s",ge.evaluate());
	}
	
	@Test
	public void testGreaterEqualsGetValueFunctionAndGetValueFunctionStrings() {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		GetValueFunction gvf1=new GetValueFunction(ed);
		GetValueFunction gvf2=new GetValueFunction(ed);
		ge=new GreaterEquals(gvf1,gvf2);
		assertTrue("Both getValueFunction should return the same value.",ge.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterEqualsGetValueFunctionAndARangeValue1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		ge=new GreaterEquals(gvf1,d2);
		assertFalse("Numberdecision has a lower value than value2 therefore should be false.",ge.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterEqualsGetValueFunctionAndARangeValue2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		ge=new GreaterEquals(gvf1,d2);
		assertTrue("Numberdecision has a higher value than value2 therefore should be true.",ge.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterEqualsGetValueFunctionAndARangeValue3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		ge=new GreaterEquals(gvf1,d2);
		assertTrue("Numberdecision is equal to value2 therefore should be true.",ge.evaluate());
	}
	
	@Test
	public void testGreaterEqualsARangeValueAndGetValueFunction1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		ge=new GreaterEquals(d2,gvf1);
		assertTrue(d2.toString() +"should be >= " +nd1.getValue(),ge.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterEqualsARangeValueAndGetValueFunction2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		ge=new GreaterEquals(d2,gvf1);
		assertFalse(d2.toString() +"should not be >= " +nd1.getValue(),ge.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterEqualsARangeValueAndGetValueFunction3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		ge=new GreaterEquals(d2,gvf1);
		assertTrue("The NumberValue is equal to the decision value, therefore should be true.",ge.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterEqualsGetValueFunctionAndGetValueFunctionNumbers1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		NumberDecision nd2=new NumberDecision("testDecision2");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd2.getRange().add(d2);
		nd1.setValue(d1);
		nd2.setValue(d2);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		GetValueFunction gvf2=new GetValueFunction(nd2);
		ge=new GreaterEquals(gvf1,gvf2);
		assertFalse("Decision1 has a lower value than decision2 and should therefore return false.",ge.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterEqualsGetValueFunctionAndGetValueFunctionNumbers2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		NumberDecision nd2=new NumberDecision("testDecision2");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd2.getRange().add(d2);
		nd1.setValue(d1);
		nd2.setValue(d2);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		GetValueFunction gvf2=new GetValueFunction(nd2);
		ge=new GreaterEquals(gvf2,gvf1);
		assertTrue("Decision2 has a lower value than decision1 and should therefore return true.",ge.evaluate());
	}

	@Test
	public void testEvaluateFalseTrue() {
		assertFalse("Returns false because there is no hierarchy between true and false",ge.evaluate());
	}
	
	@Test
	public void testEvaluateTrueFalse() {
		ge= new GreaterEquals(ICondition.TRUE,ICondition.FALSE);
		assertFalse("Returns false because there is no hierarchy between true and false",ge.evaluate());
	}
	
	@Test
	public void testEvaluateFalseFalse() {
		ge= new GreaterEquals(ICondition.FALSE,ICondition.FALSE);
		assertTrue("Should return true, because two false are the same!",ge.evaluate());
	}
	
	@Test
	public void testEvaluateTrueTrue() {
		ge= new GreaterEquals(ICondition.TRUE,ICondition.TRUE);
		assertTrue("Should return true, because two true are the same!",ge.evaluate());
	}
	
	@Test(expected= NullPointerException.class)
	public void testGreaterEqualsNullFalse() {
		ge= new GreaterEquals(null,ICondition.FALSE);
	}
	
	@Test(expected= NullPointerException.class)
	public void testGreaterEqualsFalseNull() {
		ge= new GreaterEquals(ICondition.FALSE,null);
	}

	@Test
	public void testToStringFalseTrue() {
		assertEquals("Text should be identical", ICondition.FALSE+" >= "+ICondition.TRUE,ge.toString());
	}

}
