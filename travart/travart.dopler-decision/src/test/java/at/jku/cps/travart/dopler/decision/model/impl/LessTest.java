package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class LessTest {
	private Less l;
	
	@Before
	public void setUp() throws Exception {
		l=new Less(ICondition.FALSE,ICondition.TRUE);
	}

	@Test
	public void testLessGetValueFunctionAndARangeValue() throws RangeValueException {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		ed.setValue(s);
		GetValueFunction gvf=new GetValueFunction(ed);
		l=new Less(gvf,s);
		assertFalse(gvf.getDecision().getValue() +" < " +s +" should be false",l.evaluate());
	}
	
	@Test
	public void testLessGetValueFunctionAndGetValueFunctionStrings() {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		GetValueFunction gvf1=new GetValueFunction(ed);
		GetValueFunction gvf2=new GetValueFunction(ed);
		l=new Less(gvf1,gvf2);
		assertFalse(gvf1.getDecision().getValue() +" < " +gvf2.getDecision().getValue() +" should be false",l.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessGetValueFunctionAndARangeValue1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		l=new Less(gvf1,d2);
		assertTrue("Numberdecision has a lower value than value2 therefore should be true.",l.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessGetValueFunctionAndARangeValue2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		l=new Less(gvf1,d2);
		assertFalse("Numberdecision has a higher value than value2 therefore should be false.",l.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessGetValueFunctionAndARangeValue3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		l=new Less(gvf1,d2);
		assertFalse("Numberdecision is equal to value2 therefore should be false.",l.evaluate());
	}
	
	@Test
	public void testLessARangeValueAndGetValueFunction1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		l=new Less(d2,gvf1);
		assertFalse(d2.toString() +" should not be < " +nd1.getValue(),l.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessARangeValueAndGetValueFunction2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		l=new Less(d2,gvf1);
		assertTrue(d2.toString() +" should be < " +nd1.getValue(),l.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessARangeValueAndGetValueFunction3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		l=new Less(d2,gvf1);
		assertFalse(d2 +" < "+d1 +" should be false" ,l.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessGetValueFunctionAndGetValueFunctionNumbers1() throws RangeValueException {
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
		l=new Less(gvf1,gvf2);
		assertTrue(gvf1 +" < "+ gvf2 +" should be true",l.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testLessGetValueFunctionAndGetValueFunctionNumbers2() throws RangeValueException {
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
		l=new Less(gvf2,gvf1);
		assertFalse(gvf2+" < "+ gvf1 +" should be false",l.evaluate());
	}

	@Test
	public void testEvaluateFalseTrue() {
		assertFalse("Returns false because there is no hierarchy between true and false",l.evaluate());
	}
	
	@Test
	public void testEvaluateTrueFalse() {
		l= new Less(ICondition.TRUE,ICondition.FALSE);
		assertFalse("Returns false because there is no hierarchy between true and false",l.evaluate());
	}
	
	@Test
	public void testEvaluateFalseFalse() {
		l= new Less(ICondition.FALSE,ICondition.FALSE);
		assertFalse("Should return false, because two false are the same!",l.evaluate());
	}
	
	@Test
	public void testEvaluateTrueTrue() {
		l= new Less(ICondition.TRUE,ICondition.TRUE);
		assertFalse("Should return false, because two true are the same!",l.evaluate());
	}
	
	@Test(expected= NullPointerException.class)
	public void testLessNullFalse() {
		l= new Less(null,ICondition.FALSE);
	}
	
	@Test(expected= NullPointerException.class)
	public void testLessFalseNull() {
		l= new Less(ICondition.FALSE,null);
	}

	@Test
	public void testToStringFalseTrue() {
		assertEquals("Text should be identical", ICondition.FALSE+" < "+ICondition.TRUE,l.toString());
	}

}
