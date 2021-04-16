package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.*;

import org.junit.Test;

import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;

public class DoubleValueTest {

	@Test
	public void equalsTest() {
		DoubleValue d1 = new DoubleValue(1.0);
		DoubleValue d2 = new DoubleValue(2.0);
		DoubleValue d3 = new DoubleValue(1.0);
		DoubleValue d4 = new DoubleValue(-1.0);
		DoubleValue d5 = new DoubleValue(Double.MAX_VALUE);
		DoubleValue d6 = new DoubleValue(Double.MAX_VALUE);
		DoubleValue d7 = new DoubleValue(Double.MIN_VALUE);

		assertNotEquals(d1, d2);
		assertEquals(d1, d1);
		assertEquals(d1, d3);
		assertNotEquals(d1, d4);
		assertEquals(d5, d6);
		assertEquals(d5, d5);
		assertNotEquals(d6, d7);
	}
	
	@Test
	public void evaluateTest() {
		DoubleValue d1 = new DoubleValue(1.0);
		assertTrue(d1.evaluate());
		d1= new DoubleValue(-1.0);
		assertTrue(d1.evaluate());
		d1= new DoubleValue(Double.MAX_VALUE);
		assertTrue(d1.evaluate());
		d1= new DoubleValue(Double.MIN_VALUE);
		assertTrue(d1.evaluate());
		d1= new DoubleValue(Double.POSITIVE_INFINITY);
		assertFalse(d1.evaluate());
		d1= new DoubleValue(Double.NEGATIVE_INFINITY);
		assertFalse(d1.evaluate());
		
	}
}
