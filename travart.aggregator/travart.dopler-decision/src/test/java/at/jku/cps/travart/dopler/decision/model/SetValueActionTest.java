package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.exc.UnsatisfiedCardinalityException;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

public class SetValueActionTest {
	private SetValueAction sva;
	private SetValueAction sva2;
	private EnumDecision dec;
	private StringValue sv;
	private StringValue sv2;

	@Before
	public void setUp() throws Exception {
		Range<String> r = new Range<>();
		dec = new EnumDecision("test");
		sv = new StringValue("test");
		sv2 = new StringValue("test2");
		r.add(sv);
		r.add(sv2);
		sv.enable();
		sv2.enable();
		dec.setCardinality(new Cardinality(0, 2));
		dec.setRange(r);
		sva = new SetValueAction(dec, sv);
		sva2 = new SetValueAction(dec, sv2);
	}

	@Test
	public void testExecute() throws ActionExecutionException {
		sva.execute();
		assertEquals(sv, dec.getValue());
		assertNotEquals(sv2, dec.getValue());
	}

	@Test
	public void testExecuteEnumDecisionAlternative() throws ActionExecutionException {
		dec.setCardinality(new Cardinality(1, 1));
		sva.execute();
		assertEquals(sv, dec.getValue());
	}

	@Test
	public void testExecuteEnumDecisionMultiples() throws ActionExecutionException, RangeValueException {
		dec.setValue(sv2);
		sva.execute();
		assertTrue("Should contain old and new value after execution.",
				dec.getValues().contains(sv) && dec.getValues().contains(sv2));
	}

	@Test(expected = ActionExecutionException.class)
	public void testExecuteEnumDecisionMultiplesExceedingCardinality()
			throws ActionExecutionException, RangeValueException {
		dec.setCardinality(new Cardinality(0, 1));
		dec.setValue(sv2);
		sva.execute();
	}

	@Test(expected = ClassCastException.class)
	public void testExecuteNumberDecisionStringValue() throws ActionExecutionException {
		NumberDecision nd = new NumberDecision("test");
		sva = new SetValueAction(nd, sv);
		sva.execute();
	}
	
	@Test(expected = NullPointerException.class)
	public void testExecuteNumberDecisionNull() throws ActionExecutionException {
		NumberDecision nd = new NumberDecision("test");
		sva = new SetValueAction(nd, null);
		sva.execute();
	}
	
	@Test
	public void testisSatsifiedEnumDecisionAlternative() throws ActionExecutionException {
		dec.setCardinality(new Cardinality(1, 1));
		sva.execute();
		assertTrue(sva.isSatisfied());
	}
	
	@Test
	public void testisSatsifiedEnumDecisionAlternativeNotPerformed() throws ActionExecutionException {
		dec.setCardinality(new Cardinality(1, 1));
		assertFalse(sva.isSatisfied());
	}
	
	@Test(expected = NullPointerException.class)
	public void testIsSatisfiedNumberDecisionNull() throws ActionExecutionException {
		NumberDecision nd = new NumberDecision("test");
		sva = new SetValueAction(nd, null);
		sva.isSatisfied();
	}
	
	@Test
	public void testIsSatisfiedNumberDecisionStringValue() throws ActionExecutionException {
		NumberDecision nd = new NumberDecision("test");
		sva = new SetValueAction(nd, sv);
		assertFalse(sva.isSatisfied());
	}
	
	@Test
	public void testExecuteNumberDecisionDoubleValue() throws ActionExecutionException {
		NumberDecision nd = new NumberDecision("test");
		sva = new SetValueAction(nd, new DoubleValue(5));
		sva.execute();
		assertEquals(new DoubleValue(5),nd.getValue());
	}

	@Test
	public void testIsSatisfied() throws ActionExecutionException {
		assertFalse(sva.isSatisfied());
		assertFalse(sva2.isSatisfied());
		sva.execute();
		assertTrue(sva.isSatisfied());
		assertFalse(sva2.isSatisfied());
	}
	
	@Test
	public void testGetVariable() {
		assertEquals(dec,sva.getVariable());
	}

	@Test(expected = NullPointerException.class)
	public void testSetValueAction_NullDecision() {
		new SetValueAction(null, sv);
	}

	@Test(expected = NullPointerException.class)
	public void testSetValueAction_NullAction() {
		new SetValueAction(dec, null);
	}

	@Test
	public void testEquals() {
		assertFalse(sva.equals(null));
		assertTrue(sva.equals(sva));
		assertFalse(sva.equals("a String"));
		assertFalse(sva.equals(sva2));
		sva2 = new SetValueAction(new EnumDecision("other"), sv);
		assertFalse(sva.equals(sva2));
		sva2 = new SetValueAction(dec, sv);
		assertTrue(sva.equals(sva2));
	}

	@Test
	public void testToString() {
		assertNotNull(sva.toString());
		assertEquals(dec.toString() + " = " + sva.getValue().toString() + ";", sva.toString());
	}

}
