package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class LessEqualsTest {
	private LessEquals le;
	
	@Before
	public void setUp() throws Exception {
		le=new LessEquals(ICondition.FALSE,ICondition.TRUE);
	}

	@Test
	public void testLessEqualsGetValueFunctionAndARangeValue() throws RangeValueException {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		ed.setValue(s);
		GetValueFunction gvf=new GetValueFunction(ed);
		le=new LessEquals(gvf,s);
		assertTrue("StringValue s is same as StringValue s",le.evaluate());
	}
	
	@Test
	public void testLessEqualsGetValueFunctionAndGetValueFunctionStrings() {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		GetValueFunction gvf1=new GetValueFunction(ed);
		GetValueFunction gvf2=new GetValueFunction(ed);
		le=new LessEquals(gvf1,gvf2);
		assertTrue("Both getValueFunction should return the same value.",le.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessEqualsGetValueFunctionAndARangeValue1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		le=new LessEquals(gvf1,d2);
		assertTrue("Numberdecision has a lower value than value2 therefore should be true.",le.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessEqualsGetValueFunctionAndARangeValue2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		le=new LessEquals(gvf1,d2);
		assertFalse("Numberdecision has a higher value than value2 therefore should be false.",le.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessEqualsGetValueFunctionAndARangeValue3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		le=new LessEquals(gvf1,d2);
		assertTrue("Numberdecision is equal to value2 therefore should be true.",le.evaluate());
	}
	
	@Test
	public void testLessEqualsARangeValueAndGetValueFunction1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		le=new LessEquals(d2,gvf1);
		assertFalse(d2.toString() +" should not be <= " +nd1.getValue(),le.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessEqualsARangeValueAndGetValueFunction2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		le=new LessEquals(d2,gvf1);
		assertTrue(d2.toString() +" should be <= " +nd1.getValue(),le.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessEqualsARangeValueAndGetValueFunction3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		le=new LessEquals(d2,gvf1);
		assertTrue(d2 +" <= "+d1 +" should be true" ,le.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessEqualsGetValueFunctionAndGetValueFunctionNumbers1() throws RangeValueException {
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
		le=new LessEquals(gvf1,gvf2);
		assertTrue(gvf1 +" <= "+ gvf2 +" should be true",le.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessEqualsGetValueFunctionAndGetValueFunctionNumbers2() throws RangeValueException {
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
		le=new LessEquals(gvf2,gvf1);
		assertFalse(gvf2+" <= "+ gvf1 +" should be false",le.evaluate());
	}

	@Test
	public void testEvaluateFalseTrue() {
		assertFalse("Returns false because there is no hierarchy between true and false",le.evaluate());
	}
	
	@Test
	public void testEvaluateTrueFalse() {
		le= new LessEquals(ICondition.TRUE,ICondition.FALSE);
		assertFalse("Returns false because there is no hierarchy between true and false",le.evaluate());
	}
	
	@Test
	public void testEvaluateFalseFalse() {
		le= new LessEquals(ICondition.FALSE,ICondition.FALSE);
		assertTrue("Should return true, because two false are the same!",le.evaluate());
	}
	
	@Test
	public void testEvaluateTrueTrue() {
		le= new LessEquals(ICondition.TRUE,ICondition.TRUE);
		assertTrue("Should return true, because two true are the same!",le.evaluate());
	}
	
	@Test(expected= NullPointerException.class)
	public void testLessEqualsNullFalse() {
		le= new LessEquals(null,ICondition.FALSE);
	}
	
	@Test(expected= NullPointerException.class)
	public void testLessEqualsFalseNull() {
		le= new LessEquals(ICondition.FALSE,null);
	}

	@Test
	public void testToStringFalseTrue() {
		assertEquals("Text should be identical", ICondition.FALSE+" <= "+ICondition.TRUE,le.toString());
	}

}
