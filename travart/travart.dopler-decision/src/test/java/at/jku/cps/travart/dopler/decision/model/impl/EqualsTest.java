package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ICondition;

public class EqualsTest {
	private Equals e;
	
	@Before
	public void setUp() throws Exception {
		e=new Equals(ICondition.FALSE,ICondition.TRUE);
	}

	@Test
	public void testEqualsGetValueFunctionAndARangeValue() throws RangeValueException {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		ed.setValue(s);
		GetValueFunction gvf=new GetValueFunction(ed);
		e=new Equals(gvf,s);
		assertTrue("StringValue s is same as StringValue s",e.evaluate());
	}
	
	@Test
	public void testEqualsGetValueFunctionAndGetValueFunctionStrings() {
		EnumDecision ed=new EnumDecision("testDecision");
		StringValue s=new StringValue("TestValue");
		ed.getRange().add(s);
		GetValueFunction gvf1=new GetValueFunction(ed);
		GetValueFunction gvf2=new GetValueFunction(ed);
		e=new Equals(gvf1,gvf2);
		assertTrue("Both getValueFunction should return the same value.",e.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testEqualsGetValueFunctionAndARangeValue1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		e=new Equals(gvf1,d2);
		assertFalse(gvf1.getDecision().getValue() +" == "+d2 +" should be false",e.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testEqualsGetValueFunctionAndARangeValue2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		e=new Equals(gvf1,d2);
		assertFalse(gvf1.getDecision().getValue() +" == "+d2 +" should be false",e.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testEqualsGetValueFunctionAndARangeValue3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		e=new Equals(gvf1,d2);
		assertTrue(gvf1.getDecision().getValue() +" == "+d2 +" should be true",e.evaluate());
	}
	
	@Test
	public void testEqualsARangeValueAndGetValueFunction1() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(3);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		e=new Equals(d2,gvf1);
		assertFalse(d2 +" == "+gvf1.getDecision().getValue() +" should be false",e.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testEqualsARangeValueAndGetValueFunction2() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(3);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		e=new Equals(d2,gvf1);
		assertFalse(d2 +" == "+gvf1.getDecision().getValue() +" should be false",e.evaluate());
	}
	
	/**
	 * Checks a getvaluefunction against a concrete value.
	 * @throws RangeValueException
	 */
	@Test
	public void testEqualsARangeValueAndGetValueFunction3() throws RangeValueException {
		NumberDecision nd1=new NumberDecision("testDecision1");
		
		DoubleValue d1=new DoubleValue(4);
		DoubleValue d2=new DoubleValue(4);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		GetValueFunction gvf1=new GetValueFunction(nd1);
		e=new Equals(d2,gvf1);
		assertTrue(d2 +" == "+gvf1.getDecision().getValue() +" should be true",e.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testEqualsGetValueFunctionAndGetValueFunctionNumbers1() throws RangeValueException {
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
		e=new Equals(gvf1,gvf2);
		assertFalse(gvf1.getDecision().getValue() +" == "+gvf2.getDecision().getValue() +" should be false",e.evaluate());
	}
	
	/**
	 * Creates 2 NumberDecisions, adds them to getValueFunctions and
	 * checks if the value comparison is correct.
	 * @throws RangeValueException
	 */
	@Test
	public void testEqualsGetValueFunctionAndGetValueFunctionNumbers2() throws RangeValueException {
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
		e=new Equals(gvf2,gvf1);
		assertFalse(gvf2.getDecision().getValue() +" == "+gvf1.getDecision().getValue() +" should be false",e.evaluate());
	}

	@Test
	public void testEvaluateFalseTrue() {
		assertFalse("Returns false because there is no hierarchy between true and false",e.evaluate());
	}
	
	@Test
	public void testEvaluateTrueFalse() {
		e= new Equals(ICondition.TRUE,ICondition.FALSE);
		assertFalse("Returns false because there is no hierarchy between true and false",e.evaluate());
	}
	
	@Test
	public void testEvaluateFalseFalse() {
		e= new Equals(ICondition.FALSE,ICondition.FALSE);
		assertTrue("Should return true, because two false are the same!",e.evaluate());
	}
	
	@Test
	public void testEvaluateTrueTrue() {
		e= new Equals(ICondition.TRUE,ICondition.TRUE);
		assertTrue("Should return true, because two true are the same!",e.evaluate());
	}
	
	@Test(expected= NullPointerException.class)
	public void testEqualsNullFalse() {
		e= new Equals(null,ICondition.FALSE);
	}
	
	@Test(expected= NullPointerException.class)
	public void testEqualsFalseNull() {
		e= new Equals(ICondition.FALSE,null);
	}

	@Test
	public void testToStringFalseTrue() {
		assertEquals("Text should be identical", ICondition.FALSE+" == "+ICondition.TRUE,e.toString());
	}

}
