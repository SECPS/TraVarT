package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.IsTakenFunction;

public class IsTakenFunctionTest {

	private IsTakenFunction fun;
	private BooleanDecision b;

	@Before
	public void prepareObject() {
		b = new BooleanDecision("test");
		b.setSelected(true);
		fun = new IsTakenFunction(b);
	}

	@Test(expected = NullPointerException.class)
	/**
	 * Null as a parameter for the constructor should not be allowed
	 */
	public void testIsTakenFunction() {
		fun = new IsTakenFunction(null);
	}

	@Test
	public void testExecute() {
		assertTrue(fun.execute());
		b.setSelected(false);
		assertFalse(fun.execute());
	}

	@Test
	public void testEval() {
		assertTrue(fun.evaluate());
		b.setSelected(false);
		assertFalse(fun.evaluate());
	}

	@Test
	public void testToString() {
		assertEquals("isTaken(" + b.toString() + ")", fun.toString());
	}

	@Test
	public void testGetName() {
		assertEquals("isTaken", fun.getName());
	}

	@Test
	public void testEqualsObject() {
		assertFalse(fun.equals(null));
		assertFalse(fun.equals("aString"));
		assertTrue(fun.equals(fun));
		BooleanDecision b2 = new BooleanDecision("test");
		b2.setSelected(true);
		IsTakenFunction fun2 = new IsTakenFunction(b2);
		BooleanDecision b3 = new BooleanDecision("test2");
		b3.setSelected(false);
		IsTakenFunction fun3 = new IsTakenFunction(b3);
		assertTrue(fun.equals(fun2));
		assertFalse(fun.equals(fun3));

	}

}
