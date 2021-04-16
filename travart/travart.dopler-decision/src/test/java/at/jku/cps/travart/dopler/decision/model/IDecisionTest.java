package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.ADecision.DecisionType;
import at.jku.cps.travart.dopler.decision.model.impl.AllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

@RunWith(Parameterized.class)
public class IDecisionTest {
	@SuppressWarnings("rawtypes")
	private final ADecision dec;

	@SuppressWarnings("rawtypes")
	public IDecisionTest(final ADecision myDec) {
		dec = myDec;
	}

	@Test
	public void testIsSelected() {
		assertFalse(dec.isSelected());
	}

	@Test
	public void testSetSelected() {
		assertFalse(dec.isSelected());
		dec.setSelected(true);
		assertTrue(dec.isSelected());
		dec.setSelected(false);
		assertFalse(dec.isSelected());
	}

	@Test
	public void testGetId() {
		assertEquals("test", dec.getId());
	}

	@Test
	public void testSetGetQuestion() {
		assertEquals("", dec.getQuestion());
		String t = "test";
		dec.setQuestion(t);
		assertEquals(t, dec.getQuestion());
	}

	@Test
	public void testSetGetDescription() {
		assertEquals("", dec.getDescription());
		String t = "test";
		dec.setDescription(t);
		assertEquals(t, dec.getDescription());
	}

	@Test
	public void testGetType() {
		assertNotNull(dec.getType());
	}

	@Test
	public void testSetType() {
		dec.setType(DecisionType.BOOLEAN);
		assertEquals(DecisionType.BOOLEAN, dec.getType());
		dec.setType(DecisionType.DOUBLE);
		assertEquals(DecisionType.DOUBLE, dec.getType());
		dec.setType(DecisionType.ENUM);
		assertEquals(DecisionType.ENUM, dec.getType());
		dec.setType(DecisionType.STRING);
		assertEquals(DecisionType.STRING, dec.getType());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGetRange() {
		if (dec instanceof BooleanDecision) {
			Range r = new Range();
			r.add(BooleanValue.getFalse());
			r.add(BooleanValue.getTrue());
			assertEquals(r, dec.getRange());
		} else if (dec instanceof StringDecision) {
			assertNull(dec.getRange());
		} else {
			assertNotNull(dec.getRange());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetRangeValue() throws RangeValueException {
		dec.reset();
		if (dec instanceof BooleanDecision) {
			assertNotNull(dec.getRangeValue(false));
		} else if (dec instanceof StringDecision) {
			assertEquals(new StringValue("test"), dec.getRangeValue("test"));
		} else if (dec instanceof NumberDecision) {
			DoubleValue dv = new DoubleValue(0d);
			dec.getRange().add(dv);
			assertEquals(new DoubleValue(0d), dec.getRangeValue(0d));
		} else {
			assertNull(dec.getRangeValue("test"));
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testContainsRangeValue() {
		assertFalse(dec.containsRangeValue(new StringValue("test")));
	}

	@Test
	public void testGetRules() {
		assertTrue(dec.getRules().isEmpty());
	}

	@Test
	public void testExecuteRules() {
		try {
			dec.executeRules();
		} catch (ActionExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testAddRule() throws RangeValueException {
		Rule r = new Rule(ICondition.TRUE, new AllowAction(dec, new StringValue("test")));
		dec.addRule(r);
		assertTrue(dec.getRules().contains(r));
		dec.getRules().clear();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddRules() {
		Set<Rule> sr = new HashSet<>();
		Rule r1 = new Rule(ICondition.TRUE, new SetValueAction(dec, new StringValue("test")));
		Rule r2 = new Rule(ICondition.TRUE, new SetValueAction(dec, new StringValue("test2")));
		Rule r3 = new Rule(ICondition.FALSE, new SetValueAction(dec, new StringValue("test3")));
		sr.add(r1);
		sr.add(r2);
		sr.add(r3);
		dec.addRules(sr);
		assertTrue(dec.getRules().contains(r1) && dec.getRules().contains(r2) && dec.getRules().contains(r3));
		dec.getRules().clear();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRemoveRule() {
		Set<Rule> sr = new HashSet<>();
		Rule r1 = new Rule(ICondition.TRUE, new SetValueAction(dec, new StringValue("test")));
		Rule r2 = new Rule(ICondition.TRUE, new SetValueAction(dec, new StringValue("test2")));
		Rule r3 = new Rule(ICondition.FALSE, new SetValueAction(dec, new StringValue("test3")));
		sr.add(r1);
		sr.add(r2);
		sr.add(r3);
		dec.addRules(sr);
		assertTrue(dec.getRules().contains(r1) && dec.getRules().contains(r2) && dec.getRules().contains(r3));
		assertTrue(dec.removeRule(r1));
		assertTrue(dec.removeRule(r2));
		assertTrue(dec.removeRule(r3));
		assertTrue(dec.getRules().isEmpty());
		dec.getRules().clear();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSetRules() {
		Set<Rule> sr = new HashSet<>();
		Rule r1 = new Rule(ICondition.TRUE, new SetValueAction(dec, new StringValue("test")));
		Rule r2 = new Rule(ICondition.TRUE, new SetValueAction(dec, new StringValue("test2")));
		Rule r3 = new Rule(ICondition.FALSE, new SetValueAction(dec, new StringValue("test3")));
		Rule r4 = new Rule(ICondition.FALSE, new SetValueAction(dec, new StringValue("test4")));
		dec.addRule(r4);
		assertTrue(dec.getRules().contains(r4));
		sr.add(r1);
		sr.add(r2);
		sr.add(r3);
		dec.setRules(sr);
		assertTrue(!dec.getRules().contains(r4) && dec.getRules().contains(r1) && dec.getRules().contains(r2)
				&& dec.getRules().contains(r3));
		dec.getRules().clear();
	}

	@Test
	public void testGetVisibility() {
		assertNotNull(dec.getVisiblity());
	}

	@Test
	public void testSetVisibility() {
		dec.setVisibility(ICondition.TRUE);
		assertEquals(ICondition.TRUE, dec.getVisiblity());
	}

	@Test
	public void testReset() throws Throwable {
		dec.reset();
		if (dec instanceof BooleanDecision) {
			assertNotNull(dec.getValue());

		} else if (dec instanceof NumberDecision) {
			dec.getValue();
			// just don't fail
		} else if (dec instanceof StringDecision) {
			assertEquals(new StringValue(""), dec.getValue());
		} else {
//			ThrowingRunnable r = () -> {
			assertEquals(new StringValue(" "), dec.getValue());
//			};
//			assertThrows(NoSuchElementException.class, r);
		}
//		assertFalse(dec.hasNoneOption());
	}

	@SuppressWarnings("rawtypes")
	@Parameterized.Parameters
	public static Collection<IDecision> instancesToTest() {
		return Arrays.<IDecision>asList(new BooleanDecision("test"), new EnumDecision("test"),
				new NumberDecision("test"), new StringDecision("test"));
	}
}
