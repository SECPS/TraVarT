package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueNotEnabledException;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class StringDecisionTest {
	private StringDecision sdec;

	@Before
	public void init() {
		sdec = new StringDecision("test");
	}

	@Test(expected = NullPointerException.class)
	public void testStringDecisionString() {
		sdec = new StringDecision(null);
	}

	@Test(expected = NullPointerException.class)
	public void testStringDecisionStringBoolean_NullId_true() {
		new StringDecision(null);
	}

	@Test(expected = NullPointerException.class)
	public void testStringDecisionStringBoolean_NullId_false() {
		new StringDecision(null);
	}

	@Test
	public void testEval() throws RangeValueException {
		assertFalse(sdec.evaluate());
		sdec.setValue(new StringValue("test"));
		assertTrue(sdec.evaluate());
	}

	@Test
	public void testGetValue() throws RangeValueException {
		sdec.setValue(new StringValue("test"));
		assertEquals("test", sdec.getValue().getValue());
	}

	@Test(expected = RangeValueException.class)
	public void testSetValue_setNull() throws RangeValueException {
		sdec.setValue(null);
	}

	@Test(expected = RangeValueNotEnabledException.class)
	public void testSetValue_setDisabledValue() throws RangeValueException {
		StringValue s = new StringValue("test");
		s.disable();
		sdec.setValue(s);
	}

	@Test
	public void testSetValue_valid() throws RangeValueException {
		sdec.setValue(new StringValue("test"));
		assertEquals("test", sdec.getValue().getValue());
	}

	@Test
	public void testGetRange() {
		assertNull(sdec.getRange());
	}

	@Test
	public void testSetRange() {
		// just don't fail
		sdec.setRange(new Range<String>());
	}

	@Test
	public void testGetRangeValue() {
		String s = "test";
		assertEquals(s, sdec.getRangeValue(s).toString());
	}

	@Test
	public void testReset() throws RangeValueException {
		sdec.setValue(new StringValue("test"));
		assertEquals("test", sdec.getValue().toString());
		sdec.reset();
		assertEquals("", sdec.getValue().toString());
	}
}
