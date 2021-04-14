package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;
import at.jku.cps.travart.dopler.decision.model.impl.Not;

public class NotTest {
	private Not n;

	@Before
	public void init() {
		n = new Not(ICondition.TRUE);
	}

	@Test
	public void testNotICondition() throws CircleInConditionException {
		n = new Not(ICondition.TRUE);
		assertFalse(n.evaluate());
		n = new Not(ICondition.FALSE);
		assertTrue(n.evaluate());
	}

	@Test
	public void testToString() {
		// tests tostring for default not. DEPRECATED because empty NOT is now allowed
		// anymore
//		assertEquals("!", n.toString());
		// tests toString for a not with a set value
		n = new Not(ICondition.TRUE);
		assertEquals("!true", n.toString());
	}

	@Test
	public void testEval() throws CircleInConditionException {
		// tests default evaluation of Not
		assertFalse(n.evaluate());
	}

}
