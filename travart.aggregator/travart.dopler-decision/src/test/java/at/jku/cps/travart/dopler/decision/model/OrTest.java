package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;
import at.jku.cps.travart.dopler.decision.model.impl.Or;

public class OrTest {
	private Or or;

	@Before
	public void init() {
		or = new Or(ICondition.TRUE, ICondition.TRUE);
	}

	@Test
	public void testToString() throws CircleInConditionException {
		assertEquals(or.getLeft() + " || " + or.getRight(), or.toString());
		or.setRight(ICondition.TRUE);
		or.setLeft(ICondition.TRUE);
		assertEquals(or.getLeft().toString() + " || " + or.getRight().toString(), or.toString());
	}

	@Test
	public void testEval() throws CircleInConditionException {
		// not possible anymore, because left and right are not allowed to be null
//		ThrowingRunnable runner = () -> or.eval();
//		Assert.assertThrows(NullPointerException.class, runner);
		or.setRight(ICondition.TRUE);
		or.setLeft(ICondition.TRUE);
		assertTrue(or.evaluate());
		or.setRight(ICondition.FALSE);
		or.setLeft(ICondition.FALSE);
		assertFalse(or.evaluate());
		or.setRight(ICondition.FALSE);
		or.setLeft(ICondition.TRUE);
		assertTrue(or.evaluate());
		or.setRight(ICondition.TRUE);
		or.setLeft(ICondition.FALSE);
		assertTrue(or.evaluate());
	}

}
