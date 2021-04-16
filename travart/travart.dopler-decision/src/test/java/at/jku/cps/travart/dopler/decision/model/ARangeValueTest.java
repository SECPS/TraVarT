package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;

public class ARangeValueTest {
	private ARangeValue<Double> rv;

	@Before
	public void setUp() throws Exception {
		rv = new DoubleValue(0d);
	}

	@SuppressWarnings({ "rawtypes" })
	@Test
	public void equalsTest() {
		ARangeValue v = new ARangeValue() {

			@Override
			public boolean lessThan(final Object other) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean greaterThan(final Object other) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean evaluate() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		assertEquals(v, v);
		assertNotEquals(v, null);
		assertNotEquals(v, "a string");
		assertNotEquals(v, BooleanValue.getTrue());
		assertNotEquals(BooleanValue.getTrue(), BooleanValue.getFalse());
		assertEquals(BooleanValue.getTrue(), BooleanValue.getTrue());
		assertEquals(BooleanValue.getFalse(), BooleanValue.getFalse());
	}

	@Test(expected = NullPointerException.class)
	public void toStringTest_NoValue() {
		@SuppressWarnings("rawtypes")
		ARangeValue v = new ARangeValue() {

			@Override
			public boolean lessThan(final Object other) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean greaterThan(final Object other) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean evaluate() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		v.toString();
	}

	@SuppressWarnings({ "rawtypes" })
	@Test
	public void toStringTest() {
		ARangeValue v = BooleanValue.getTrue();
		assertEquals("true", v.toString());
	}

	@Test
	public void testIsEnabled() {
		assertTrue(rv.isEnabled());
	}

	@Test
	public void testSetEnabled() {
		assertTrue(rv.isEnabled());
		rv.disable();
		assertFalse(rv.isEnabled());
	}

}
