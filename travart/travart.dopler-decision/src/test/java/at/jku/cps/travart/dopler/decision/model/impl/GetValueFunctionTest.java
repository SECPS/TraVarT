package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ADecision.DecisionType;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;

public class GetValueFunctionTest {
	GetValueFunction gvf;
	NumberDecision nd1;

	@Before
	public void setUp() throws Exception {
		nd1=new NumberDecision("nd");
		DoubleValue d1=new DoubleValue(0);
		nd1.getRange().add(d1);
		nd1.setValue(d1);
		gvf=new GetValueFunction(nd1);
	}

	@Test(expected = NullPointerException.class)
	public void testGetValueFunctionNull() {
		gvf=new GetValueFunction(null);
	}

	@Test
	public void testGetDecision() {
		assertEquals("Should return decision.",nd1,gvf.getDecision());
	}

	@Test
	public void testEvaluateNonNaN() {
		assertTrue("True, because Non-NaN-Numberdecisions are always true.",gvf.evaluate());
	}

	@Test
	public void testExecuteNoRangeValueDoubleValue() {
		assertEquals("Return value should be 0.",(ARangeValue)new DoubleValue(0),gvf.execute());
	}
	
	@Test
	public void testExecuteNoRangeValueEnumValue() {
		EnumDecision ed1=new EnumDecision("test");
		gvf=new GetValueFunction(ed1);
		assertEquals("Should return single space String",new StringValue(" "),gvf.execute());
	}
	
	@Test
	public void testExecuteNoRangeValueStringValue() {
		StringDecision ed1=new StringDecision("test");
		gvf=new GetValueFunction(ed1);
		assertEquals("Should return empty String",new StringValue(""),gvf.execute());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testExecuteARangeValue() {
		ADecision<Object> ad=new ADecision<Object>("test",DecisionType.ENUM) {

			@Override
			public Range<Object> getRange() {return null;}

			@Override
			public ARangeValue<Object> getRangeValue(Object value) {return null;}

			@Override
			public ARangeValue<Object> getRangeValue(String value) {return null;}

			@Override
			public boolean containsRangeValue(ARangeValue<Object> value) {return false;	}

			@Override
			public void setRange(Range<Object> range) {}

			@Override
			public void reset() throws RangeValueException {}

			@Override
			public boolean evaluate() {return false;}

			@Override
			public ARangeValue<Object> getValue() {return null;}

			@Override
			public void setValue(ARangeValue<Object> value) throws RangeValueException {}
			
		};
		gvf=new GetValueFunction(ad);
		gvf.execute();
		
	}

	@Test
	public void testToString() {
		assertEquals("getValue("+nd1.toString()+")",gvf.toString());
	}

}
