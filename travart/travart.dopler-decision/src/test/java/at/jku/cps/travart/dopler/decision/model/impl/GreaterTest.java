package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class GreaterTest {
	private Greater g;
	
	@Before
	public void setUp() throws Exception {
		g=new Greater(ICondition.FALSE,ICondition.TRUE);
	}

	@Test
	public void testGreaterGetValueFunctionAndARangeValue() throws RangeValueException {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		ed.setValue(s);
		GetValueFunction gvf=new GetValueFunction(ed);
		g=new Greater(gvf,s);
		assertFalse("StringValue s is same as StringValue s",g.evaluate());
	}
	
	@Test
	public void testGreaterGetValueFunctionAndGetValueFunctionStrings() {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		GetValueFunction gvf1=new GetValueFunction(ed);
		GetValueFunction gvf2=new GetValueFunction(ed);
		g=new Greater(gvf1,gvf2);
		assertFalse("Both getValueFunction should return the same value.",g.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterGetValueFunctionAndARangeValue1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		g=new Greater(gvf1,d2);
		assertFalse(gvf1.getDecision().getValue() +" > " +d2+" should be false",g.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterGetValueFunctionAndARangeValue2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		g=new Greater(gvf1,d2);
		assertTrue(gvf1.getDecision().getValue() +" > " +d2+" should be True",g.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterGetValueFunctionAndARangeValue3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		g=new Greater(gvf1,d2);
		assertFalse(gvf1.getDecision().getValue() +" > " +d2+" should be false",g.evaluate());
	}
	
	@Test
	public void testGreaterARangeValueAndGetValueFunction1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		g=new Greater(d2,gvf1);
		assertTrue(d2.toString() +" > " +nd1.getValue() +" should be true",g.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterARangeValueAndGetValueFunction2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		g=new Greater(d2,gvf1);
		assertFalse(d2.toString() +" > " +nd1.getValue() + " should be false",g.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterARangeValueAndGetValueFunction3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		g=new Greater(d2,gvf1);
		assertFalse(d2 +" > " +gvf1.getDecision().getValue()+" should be false",g.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterGetValueFunctionAndGetValueFunctionNumbers1() throws RangeValueException {
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
		g=new Greater(gvf1,gvf2);
		assertFalse(gvf1.getDecision().getValue() +" > " +gvf2.getDecision().getValue()+" should be false",g.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testGreaterGetValueFunctionAndGetValueFunctionNumbers2() throws RangeValueException {
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
		g=new Greater(gvf2,gvf1);
		assertTrue(gvf1.getDecision().getValue() +" > " +gvf2.getDecision().getValue()+" should be true",g.evaluate());
	}

	@Test
	public void testEvaluateFalseTrue() {
		assertFalse("Returns false because there is no hierarchy between true and false",g.evaluate());
	}
	
	@Test
	public void testEvaluateTrueFalse() {
		g= new Greater(ICondition.TRUE,ICondition.FALSE);
		assertFalse("Returns false because there is no hierarchy between true and false",g.evaluate());
	}
	
	@Test
	public void testEvaluateFalseFalse() {
		g= new Greater(ICondition.FALSE,ICondition.FALSE);
		assertFalse("Should return false, because two false are the same!",g.evaluate());
	}
	
	@Test
	public void testEvaluateTrueTrue() {
		g= new Greater(ICondition.TRUE,ICondition.TRUE);
		assertFalse("Should return false, because two true are the same!",g.evaluate());
	}
	
	@Test(expected= NullPointerException.class)
	public void testGreaterNullFalse() {
		g= new Greater(null,ICondition.FALSE);
	}
	
	@Test(expected= NullPointerException.class)
	public void testGreaterFalseNull() {
		g= new Greater(ICondition.FALSE,null);
	}

	@Test
	public void testToStringFalseTrue() {
		assertEquals("Text should be identical", ICondition.FALSE+" > "+ICondition.TRUE,g.toString());
	}

}
