package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class RangeTest {
	private Range<Double> r;
	private DoubleValue test = new DoubleValue(0d);

	@Before
	public void setUp() throws Exception {
		r = new Range<>();

		r.add(test);
	}

	@Test
	public void testEnable() throws RangeValueException {
		test.enable();
		assertTrue(test.isEnabled());
	}

	@Test
	public void testDisable() throws RangeValueException {
		test.enable();
		assertTrue(test.isEnabled());
		test.disable();
		assertFalse(test.isEnabled());
	}
	
	@Test
	public void testEqualsSameObject() {
		assertTrue(r+ " should be same as "+r,r.equals(r));
	}
	
	@Test
	public void testEqualsTotallyDifferentClass() {
		StringValue sv=new StringValue("test");
		assertFalse(sv+ " should not be same as "+r,r.equals(sv));
	}
	
	@Test
	public void testEqualsDifferentSubclass() {
		HashSet<ARangeValue<Double>> hs=new HashSet<>();
		assertFalse(hs+ " should not be same as "+r,r.equals(hs));
	}
	
	@Test
	public void testEqualsUnequalSubset1() {
		DoubleValue dv1=new DoubleValue(1d);
		r.add(dv1);
		Range<Double> r2=new Range<>();
		r2.add(test);
		assertFalse(r2+ " should not be same as "+r,r.equals(r2));
	}
	
	@Test
	public void testEqualsUnequalSubset2() {
		DoubleValue dv1=new DoubleValue(1d);
		Range<Double> r2=new Range<>();
		r2.add(dv1);
		r2.add(test);
		assertFalse(r2+ " should not be same as "+r,r.equals(r2));
	}
	@Test
	public void testEqualsEqualSubset() {
		Range<Double> r2=new Range<>();
		r2.add(test);
		assertTrue(r2+ " should be same as "+r,r.equals(r2));
	}
	
	@Test
	public void testEqualsUnequalSubset3() {
		DoubleValue dv1=new DoubleValue(1d);
		Range<Double> r2=new Range<>();
		r2.add(dv1);
		assertFalse(r2+ " should not be same as "+r,r.equals(r2));
	}

}
