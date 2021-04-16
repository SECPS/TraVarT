package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;

public class BooleanValueTest {

	@Test
	public void equalsTest() {
		BooleanValue b1 = BooleanValue.getTrue();
		BooleanValue b2 = BooleanValue.getTrue();
		BooleanValue b3 = BooleanValue.getFalse();
		BooleanValue b4 = BooleanValue.getFalse();
		assertEquals(b1, b2);
		assertEquals(b1, b1);
		assertEquals(b3, b4);
		assertNotEquals(b1, "a string");
		assertNotEquals(b1, new DoubleValue(1.2));
		assertNotEquals(b1, b4);
		assertNotEquals(b3, b2);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetValue() {
		BooleanValue b1 = BooleanValue.getTrue();
		b1.setValue(true);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSetValueNull() {
		BooleanValue b1 = BooleanValue.getTrue();
		b1.setValue(null);
	}

	@Test
	public void testSetEnabled() {
		BooleanValue b1 = BooleanValue.getTrue();
		b1.enable();
		assertTrue(b1.isEnabled());
		b1.disable();
		assertFalse(b1.isEnabled());
	}
	
	@Test
	public void testLessThan() {
		BooleanValue tr=BooleanValue.getTrue();
		BooleanValue fa=BooleanValue.getFalse();
		assertFalse("No hierarchy in boolean values, therefore false",tr.lessThan(tr));
		assertFalse("No hierarchy in boolean values, therefore false",tr.lessThan(fa));
		assertFalse("No hierarchy in boolean values, therefore false",fa.lessThan(tr));
		assertFalse("No hierarchy in boolean values, therefore false",fa.lessThan(fa));
	}
	
	@Test
	public void testGreaterThan() {
		BooleanValue tr=BooleanValue.getTrue();
		BooleanValue fa=BooleanValue.getFalse();
		assertFalse("No hierarchy in boolean values, therefore false",tr.greaterThan(tr));
		assertFalse("No hierarchy in boolean values, therefore false",tr.greaterThan(fa));
		assertFalse("No hierarchy in boolean values, therefore false",fa.greaterThan(tr));
		assertFalse("No hierarchy in boolean values, therefore false",fa.greaterThan(fa));
	}
	
	@Test
	public void testEvaluate() {
		BooleanValue tr=BooleanValue.getTrue();
		BooleanValue fa=BooleanValue.getFalse();
		assertTrue("True should return true",tr.evaluate());
		assertFalse("False should return false",fa.evaluate());
	}

}
