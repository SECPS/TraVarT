package at.jku.cps.travart.dopler.common;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.dopler.decision.factory.impl.DecisionModelFactory;
import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.ARangeValue;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.AllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.And;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.DecisionValueCondition;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Equals;
import at.jku.cps.travart.dopler.decision.model.impl.GetValueFunction;
import at.jku.cps.travart.dopler.decision.model.impl.Greater;
import at.jku.cps.travart.dopler.decision.model.impl.GreaterEquals;
import at.jku.cps.travart.dopler.decision.model.impl.Less;
import at.jku.cps.travart.dopler.decision.model.impl.LessEquals;
import at.jku.cps.travart.dopler.decision.model.impl.Not;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Or;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;
import at.jku.cps.travart.dopler.decision.exc.*;

public class DecisionModelUtilsTest {
	private DecisionModelFactory factory;
	private DecisionModel dm;

	@Before
	public void setUp() throws Exception {
		factory = DecisionModelFactory.getInstance();
		dm = factory.create();
		dm.setName("TestModel");

	}

	@Test(expected = ConditionCreationException.class)
	public void testConsumeToBinaryConditionNoDecisions()
			throws CircleInConditionException, ConditionCreationException {
		List<IDecision> lDec = new LinkedList<>();
		DecisionModelUtils.consumeToBinaryCondition(lDec, null, false);
	}

	@Test
	public void testConsumeToBinaryConditionOnlyOneBooleanDecision()
			throws CircleInConditionException, ConditionCreationException {
		List<IDecision> lDec = new LinkedList<>();
		lDec.add(new BooleanDecision("bd"));
		ICondition d1 = DecisionModelUtils.consumeToBinaryCondition(lDec, null, false);
		assertFalse(d1.evaluate());

		ICondition d2 = DecisionModelUtils.consumeToBinaryCondition(lDec, null, true);
		assertTrue(d2.evaluate());
	}

	@Test(expected = ConditionCreationException.class)
	public void testConsumeToBinaryConditionOnlyOneNullDecision()
			throws CircleInConditionException, ConditionCreationException {
		List<IDecision> lDec = new LinkedList<>();
		lDec.add(null);
		ICondition d1 = DecisionModelUtils.consumeToBinaryCondition(lDec, null, false);
		assertFalse(d1.evaluate());
	}

	// TODO check if there should be DeMorgan implemented here.
	// With this function only the individual decisions in the list are negated
	// but not the operators. If the negative flag is supposed to negate the entire
	// consumedbinarycondition linked together by the passed BinaryCondition the
	// result will not be correct due to breaking of DeMorgan
	@Test
	public void testConsumeToBinaryConditionTwoDecisionsAnd()
			throws CircleInConditionException, ConditionCreationException {
		List<IDecision> lDec = new LinkedList<>();
		lDec.add(new BooleanDecision("bd1"));
		lDec.add(new BooleanDecision("bd2"));
		ICondition d1 = DecisionModelUtils.consumeToBinaryCondition(lDec, And.class, false);
		assertFalse(d1.evaluate());

		lDec.add(new BooleanDecision("bd1"));
		lDec.add(new BooleanDecision("bd2"));
		ICondition d2 = DecisionModelUtils.consumeToBinaryCondition(lDec, And.class, true);
		assertTrue(d2.evaluate());
	}

	@Test(expected = ConditionCreationException.class)
	public void testConsumeToBinaryConditionOneDecisionOneNullAnd()
			throws CircleInConditionException, ConditionCreationException {
		List<IDecision> lDec = new LinkedList<>();
		lDec.add(new BooleanDecision("bd1"));
		lDec.add(null);
		ICondition d1 = DecisionModelUtils.consumeToBinaryCondition(lDec, And.class, false);
	}

	@Test
	public void testConsumeToBinaryConditionMultipleDecisionsAnd()
			throws CircleInConditionException, ConditionCreationException {
		List<IDecision> lDec = new LinkedList<>();
		lDec.add(new BooleanDecision("bd1"));
		lDec.add(new BooleanDecision("bd2"));
		lDec.add(new BooleanDecision("bd3"));
		lDec.add(new BooleanDecision("bd4"));
		ICondition d1 = DecisionModelUtils.consumeToBinaryCondition(lDec, And.class, false);
		assertFalse(d1.evaluate());

		lDec.add(new BooleanDecision("bd1"));
		lDec.add(new BooleanDecision("bd2"));
		lDec.add(new BooleanDecision("bd3"));
		lDec.add(new BooleanDecision("bd4"));
		ICondition d2 = DecisionModelUtils.consumeToBinaryCondition(lDec, And.class, true);
		assertTrue(d2.evaluate());
	}

	@Test
	public void testDetectCircleToAddTrivial() {
		assertFalse(DecisionModelUtils.detectCircle(null, ICondition.TRUE));
		assertFalse(DecisionModelUtils.detectCircle(null, ICondition.FALSE));
	}

	@Test
	public void testDetectCircleConditionTrivial() {
		assertFalse(DecisionModelUtils.detectCircle(ICondition.TRUE, null));
		assertFalse(DecisionModelUtils.detectCircle(ICondition.FALSE, null));
	}

	@Test
	public void testDetectCircleAddingSelf() {
		ICondition c = new And(ICondition.TRUE, ICondition.FALSE);
		assertTrue(DecisionModelUtils.detectCircle(c, c));
	}

	@Test
	public void testDetectCircleAddingNegatedSelf() {
		ICondition c = new And(ICondition.TRUE, ICondition.FALSE);
		Not nc = new Not(c);
		assertTrue(DecisionModelUtils.detectCircle(nc, c));
	}

	@Test
	public void testDetectCircleAddingNegatedSelfLowerLayer() {
		ICondition c = new And(ICondition.TRUE, ICondition.FALSE);
		ICondition cO = new Or(c, ICondition.TRUE);
		assertTrue(DecisionModelUtils.detectCircle(cO, c));
		cO = new Or(ICondition.TRUE, c);
		assertTrue(DecisionModelUtils.detectCircle(cO, c));
	}

	@Test
	public void testGetBooleanDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		BooleanDecision bd2 = new BooleanDecision("bd2");
		sd.add(bd1);
		sd.add(bd2);
		sd.add(new NumberDecision("nd1"));
		sd.add(new EnumDecision("ed1"));
		sd.add(new StringDecision("sd1"));
		dm.addAll(sd);
		Set<BooleanDecision> cd = DecisionModelUtils.getBooleanDecisions(dm);
		assertTrue(cd.size() == 2 && cd.contains(bd1) && cd.contains(bd2));
	}

	@Test
	public void testGetBooleanDecisionsAsNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		BooleanDecision bd2 = new BooleanDecision("bd2");
		sd.add(bd1);
		sd.add(bd2);
		sd.add(new NumberDecision("nd1"));
		sd.add(new EnumDecision("ed1"));
		sd.add(new StringDecision("sd1"));
		dm.addAll(sd);
		Set<String> cd = DecisionModelUtils.getBooleanDecisionsAsNames(dm);
		assertTrue(cd.size() == 2 && cd.contains(bd1.getName()) && cd.contains(bd2.getName()));
	}

	@Test
	public void testGetDecisionNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");		
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<String> cd = DecisionModelUtils.getDecisionNames(dm);
		assertTrue(cd.size() == 5 && cd.contains(sd1.getName()) && cd.contains(ed1.getName())
				&& cd.contains(nd1.getName()) && cd.contains(bd1.getName()) && cd.contains(bd2.getName()));

	}

	@Test
	public void testGetEnumDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");
		
		sd.add(bd1);
		sd.add(bd2);
		sd.add(new NumberDecision("nd1"));
		sd.add(ed1);
		sd.add(new StringDecision("sd1"));
		dm.addAll(sd);
		Set<EnumDecision> cd = DecisionModelUtils.getEnumDecisions(dm);
		assertTrue(cd.size() == 1 && cd.contains(ed1));
	}

	@Test
	public void testGetEnumDecisionsAsNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");
		
		sd.add(bd1);
		sd.add(bd2);
		sd.add(new NumberDecision("nd1"));
		sd.add(ed1);
		sd.add(new StringDecision("sd1"));
		dm.addAll(sd);
		Set<String> cd = DecisionModelUtils.getEnumDecisionsAsNames(dm);
		assertTrue(cd.size() == 1 && cd.contains(ed1.getName()));
	}

	@Test
	public void testGetNumberDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");		
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<NumberDecision> cd = DecisionModelUtils.getNumberDecisions(dm);
		assertTrue(cd.size() == 1 && cd.contains(nd1));

	}

	@Test
	public void testGetNumberDecisionsAsNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");		
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<String> cd = DecisionModelUtils.getNumberDecisionsAsNames(dm);
		assertTrue(cd.size() == 1 && cd.contains(nd1.getName()));
	}

	@Test
	public void testGetReachableDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<IDecision> cd = DecisionModelUtils.getReachableDecisions(dm);
		assertTrue(cd.size() == 2 && cd.contains(nd1) && cd.contains(ed1));
	}

	@Test
	public void testGetReachableDecisionsAsNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<String> cd = DecisionModelUtils.getReachableDecisionsAsNames(dm);
		assertTrue(cd.size() == 2 && cd.contains(nd1.getName()) && cd.contains(ed1.getName()));
	}

	@Test
	public void testGetSelectableDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<IDecision> cd = DecisionModelUtils.getSelectableDecisions(dm);
		assertTrue(cd.size() == 3 && cd.contains(bd1) && cd.contains(nd1) && cd.contains(ed1));
	}

	
	@Test
	public void testGetSelectableDecisionsAsNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<String> cd = DecisionModelUtils.getSelectableDecisionsAsNames(dm);
		assertTrue(cd.size() == 3 && cd.contains(bd1.getName()) && cd.contains(nd1.getName()) && cd.contains(ed1.getName()));
	}

	@Test
	public void testGetSelectedDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		List<IDecision> cd = DecisionModelUtils.getSelectedDecisions(dm);
		assertTrue(cd.size() == 1 && cd.contains(bd1));
	}

	@Test
	public void testGetSelectedDecisionsAsNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		List<String> cd = DecisionModelUtils.getSelectedDecisionsAsNames(dm);
		assertTrue(cd.size() == 1 && cd.contains(bd1.getName()));
	}

	@Test
	public void testGetStringDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<StringDecision> cd = DecisionModelUtils.getStringDecisions(dm);
		assertTrue(cd.size() == 1 && cd.contains(sd1));
	}

	@Test
	public void testGetStringDecisionsAsNames() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		Set<String> cd = DecisionModelUtils.getStringDecisionsAsNames(dm);
		assertTrue(cd.size() == 1 && cd.contains(sd1.getName()));
	}

	@Test
	public void testHasReachableDecisions() {
		Set<IDecision<?>> sd = new LinkedHashSet<>();
		BooleanDecision bd1 = new BooleanDecision("bd1");
		bd1.setSelected(true);
		BooleanDecision bd2 = new BooleanDecision("bd2");
		EnumDecision ed1 = new EnumDecision("ed1");	
		bd2.setVisibility(ICondition.FALSE);
		NumberDecision nd1 = new NumberDecision("nd1");
		StringDecision sd1 = new StringDecision("sd1");
		sd1.setVisibility(ICondition.FALSE);
		sd.add(bd1);
		sd.add(bd2);
		sd.add(nd1);
		sd.add(ed1);
		sd.add(sd1);
		dm.addAll(sd);
		assertTrue(DecisionModelUtils.hasReachableDecisions(dm));
		dm.remove(bd1);
		dm.remove(nd1);
		dm.remove(ed1);
		assertFalse(DecisionModelUtils.hasReachableDecisions(dm));
	}

	@Test
	public void testIsBinaryCondition() {
		assertFalse(DecisionModelUtils.isBinaryCondition(null));
		assertTrue(DecisionModelUtils.isBinaryCondition(new And(new BooleanDecision("bd"),new BooleanDecision("bd"))));
		assertFalse(DecisionModelUtils.isBinaryCondition(new GetValueFunction(new BooleanDecision("bd"))));
		assertFalse(DecisionModelUtils.isBinaryCondition(new BooleanDecision("bd")));
		assertFalse(DecisionModelUtils.isBinaryCondition(new StringValue("sv")));		
	}

	@Test
	public void testIsBooleanDecision() {
		assertTrue(DecisionModelUtils.isBooleanDecision(new BooleanDecision("bd")));
		assertFalse(DecisionModelUtils.isBooleanDecision(new StringDecision("sd")));
	}

	@Test
	public void testIsComplexCondition() {
		DecisionValueCondition dvc=new DecisionValueCondition(new BooleanDecision("bd"),BooleanValue.getFalse());
		assertFalse(DecisionModelUtils.isComplexCondition(dvc));
		assertFalse(DecisionModelUtils.isComplexCondition(ICondition.FALSE));
		Not n=new Not(ICondition.FALSE);
		assertTrue(DecisionModelUtils.isComplexCondition(n));
		And a=new And(ICondition.FALSE,ICondition.TRUE);
		assertTrue(DecisionModelUtils.isComplexCondition(a));
	}

	@Test
	public void testIsComplexVisibilityCondition() {
		assertFalse(DecisionModelUtils.isComplexVisibilityCondition(ICondition.FALSE));
		assertFalse(DecisionModelUtils.isComplexVisibilityCondition(ICondition.TRUE));
		And and=new And(ICondition.FALSE,ICondition.TRUE);
		assertTrue(DecisionModelUtils.isComplexVisibilityCondition(and));
		BooleanValue bv=BooleanValue.getTrue();
		assertFalse(DecisionModelUtils.isComplexVisibilityCondition(bv));
		bv=BooleanValue.getFalse();
		assertFalse(DecisionModelUtils.isComplexVisibilityCondition(bv));
	}

	@Test
	public void testIsDecision() {
		assertFalse(DecisionModelUtils.isDecision(null));
		assertFalse(DecisionModelUtils.isDecision(new Not(ICondition.FALSE)));
		assertFalse(DecisionModelUtils.isDecision(BooleanValue.getFalse()));
		assertFalse(DecisionModelUtils.isDecision(new And(new EnumDecision("1"),new EnumDecision("2"))));
		assertTrue(DecisionModelUtils.isDecision(new EnumDecision("test")));
	}

	@Test
	public void testIsDecisionValueCondition() {
		DecisionValueCondition dvc=new DecisionValueCondition(new BooleanDecision("bd"),BooleanValue.getFalse());
		Not ndvc=new Not(dvc);
		assertTrue(DecisionModelUtils.isDecisionValueCondition(dvc));
		assertTrue(DecisionModelUtils.isDecisionValueCondition(ndvc));
		assertFalse(DecisionModelUtils.isDecisionValueCondition(null));
		assertFalse(DecisionModelUtils.isDecisionValueCondition(new BooleanDecision("test")));
	}

	@Test
	public void testIsEnumDecision() {
		assertTrue(DecisionModelUtils.isEnumDecision(new EnumDecision("test")));
		assertFalse(DecisionModelUtils.isEnumDecision(new BooleanDecision("bd")));
		assertFalse(DecisionModelUtils.isEnumDecision(new NumberDecision("nd")));
		assertFalse(DecisionModelUtils.isEnumDecision(new StringDecision("sd")));		
	}

	@Test
	public void testIsEnumNoneOption() {
		assertFalse(DecisionModelUtils.isEnumNoneOption(new BooleanDecision("bd"),BooleanValue.getFalse()));
		assertFalse(DecisionModelUtils.isEnumNoneOption(new EnumDecision("ed"),new BooleanDecision("bd")));
		assertFalse(DecisionModelUtils.isEnumNoneOption(new EnumDecision("ed"),new StringValue("None")));
		EnumDecision ed=new EnumDecision("ed");
		ARangeValue<String> sv=ed.getNoneOption();
		ed.getRange().add(sv);
		assertTrue(DecisionModelUtils.isEnumNoneOption(ed, sv));
		EnumDecision ed2=new EnumDecision("ed2");
		ed2.getNoneOption();
		assertTrue(DecisionModelUtils.isEnumNoneOption(ed2, sv));
		
	}

	@Test
	public void testIsInItSelfRuleNotContained() {
		EnumDecision ed= new EnumDecision("ed");
		StringValue sv=new StringValue("sv");
		ed.getRange().add(sv);
		AllowAction aa=new AllowAction(ed,sv);
		Rule r=new Rule(ICondition.TRUE,aa);
		assertFalse(DecisionModelUtils.isInItSelfRule(r));
	}
	
	@Test
	public void testIsInItSelfRuleNotContainedButNegatedCondition() {
		EnumDecision ed= new EnumDecision("ed");
		StringValue sv=new StringValue("sv");
		ed.getRange().add(sv);
		AllowAction aa=new AllowAction(ed,sv);
		Rule r=new Rule(new Not(ICondition.TRUE),aa);
		assertFalse(DecisionModelUtils.isInItSelfRule(r));
	}
	
	@Test
	public void testIsInItSelfRuleIsItselfRule() {
		BooleanDecision bd= new BooleanDecision("bd");
		DisAllowAction daa=new DisAllowAction(bd,BooleanValue.getFalse());
		Rule r=new Rule(bd,daa);
		assertTrue(DecisionModelUtils.isInItSelfRule(r));
	}
	
	@Test
	public void testIsInItSelfRuleIsDecisionButNotItselfRule() {
		BooleanDecision bd= new BooleanDecision("bd");
		EnumDecision ed=new EnumDecision("ed");
		ed.getRange().add(ed.getNoneOption());
		DisAllowAction daa=new DisAllowAction(ed,ed.getNoneOption());
		Rule r=new Rule(bd,daa);
		assertFalse(DecisionModelUtils.isInItSelfRule(r));
	}

	@Test
	public void testIsMandatoryVisibilityConditionSimpleCondition() {
		assertFalse(DecisionModelUtils.isMandatoryVisibilityCondition(ICondition.FALSE));
		assertFalse(DecisionModelUtils.isMandatoryVisibilityCondition(ICondition.TRUE));
		Not n=new Not(ICondition.FALSE);
		assertFalse(DecisionModelUtils.isMandatoryVisibilityCondition(n));
		Or o=new Or(ICondition.FALSE,ICondition.TRUE);
		assertFalse(DecisionModelUtils.isMandatoryVisibilityCondition(o));
		And a= new And(ICondition.FALSE,new BooleanDecision("bd"));
		assertTrue(DecisionModelUtils.isMandatoryVisibilityCondition(a));
		a= new And(new BooleanDecision("bd"),ICondition.FALSE);
		assertTrue(DecisionModelUtils.isMandatoryVisibilityCondition(a));
		a= new And(new BooleanDecision("bd"),ICondition.TRUE);
		assertFalse(DecisionModelUtils.isMandatoryVisibilityCondition(a));
		a= new And(ICondition.FALSE,ICondition.FALSE);
		assertFalse(DecisionModelUtils.isMandatoryVisibilityCondition(a));				
	}

	@Test
	public void testIsNoneAction() {
		assertFalse(DecisionModelUtils.isNoneAction(new DeSelectDecisionAction(new BooleanDecision("bd"))));
		
		EnumDecision ed=new EnumDecision("ed");
		StringValue sv=new StringValue("sv");
		ed.getRange().add(sv);
		assertFalse(DecisionModelUtils.isNoneAction(new AllowAction(ed,sv)));
		
		ed.getRange().add(ed.getNoneOption());
		assertTrue(DecisionModelUtils.isNoneAction(new AllowAction(ed,ed.getNoneOption())));
	}

	@Test
	public void testIsNoneCondition() {
		assertFalse(DecisionModelUtils.isNoneCondition(null));
		
		
		EnumDecision ed=new EnumDecision("ed");
		StringValue sv = new StringValue("sd");
		ed.getRange().add(sv);
		
		DecisionValueCondition dvc= new DecisionValueCondition(ed,sv);
		assertFalse(DecisionModelUtils.isNoneCondition(dvc));
		ARangeValue<String> sv2=ed.getNoneOption();
		ed.getRange().add(sv2);
		dvc=new DecisionValueCondition(ed,sv2);
		assertTrue(DecisionModelUtils.isNoneCondition(dvc));
		Not n=new Not(dvc);
		assertTrue(DecisionModelUtils.isNoneCondition(n));		
	}

	@Test
	public void testIsNot() {
		assertTrue(DecisionModelUtils.isNot(new Not(new BooleanDecision("ed"))));
		assertFalse(DecisionModelUtils.isNot(new BooleanDecision("ed")));
	}

	@Test
	public void testPowerSetEmptySet() {
		Set<IDecision<?>> s=new HashSet<>();
		Set<Set<IDecision<?>>> s2=new HashSet<>();
		s2.add(s);
		assertEquals(s2,DecisionModelUtils.powerSet(s));		
	}
	
	@Test
	public void testPowerSetNoneEmptySet() {
		Set<IDecision<?>> s=new HashSet<>();
		BooleanDecision bd=new BooleanDecision("bd");
		EnumDecision ed=new EnumDecision("ed");
		s.add(bd);
		s.add(ed);
		Set<IDecision<?>> s0=new HashSet<>();
		Set<IDecision<?>> s1=new HashSet<>();
		s1.add(bd);
		Set<IDecision<?>> s2=new HashSet<>();
		s2.add(ed);
		Set<IDecision<?>> s3=new HashSet<>();
		s3.add(bd);
		s3.add(ed);
		Set<Set<IDecision<?>>> ps=new HashSet<>();
		ps.add(s0);
		ps.add(s1);
		ps.add(s2);
		ps.add(s3);
		assertEquals(ps,DecisionModelUtils.powerSet(s));
	}

	@Test
	public void testPowerSetWithMinAndMax() {
		Set<IDecision<?>> s=new HashSet<>();
		BooleanDecision bd=new BooleanDecision("bd");
		EnumDecision ed=new EnumDecision("ed");
		s.add(bd);
		s.add(ed);
		Set<IDecision<?>> s1=new HashSet<>();
		s1.add(bd);
		Set<IDecision<?>> s2=new HashSet<>();
		s2.add(ed);
		Set<Set<IDecision<?>>> ps=new HashSet<>();
		ps.add(s1);
		ps.add(s2);
		assertEquals(ps,DecisionModelUtils.powerSetWithMinAndMax(s,1,1));
	}

	@Test
	public void testRetriveConditionDecisions() {
		BooleanDecision bd1=new BooleanDecision("bd1");
		BooleanDecision bd2=new BooleanDecision("bd2");
		Not n=new Not(bd1);
		And a=new And(n,bd2);
		DecisionValueCondition dvc=new DecisionValueCondition(bd1,BooleanValue.getFalse());
		Or o=new Or(a,dvc);
		And a2=new And(dvc,o);
		Set<IDecision> s=new HashSet<>();
		s.add(bd1);
		s.add(bd2);
		assertEquals(s,DecisionModelUtils.retriveConditionDecisions(a2));		
	}
	
	@Test
	public void testRetriveConditionDecisionsNullCheck() {
		assertTrue("Not passing anything should return an empty set.",DecisionModelUtils.retriveConditionDecisions(null).isEmpty());		
	}

	@Test
	public void testRetriveFeatureNameIDecisionBooleanBooleanEnumDecFalseFalse() {
		EnumDecision ed=new EnumDecision("ed");
		assertEquals(ed.toString(),DecisionModelUtils.retriveFeatureName(ed, false, false));
	}
	
	@Test
	public void testRetriveFeatureNameIDecisionBooleanBooleanEnumDecTrueFalse() {
		EnumDecision ed=new EnumDecision("d_ed");
		assertEquals("ed",DecisionModelUtils.retriveFeatureName(ed, true, false));
	}
	
	@Test
	public void testRetriveFeatureNameIDecisionBooleanBooleanEnumDecFalseTrue() {
		EnumDecision ed=new EnumDecision("ed_2");
		assertEquals("ed",DecisionModelUtils.retriveFeatureName(ed, false, true));
	}
	
	@Test
	public void testRetriveFeatureNameIDecisionBooleanBooleanEnumDecTrueTrue() {
		EnumDecision ed=new EnumDecision("d_ed_2");
		assertEquals("ed",DecisionModelUtils.retriveFeatureName(ed, true, true));
	}

	@Test
	public void testRetriveFeatureNameStringBooleanBooleanFalseFalse() {
		String s= "myFeature";
		assertEquals(s,DecisionModelUtils.retriveFeatureName(s, false, false));
	}
	
	@Test
	public void testRetriveFeatureNameStringBooleanBooleanFalseTrue() {
		String s= "myFeature";
		assertEquals(s+"_0",DecisionModelUtils.retriveFeatureName(s, false, true));
	}
	
	@Test
	public void testRetriveFeatureNameStringBooleanBooleanTrueFalse() {
		String s= "myFeature";
		assertEquals("d_"+s,DecisionModelUtils.retriveFeatureName(s, true, false));
	}
	
	@Test
	public void testRetriveFeatureNameStringBooleanBooleanTrueTrue() {
		String s= "myFeature";
		assertEquals("d_"+s+"_0",DecisionModelUtils.retriveFeatureName(s, true, true));
	}

	@Test
	public void testIsCompareCondition() {
		assertTrue(DecisionModelUtils.isCompareCondition(new Equals(ICondition.FALSE,ICondition.FALSE)));
		assertTrue(DecisionModelUtils.isCompareCondition(new Greater(ICondition.FALSE,ICondition.FALSE)));
		assertTrue(DecisionModelUtils.isCompareCondition(new Less(ICondition.FALSE,ICondition.FALSE)));
		assertTrue(DecisionModelUtils.isCompareCondition(new GreaterEquals(ICondition.FALSE,ICondition.FALSE)));
		assertTrue(DecisionModelUtils.isCompareCondition(new LessEquals(ICondition.FALSE,ICondition.FALSE)));
		assertFalse(DecisionModelUtils.isCompareCondition(new Not(ICondition.FALSE)));
		assertFalse(DecisionModelUtils.isCompareCondition(new BooleanDecision("bd")));
		assertFalse(DecisionModelUtils.isCompareCondition(new StringValue("sd")));
		assertFalse(DecisionModelUtils.isCompareCondition(new GetValueFunction(new BooleanDecision("bd"))));
		assertFalse(DecisionModelUtils.isCompareCondition(new DecisionValueCondition(new BooleanDecision("bd"),BooleanValue.getFalse())));	
	}

	@Test(expected=IllegalArgumentException.class)
	public void testRetriveMandatoryVisibilityCondition() {
		DecisionModelUtils.retriveMandatoryVisibilityCondition(ICondition.FALSE);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetriveMandatoryVisibilityCondition2() {
		Not n=new Not(ICondition.FALSE);
		DecisionModelUtils.retriveMandatoryVisibilityCondition(n);	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetriveMandatoryVisibilityCondition3() {
		Or o=new Or(ICondition.FALSE,ICondition.TRUE);
		DecisionModelUtils.retriveMandatoryVisibilityCondition(o);
		
	}
	
	@Test
	public void testRetriveMandatoryVisibilityCondition4() {
		BooleanDecision bd=new BooleanDecision("bd");
		And a= new And(ICondition.FALSE,bd);
		assertEquals(bd,DecisionModelUtils.retriveMandatoryVisibilityCondition(a));
	}
	
	@Test
	public void testRetriveMandatoryVisibilityCondition5() {
		BooleanDecision bd=new BooleanDecision("bd");
		And a= new And(bd,ICondition.FALSE);
		assertEquals(bd,DecisionModelUtils.retriveMandatoryVisibilityCondition(a));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetriveMandatoryVisibilityCondition6() {
		BooleanDecision bd=new BooleanDecision("bd");
		And a= new And(bd,ICondition.TRUE);
		DecisionModelUtils.retriveMandatoryVisibilityCondition(a);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetriveMandatoryVisibilityCondition7() {
		And a=new And(ICondition.FALSE,ICondition.FALSE);
		DecisionModelUtils.retriveMandatoryVisibilityCondition(a);
	}

}
