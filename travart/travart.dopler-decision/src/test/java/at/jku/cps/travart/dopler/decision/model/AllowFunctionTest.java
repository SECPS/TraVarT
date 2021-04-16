package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.impl.AllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class AllowFunctionTest {
	private AllowAction da;
	private EnumDecision dec;
	private ARangeValue<String> v;

	@Before
	public void prepareObject() {
		dec = new EnumDecision("test");
		Range<String> ra = new Range<>();
		v = new StringValue("testVal");
		ra.add(v);
		dec.setRange(ra);
		da = new AllowAction(dec, v);
	}

	@Test(expected = NullPointerException.class)
	public void noNullConditionTest() {
		new AllowAction(null, BooleanValue.getTrue());

	}

	@Test(expected = NullPointerException.class)
	public void noNullValueTest() {
		new AllowAction(new BooleanDecision("test"), null);
	}

	@Test(expected = ActionExecutionException.class)
	public void DecisionValueRangeTest_AllowStringForBoolDec() throws ActionExecutionException {
		IAction allow = new AllowAction(new BooleanDecision("test"), new StringValue("test"));
		allow.execute();
	}

	@Test(expected = ActionExecutionException.class)
	public void DecisionValueRangeTest_AllowDoubleForBoolDec() throws ActionExecutionException {
		IAction allow = new AllowAction(new BooleanDecision("test"), new DoubleValue(1.2));
		allow.execute();
	}

	@Test(expected = ActionExecutionException.class)
	public void DecisionValueRangeTest_AllowDoubleForEnumDec() throws ActionExecutionException {
		IAction allow = new AllowAction(new EnumDecision("test"), new StringValue("test"));
		allow.execute();
	}

	@Test(expected = ActionExecutionException.class)
	public void DecisionValueRangeTest_AllowBoolForEnumDec() throws ActionExecutionException {
		IAction allow = new AllowAction(new EnumDecision("test"), BooleanValue.getFalse());
		allow.execute();
	}

	@Test
	public void testExecute() throws ActionExecutionException, RangeValueException {
		v.disable();
		assertFalse(v.isEnabled());
		da.execute();
		assertTrue(v.isEnabled());
		dec.getRange().add(dec.getNoneOption());
		v = dec.getNoneOption();
		da = new AllowAction(dec, v);
		da.execute();
		dec.setVisibility(new BooleanDecision("test"));
		da = new AllowAction(dec, v);
		da.execute();
		assertFalse(dec.isVisible());
		BooleanDecision b = new BooleanDecision("test");
		b.setValue(BooleanValue.getTrue());
		dec.setVisibility(b);
		da = new AllowAction(dec, v);
		da.execute();
		assertTrue(dec.isVisible());
	}

	@Test
	public void testIsSatisfied_NotSatisfied() throws Exception {
		dec.setValue(v);
		v.disable();
		assertFalse("Should not be satisfied upon disabling.", da.isSatisfied());
	}

	@Test
	public void testIsSatisfied_Satisfied() throws Exception {
		dec.setValue(v);
		v.disable();
		da.execute();
		assertTrue("Should be satisfied after executing the function.", da.isSatisfied());
	}

	@Test(expected = ActionExecutionException.class)
	public void testIsSatisfied_ValueNotPresent() throws Exception {
		da = new AllowAction(dec, new StringValue("test"));
		da.isSatisfied();
		// should throw Exception because the new String is not part of the range.
	}

	@Test
	public void testEquals() {
		assertEquals(da.hashCode(), da.hashCode());
		AllowAction da2 = new AllowAction(dec, v);
		assertEquals(da.hashCode(), da2.hashCode());
		AllowAction da3 = new AllowAction(dec, new StringValue("testtest"));
		assertNotEquals(da.hashCode(), da3.hashCode());
		EnumDecision dec2 = new EnumDecision("diff");
		Range<String> ra = new Range<>();
		v = new StringValue("testVal");
		ra.add(v);
		dec2.setRange(ra);
		AllowAction da4 = new AllowAction(dec2, v);
		assertNotEquals(da.hashCode(), da4.hashCode());
		EnumDecision dec3 = new EnumDecision("diff");
		dec2.setRange(ra);
		AllowAction da5 = new AllowAction(dec3, v);
		assertNotEquals(da.hashCode(), da5.hashCode());
	}

	@Test
	public void testHashCode() {
		assertFalse(da.equals(null));
		assertTrue(da.equals(da));
		assertFalse(da.equals("a String"));
		AllowAction da2 = new AllowAction(dec, v);
		assertTrue(da.equals(da2));
		AllowAction da3 = new AllowAction(dec, new StringValue("testtest"));
		assertFalse(da.equals(da3));
	}

	@Test
	public void testToString() {
		assertEquals("allow(" + dec.toString() + "." + v.toString() + ");", da.toString());
	}
}
