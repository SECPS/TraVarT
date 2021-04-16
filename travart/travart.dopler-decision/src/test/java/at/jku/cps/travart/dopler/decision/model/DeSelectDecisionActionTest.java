package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;

public class DeSelectDecisionActionTest {
	private DeSelectDecisionAction des;
	private BooleanDecision b;
	private String testString = "test";

	@Before
	public void init() {
		b = new BooleanDecision(testString);
		des = new DeSelectDecisionAction(b);
	}

	@Test
	public void testHashCode() {
		DeSelectDecisionAction des2 = new DeSelectDecisionAction(b);
		assertEquals(des2.hashCode(), des.hashCode());
	}

	@Test(expected = NullPointerException.class)
	public void testDeSelectDecisionAction() {
		des = new DeSelectDecisionAction(null);
	}

	@Test
	public void testExecute() throws ActionExecutionException, RangeValueException {
		b.setValue(BooleanValue.getTrue());
		assertTrue(b.getValue().getValue());
		des.execute();
		assertFalse(b.getValue().getValue());
	}

	@Test
	public void testIsSatisfied() throws ActionExecutionException, RangeValueException {
		assertTrue(des.isSatisfied());
		b.setValue(BooleanValue.getTrue());
		assertFalse(des.isSatisfied());
		des.execute();
		assertTrue(des.isSatisfied());
	}

	@Test
	public void testGetVariable() {
		assertEquals(b, des.getVariable());
		assertNotEquals(new BooleanDecision("different"), des.getVariable());
	}

	@Test
	public void testGetValue() {
		assertEquals(BooleanValue.getFalse(), des.getValue());
	}

	@Test
	public void testEqualsObject() {
		assertFalse(des.equals("a String"));
		assertFalse(des.equals(null));
		assertTrue(des.equals(des));
		DeSelectDecisionAction des2 = new DeSelectDecisionAction(b);
		assertTrue(des.equals(des2));
	}

	@Test
	public void testToString() {
		assertEquals(testString + " = false;", des.toString());
	}

}
