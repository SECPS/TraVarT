package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.CircleInConditionException;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.Not;
import at.jku.cps.travart.dopler.decision.model.impl.Or;

public class AUnaryVisibilityTest {
	AUnaryCondition vis;

	@Before
	public void setUp() throws Exception {
		vis = new Not(ICondition.TRUE);
	}

	@Test
	public void testHashCode() throws CircleInConditionException {
		AUnaryCondition vis2 = new Not(ICondition.TRUE);
		assertEquals(vis.hashCode(), vis2.hashCode());
		vis.setOperand(new And(ICondition.TRUE, ICondition.TRUE));
		assertNotEquals(vis.hashCode(), vis2.hashCode());
		vis2.setOperand(new And(ICondition.TRUE, ICondition.TRUE));
		assertEquals(vis.hashCode(), vis2.hashCode());
	}

	@Test
	public void testAUnaryVisibility() {
		assertNotNull(vis.getOperand());
	}

	@Test(expected = NullPointerException.class)
	public void testAUnaryVisibilityICondition_NullCondition() {
		vis = new AUnaryCondition(null) {
			@Override
			public boolean evaluate() {
				return false;
			}
		};
	}

	@Test
	public void testAUnaryVisibilityICondition() {
		vis = new AUnaryCondition(new And(ICondition.TRUE, ICondition.TRUE)) {
			@Override
			public boolean evaluate() {
				return false;
			}
		};
	}

	@Test
	public void testGetOperand() throws CircleInConditionException {
		vis.setOperand(new And(ICondition.TRUE, ICondition.TRUE));
		assertEquals(new And(ICondition.TRUE, ICondition.TRUE), vis.getOperand());
	}

	@Test(expected = NullPointerException.class)
	public void testSetOperand() throws CircleInConditionException {
		vis.setOperand(null);
	}

	@Test
	public void testIsVisible() throws CircleInConditionException {
		vis = new AUnaryCondition(new And(ICondition.TRUE, ICondition.TRUE)) {
			@Override
			public boolean evaluate() {
				return getOperand().evaluate();
			}
		};
		vis.setOperand(ICondition.FALSE);
		assertFalse(vis.evaluate());
		vis.setOperand(ICondition.TRUE);
		assertTrue(vis.evaluate());
	}

	@Test
	public void testEqualsObject() throws CircleInConditionException {
		assertFalse(vis.equals(null));
		assertFalse(vis.equals("a String"));
		assertTrue(vis.equals(vis));
		AUnaryCondition vis2 = new Not(ICondition.TRUE);
		assertTrue(vis.equals(vis2));
		vis2.setOperand(new Or(ICondition.TRUE, ICondition.TRUE));
		assertFalse(vis.equals(vis2));
		vis.setOperand(new And(ICondition.TRUE, ICondition.TRUE));
		assertFalse(vis.equals(vis2));
		vis2.setOperand(new And(ICondition.TRUE, ICondition.TRUE));
		assertTrue(vis.equals(vis2));
	}

}
