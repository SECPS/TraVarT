package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.Or;

public class ABinaryVisibilityTest {

	@Test(expected = NullPointerException.class)
	public void testNullLeft() {
		@SuppressWarnings("unused")
		ABinaryCondition vis = new ABinaryCondition(null, new Or(ICondition.TRUE, ICondition.TRUE)) {

			@Override
			public boolean evaluate() {
				return false;
			}
		};
	}

	@Test(expected = NullPointerException.class)
	public void testNullRight() {
		@SuppressWarnings("unused")
		ABinaryCondition vis = new ABinaryCondition(new Or(ICondition.TRUE, ICondition.TRUE), null) {
			@Override
			public boolean evaluate() {
				return false;
			}
		};
	}

	@Test
	public void testABinaryVisibility() {
		ABinaryCondition vis = new ABinaryCondition(new Or(ICondition.TRUE, ICondition.TRUE),
				new Or(ICondition.TRUE, ICondition.TRUE)) {

			@Override
			public boolean evaluate() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		assertNotNull(vis.getLeft());
		assertNotNull(vis.getRight());
	}

	@Test(expected = NullPointerException.class)
	public void testSetLeftNull() throws CircleInConditionException {
		ABinaryCondition vis = new Or(ICondition.TRUE, ICondition.TRUE);
		vis.setLeft(null);
	}

	@Test(expected = NullPointerException.class)
	public void testSetRightNull() throws CircleInConditionException {
		ABinaryCondition vis = new Or(ICondition.TRUE, ICondition.TRUE);
		vis.setRight(null);
	}

	@Test
	public void testHashCode() throws CircleInConditionException {
		ABinaryCondition vis = new ABinaryCondition(ICondition.TRUE, ICondition.TRUE) {

			@Override
			public boolean evaluate() {
				return false;
			}
		};
		ABinaryCondition vis2 = new ABinaryCondition(ICondition.TRUE, ICondition.TRUE) {

			@Override
			public boolean evaluate() {
				return false;
			}
		};
		vis.setLeft(new Or(ICondition.TRUE, ICondition.TRUE));
		assertNotEquals(vis.hashCode(), vis2.hashCode());
		assertEquals(vis.hashCode(), vis.hashCode());
		vis2.setLeft(new Or(ICondition.TRUE, ICondition.TRUE));
		assertEquals(vis.hashCode(), vis2.hashCode());
		vis2.setRight(new Or(ICondition.TRUE, ICondition.TRUE));
		assertNotEquals(vis.hashCode(), vis2.hashCode());
		vis.setRight(new Or(ICondition.TRUE, ICondition.TRUE));
		assertEquals(vis.hashCode(), vis2.hashCode());

	}

	@Test
	public void testEquals() throws CircleInConditionException {
		ABinaryCondition vis = new Or(ICondition.TRUE, ICondition.TRUE);
		ABinaryCondition vis2 = new Or(ICondition.TRUE, ICondition.TRUE);
		ABinaryCondition vis3 = new And(ICondition.TRUE, ICondition.TRUE);
		assertFalse(vis.equals(vis3));
		vis2.setLeft(new Or(ICondition.TRUE, ICondition.TRUE));
		assertFalse(vis.equals(null));
		assertTrue(vis.equals(vis));
		assertFalse(vis.equals(vis2));
		vis.setLeft(new Or(ICondition.TRUE, ICondition.TRUE));
		assertTrue(vis.equals(vis2));
		vis2.setRight(new Or(ICondition.TRUE, ICondition.TRUE));
		assertFalse(vis.equals(vis2));
		vis.setRight(new Or(ICondition.TRUE, ICondition.TRUE));
		assertTrue(vis.equals(vis2));
		vis.setLeft(new And(ICondition.TRUE, ICondition.TRUE));
		assertFalse(vis.equals(vis2));
		vis.setLeft(new Or(ICondition.TRUE, ICondition.TRUE));
		vis.setRight(new And(ICondition.TRUE, ICondition.TRUE));
		assertFalse(vis.equals(vis2));
	}

	@Test
	public void testIsVisible() throws CircleInConditionException {
		ABinaryCondition vis = new ABinaryCondition(ICondition.FALSE, ICondition.FALSE) {

			@Override
			public boolean evaluate() {
				if (getLeft() != null && getRight() != null) {
					return getLeft().evaluate() && getRight().evaluate();
				}
				return false;
			}
		};
		assertFalse(vis.evaluate());
		vis.setRight(ICondition.TRUE);
		vis.setLeft(ICondition.TRUE);
		assertTrue(vis.evaluate());
	}
}
