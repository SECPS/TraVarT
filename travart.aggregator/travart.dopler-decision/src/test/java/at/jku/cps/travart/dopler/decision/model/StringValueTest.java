package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class StringValueTest {
	StringValue s;

	@Before
	public void setUp() throws Exception {
		s = new StringValue("test");
	}

	@Test
	public void testToString() {
		assertEquals("test", s.toString());
	}
	
	@Test
	public void testLessThanActuallyLess() {
		assertTrue("test should be lower than zest.",s.lessThan(new StringValue("zest")));
	}
	
	@Test
	public void testLessThanActuallyEqual() {
		assertFalse("test should not be lower than test.",s.lessThan(new StringValue("test")));
	}
	
	@Test
	public void testLessThanActuallyGreater() {
		assertFalse("test should not be lower than aest.",s.lessThan(new StringValue("aest")));
	}
	
	@Test
	public void testGreaterThanActuallyLess() {
		assertFalse("test should be not greater than zest.",s.greaterThan(new StringValue("zest")));
	}
	
	@Test
	public void testGreaterThanActuallyEqual() {
		assertFalse("test should not be greater than test.",s.greaterThan(new StringValue("test")));
	}
	
	@Test
	public void testGreaterThanActuallyGreater() {
		assertTrue("test should be greater than aest.",s.greaterThan(new StringValue("aest")));
	}
	
	@Test
	public void testevaluateisBlank() {
		s=new StringValue("");
		assertFalse("Blank value should return false.", s.evaluate());
	}
	@Test
	public void testevaluateisNotBlank() {
		assertTrue("Non-Blank value should return true.", s.evaluate());
	}
}
