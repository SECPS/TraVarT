package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueNotEnabledException;
import at.jku.cps.travart.dopler.decision.exc.UnsatisfiedCardinalityException;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class EnumDecisionTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected = NullPointerException.class)
	public void emptyRangeTest() {
		EnumDecision d = new EnumDecision("test");
		Range r = new Range();
		d.setRange(r);
		d.getRangeValue(null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void hasNoneOptionTest() {
		EnumDecision d = new EnumDecision("test");
		Range r = new Range();
		d.setRange(r);
		assertFalse(d.hasNoneOption());
		r.add(d.getNoneOption());
		r.add(new StringValue("test"));
		assertTrue(d.hasNoneOption());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void addNoneOptionTest() {
		EnumDecision d = new EnumDecision("test");
		Range r = new Range();
		d.setRange(r);
		assertFalse(d.hasNoneOption());
		d.getRange().add(d.getNoneOption());
		// checks if setting a non option worked
		assertTrue(d.hasNoneOption());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void getNoneOptionTest() {
		EnumDecision d = new EnumDecision("test");
		Range r = new Range();
		d.setRange(r);
		assertFalse(d.hasNoneOption());
		d.getRange().add(d.getNoneOption());
		assertEquals(new StringValue("None"), d.getNoneOption());
	}

	@Test
	public void evalTest() {
		EnumDecision d = new EnumDecision("test");
		d.evaluate();
		d.setCardinality(new Cardinality(1, 1));
		assertFalse(d.evaluate());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void getValueTest() throws RangeValueException {
		EnumDecision d = new EnumDecision("test");
		d.setCardinality(new Cardinality(0, 1));
		Range r = new Range();
		r.add(new StringValue("test"));
		d.setRange(r);
//		ThrowingRunnable run = () -> {
		assertEquals(" ", d.getValue().getValue());
//		};
//		assertThrows(
//				"There is no value set for the decision yet, so this should throw an exception when trying to find oen",
//				NoSuchElementException.class, run);
		// after the value was set, it should then find it.
		d.setValue(new StringValue("test"));
		assertEquals("test", d.getValue().getValue());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected = RangeValueException.class)
	public void setValueTest_setValueNotInRange() throws RangeValueException {
		EnumDecision d = new EnumDecision("test");
		Range r = new Range();
		r.add(new StringValue("test"));
		d.setRange(r);
		// the following value does not exist in the range and
		// should produce an error when trying to set to it
		d.setValue(new StringValue("something"));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected = RangeValueNotEnabledException.class)
	public void setValueTest_setValueNotEnabled() throws RangeValueException {
		// try to set value that is not enabled
		EnumDecision d = new EnumDecision("test");
		Range r = new Range();
		StringValue s = new StringValue("test");
		s.disable();
		r.add(s);
		d.setRange(r);
		d.setValue(s);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test(expected = RangeValueException.class)
	public void setValueTest_setValueViolatingCardinality() throws RangeValueException {
		// try to set value that does not satisfy cardinality
		EnumDecision d = new EnumDecision("test");
		d.setCardinality(new Cardinality(3, 3));
		Range r = new Range();
		StringValue s = new StringValue("test");
		r.add(s);
		d.setRange(r);
		d.setValue(s);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testSetTestWithHighMinCardinality() throws RangeValueException, UnsatisfiedCardinalityException {
		// This next test creates 3 values for a decision
		// with a cardinality minimum of 3. It sets all values
		// and then tests if the values are actually set
		EnumDecision d = new EnumDecision("test");
		d.setCardinality(new Cardinality(3, 4));
		Range r = new Range();
		StringValue s1 = new StringValue("test1");
		StringValue s2 = new StringValue("test2");
		StringValue s3 = new StringValue("test3");
		r.add(s1);
		r.add(s2);
		r.add(s3);
		d.setRange(r);
		Set valset = new HashSet();
		valset.add(s1);
		valset.add(s2);
		valset.add(s3);
//		ThrowingRunnable run = () -> d.getValue();
		assertEquals(" ", d.getValue().getValue());
		d.setValues(valset);
		assertTrue(d.getValues().contains(s1) && d.getValues().contains(s2) && d.getValues().contains(s3));
	}

	@Test(expected = RangeValueException.class)
	public void setValueTest_UnfittingCardinality() throws RangeValueException {
		EnumDecision d1 = new EnumDecision("test");
		d1.setCardinality(new Cardinality(2, 2));
		Range<String> r1 = new Range<>();
		StringValue t1 = new StringValue("test1");
		r1.add(t1);
		d1.setRange(r1);
		d1.setValue(new StringValue(t1.toString()));
		// only one value has been set with a cardinality
		// that has a minimum of 2, so an exception should
		// occur here
	}

	@Test(expected = UnsatisfiedCardinalityException.class)
	public void setValueTest_TooManyValuesForCardinality() throws RangeValueException, UnsatisfiedCardinalityException {
		EnumDecision d1 = new EnumDecision("test");
		d1.setCardinality(new Cardinality(0, 2));
		Range<String> r1 = new Range<>();
		StringValue t11 = new StringValue("test1");
		StringValue t21 = new StringValue("test2");
		StringValue t31 = new StringValue("test3");
		r1.add(t11);
		r1.add(t21);
		r1.add(t31);
		d1.setRange(r1);
		assertTrue(d1.getValues().isEmpty());
		Set<ARangeValue<String>> s1 = new HashSet<>();
		s1.add(new StringValue(t11.toString()));
		s1.add(new StringValue(t21.toString()));
		d1.setValues(s1);
		// still good amount of values here
		s1.add(new StringValue(t31.toString()));
		d1.setValues(s1);
		// too many values now
	}

	@Test(expected = RangeValueNotEnabledException.class)
	public void setValueTest_OneNotEnabledValue() throws RangeValueException, UnsatisfiedCardinalityException {
		// try to set values with one of the values disabled
		EnumDecision d1 = new EnumDecision("test");
		d1.setCardinality(new Cardinality(3, 3));
		Range<String> r1 = new Range<>();
		StringValue t11 = new StringValue("test1");
		StringValue t21 = new StringValue("test2");
		StringValue t31 = new StringValue("test3");
		t11.disable();
		r1.add(t11);
		r1.add(t21);
		r1.add(t31);
		d1.setRange(r1);
		assertTrue(d1.getValues().isEmpty());
		Set<ARangeValue<String>> s1 = new HashSet<>();
		s1.add(t11);
		s1.add(t21);
		s1.add(t31);
		d1.setValues(s1);
	}
	
	@Test
	public void testGetRangeValueStringContained() {
		EnumDecision d = new EnumDecision("test");
		StringValue sv=new StringValue("sv");
		d.getRange().add(sv);
		assertEquals(sv + " should equal "+d.getRangeValue("sv"),sv,d.getRangeValue("sv"));
	}
	
	@Test
	public void testGetRangeValueStringNotContained() {
		EnumDecision d = new EnumDecision("test");
		StringValue sv=new StringValue("sv");
		assertNull(d.getRangeValue("sv") + " should equal null",d.getRangeValue("sv"));
	}
	
	@Test
	public void testGetValuesNoValues() {
		EnumDecision d = new EnumDecision("test");
		d.getRange().add(d.getNoneOption());
		Set<ARangeValue<String>> set=new HashSet<>();
		set.add(d.getNoneOption());
		assertEquals(d.getValues()+" should equal "+set,set,d.getValues());
	}
	
	@Test
	public void testGetCardinality() {
		EnumDecision d = new EnumDecision("test");
		assertEquals(d.getCardinality() +" should equal "+new Cardinality(0,1),new Cardinality(0,1),d.getCardinality());
		d.setCardinality(new Cardinality(0, 2));
		assertEquals(new Cardinality(0,2),d.getCardinality());
	}
	
	@Test(expected=NullPointerException.class)
	public void testSetCardinalityNotNull() {
		EnumDecision d = new EnumDecision("test");
		//Cardinality should not be allowed to be null
		d.setCardinality(null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void setAndGetValuesTest_shouldPass() throws RangeValueException, UnsatisfiedCardinalityException {
		EnumDecision d = new EnumDecision("test");
		d.setCardinality(new Cardinality(0, 2));
		Range r = new Range();
		StringValue t1 = new StringValue("test1");
		StringValue t2 = new StringValue("test2");
		StringValue t3 = new StringValue("test3");
		r.add(t1);
		r.add(t2);
		r.add(t3);
		d.setRange(r);
		assertTrue(d.getValues().isEmpty());
		Set<ARangeValue<String>> s = new HashSet<>();
		s.add(new StringValue(t1.toString()));
		s.add(new StringValue(t2.toString()));
		d.setValues(s);
		assertTrue(d.getValues().contains(t1));
		assertTrue(d.getValues().contains(t2));
		assertEquals(2, d.getValues().size());
	}
	
	@Test(expected=RangeValueException.class)
	public void testSetValuesNotContainedInRange() throws RangeValueException, UnsatisfiedCardinalityException {
		EnumDecision d = new EnumDecision("test");
		Set<ARangeValue<String>> svset=new HashSet<>();
		svset.add(new StringValue("sv1"));
		d.setValues(svset);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void getValuesTest() throws RangeValueException, UnsatisfiedCardinalityException {
		EnumDecision d = new EnumDecision("test");
		d.setCardinality(new Cardinality(0, 2));
		Range r = new Range();
		StringValue t1 = new StringValue("test1");
		StringValue t2 = new StringValue("test2");
		StringValue t3 = new StringValue("test3");
		r.add(t1);
		r.add(t2);
		r.add(t3);
		d.setRange(r);
		assertTrue(d.getValues().isEmpty());
		Set<ARangeValue<String>> s = new HashSet<>();
		s.add(new StringValue(t1.toString()));
		s.add(new StringValue(t2.toString()));
		d.setValues(s);
		assertTrue(d.getValues().contains(t1));
		assertTrue(d.getValues().contains(t2));
		d.reset();
		assertTrue(d.getValues().isEmpty());
		d.getRange().add(d.getNoneOption());
		d.reset();
		assertEquals(d.getNoneOption().getValue(), d.getValue().getValue());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testReset() throws RangeValueException, UnsatisfiedCardinalityException {
		EnumDecision d = new EnumDecision("test");
		d.setCardinality(new Cardinality(0, 4));
		Range r = new Range();
		StringValue t1 = new StringValue("test1");
		StringValue t2 = new StringValue("test2");
		StringValue t3 = new StringValue("test3");
		r.add(t1);
		r.add(t2);
		r.add(t3);
		d.setRange(r);
		// add a bunch of values and set them, after reset check none are left
		Set s = new HashSet<>();
		s.add(t1);
		s.add(t2);
		s.add(t3);
		d.setValues(s);
		assertEquals(s, d.getValues());
		d.reset();
		assertTrue(d.getValues().isEmpty());
		/*
		 * add a bunch of values, and the none option. after reset check if none options
		 * remains and is selected. Deprecated because current implementation also
		 * removes the none-option on reset.
		 */
//		s.add(t1);
//		s.add(t2);
//		s.add(t3);
//		d.addNoneOption();
//		s.add(d.getNoneOption());
//		d.setValues(s);
//		assertTrue(d.getValues().contains(t1) && d.getValues().contains(t2) && d.getValues().contains(t3)
//				&& d.getValues().contains(d.getNoneOption()));
//		d.reset();
//		assertTrue(!d.getValues().contains(t1) && !d.getValues().contains(t2) && !d.getValues().contains(t3)
//				&& d.getValues().contains(d.getNoneOption()));
	}
}
