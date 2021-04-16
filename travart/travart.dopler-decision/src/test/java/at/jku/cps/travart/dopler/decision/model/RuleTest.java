package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.SelectDecisionAction;

public class RuleTest {
	Rule r;
	ICondition cond;
	IAction a;
	BooleanDecision b;

	@Before
	public void setUp() throws Exception {
		cond = ICondition.TRUE;
		b = new BooleanDecision("test");
		a = new SelectDecisionAction(b);
		r = new Rule(cond, a);
	}

	@Test
	public void testHashCode() {
		assertEquals(r.hashCode(), r.hashCode());
		Rule r2 = new Rule(cond, a);
		assertEquals(r.hashCode(), r2.hashCode());
	}

	@Test(expected = NullPointerException.class)
	public void testRule_NullCondition() {
		new Rule(null, a);
	}

	@Test(expected = NullPointerException.class)
	public void testRule_NullAction() {
		new Rule(cond, null);
	}

	@Test
	public void testSetGetAction() {
		assertEquals(a, r.getAction());
		IAction a2 = new DeSelectDecisionAction(b);
		r.setAction(a2);
		assertEquals(a2, r.getAction());
	}

	@Test
	public void testSetGetCondition() {
		assertEquals(cond, r.getCondition());
		ICondition cond2 = ICondition.FALSE;
		r.setCondition(cond2);
		assertEquals(cond2, r.getCondition());
	}

	@Test
	public void testEqualsObject() {
		assertFalse(r.equals(null));
		assertFalse(r.equals("a String"));
		assertTrue(r.equals(r));
		Rule r2 = new Rule(cond, a);
		assertTrue(r.equals(r2));
		r2.setCondition(ICondition.FALSE);
		assertFalse(r.equals(r2));
		IAction a2 = new DeSelectDecisionAction(b);
		r2.setAction(a2);
		assertFalse(r.equals(r2));
		r2.setCondition(cond);
		assertFalse(r.equals(r2));
	}

	@Test
	public void testToString() {
		assertEquals("if " + cond.toString() + " {\n" + a.toString() + "\n}\n", r.toString());
		r.setCondition(b);
		assertEquals("if (" + b.toString() + ") {\n" + a.toString() + "\n}\n", r.toString());
	}

}
