package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.SelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;

public class SelectDecisionActionTest {
	private SelectDecisionAction s;
	private ADecision<Boolean> d;

	@Before
	public void setUp() throws Exception {
		d = new BooleanDecision("test");
		d.setValue(BooleanValue.getFalse());
		s = new SelectDecisionAction(d);
	}

	@Test
	public void testExecute() throws ActionExecutionException {
		s.execute();
		assertTrue(d.getValue().getValue());
		s.execute();
		assertTrue(d.getValue().getValue());
	}

	@Test
	public void testIsSatisfied() throws ActionExecutionException {
		assertFalse(s.isSatisfied());
		s.execute();
		assertTrue(s.isSatisfied());
		s = new SelectDecisionAction(d);
		assertTrue(s.isSatisfied());
		s.execute();
		assertTrue(s.isSatisfied());
	}

	@Test(expected = NullPointerException.class)
	public void testSelectDecisionAction() {
		new SelectDecisionAction(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testEquals() {
		assertFalse(s.equals(null));
		assertFalse(s.equals("aString"));
		assertTrue(s.equals(s));
		SelectDecisionAction s2 = new SelectDecisionAction(d);
		assertTrue(s.equals(s2));
		ADecision d2 = new StringDecision("test2");
		SelectDecisionAction s3 = new SelectDecisionAction(d2);
		assertFalse(s.equals(s3));
	}

}
