package at.jku.cps.travart.dopler.decision.model.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueNotEnabledException;

public class DisAllowActionTest {
	private DisAllowAction daa;
	private EnumDecision ed;
	private StringValue s;

	@Before
	public void setUp() throws Exception {
		ed=new EnumDecision("test");
		s= new StringValue("testValue");
		ed.getRange().add(s);
		daa= new DisAllowAction(ed,s);		
	}

	@Test(expected=NullPointerException.class)
	public void testDisAllowActionNullARangeValue() {
		daa=new DisAllowAction(null,s);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDisAllowActionIDecisionNull() {
		daa=new DisAllowAction(ed,null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDisAllowActionNotInRange() throws ActionExecutionException {
		daa=new DisAllowAction(ed,new StringValue("someNotContainedValue"));
	}
	
	@Test(expected=ActionExecutionException.class)
	public void testExecuteNotInRange() throws ActionExecutionException {
		StringValue s1=new StringValue("someNotContainedValue");
		ed.getRange().add(s1);
		daa=new DisAllowAction(ed,s1);
		ed.getRange().remove(s1);
		daa.execute();
	}
	
	@Test
	public void testExecuteWorking() throws ActionExecutionException {
		daa.execute();
	}
	
	
	@Test(expected= RangeValueNotEnabledException.class)
	public void testExecuteEnumNoneOption() throws ActionExecutionException, RangeValueException {
		ed.getRange().add(ed.getNoneOption());
		daa=new DisAllowAction(ed, ed.getNoneOption());
		daa.execute();
		ed.setValue(ed.getNoneOption());
	}
	
	@Test(expected= RangeValueNotEnabledException.class)
	public void testExecuteEnumNoneOptionVisibilityFalse() throws ActionExecutionException, RangeValueException {
		ed.getRange().add(ed.getNoneOption());
		BooleanDecision bd= new BooleanDecision("testbool");
		bd.setSelected(false);
		ed.setVisibility(bd);
		daa=new DisAllowAction(ed, ed.getNoneOption());
		daa.execute();
		assertEquals(BooleanValue.getTrue(),bd.getValue());
		ed.setValue(ed.getNoneOption());
	}

	@Test
	public void testIsSatisfiedNotSatisfied() throws ActionExecutionException {
		assertFalse(daa.isSatisfied());
	}
	
	@Test
	public void testIsSatisfiedIsSatisfied() throws ActionExecutionException {
		daa.execute();
		assertTrue(daa.isSatisfied());
	}
	
	@Test(expected= ActionExecutionException.class)
	public void testIsSatisfiedNotInRange() throws ActionExecutionException {
		StringValue s1=new StringValue("someNotContainedValue");
		ed.getRange().add(s1);
		daa=new DisAllowAction(ed,s1);
		ed.getRange().remove(s1);
		daa.isSatisfied();
	}

	@Test
	public void testGetVariable() {
		assertEquals(ed,daa.getVariable());
	}

	@Test
	public void testGetValue() {
		assertEquals(s,daa.getValue());
	}

	@Test
	public void testEqualsDisAllowActionSame() {
		DisAllowAction daa2=new DisAllowAction(ed,s);
		assertTrue(daa.equals(daa2));
	}
	
	@Test
	public void testEqualsItself() {
		assertTrue(daa.equals(daa));
	}
	
	@Test
	public void testEqualsNull() {
		assertFalse(daa.equals(null));
	}
	
	@Test
	public void testEqualsObject() {
		assertFalse(daa.equals(s));
	}
		
	@Test
	public void testEqualsDisAllowActionDifferent() {
		EnumDecision ed2=new EnumDecision("Test2");
		StringValue s2=new StringValue("Value2");
		ed2.getRange().add(s2);
		DisAllowAction daa2=new DisAllowAction(ed2,s2);
		assertFalse(daa.equals(daa2));
	}
	

	@Test
	public void testToString() {
		assertEquals("disAllow("+daa.getVariable()+"."+daa.getValue()+");",daa.toString());
	}

}
