package at.jku.cps.travart.dopler.decision.model;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import at.jku.cps.travart.dopler.decision.exc.ActionExecutionException;
import at.jku.cps.travart.dopler.decision.model.impl.AllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.DeSelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.DisAllowAction;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.SelectDecisionAction;
import at.jku.cps.travart.dopler.decision.model.impl.SetValueAction;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

@RunWith(Parameterized.class)
public class IActionTest {
	private final IAction a;
	private static EnumDecision e = new EnumDecision("test");
	private static BooleanDecision b = new BooleanDecision("test");

	public IActionTest(final IAction ac) {
		a = ac;
	}

	@Test
	public void testExecute() {
		// just process without an error
		try {
			a.execute();
		} catch (ActionExecutionException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIsSatisfied() throws ActionExecutionException {
		// just process without an error
		a.isSatisfied();
	}

	@Test
	public void testGetVariable() {
		// just process without an error
		a.getVariable();
	}

	@Test
	public void testGetValue() {
		// just process without an error
		a.getValue();
	}

	@Parameterized.Parameters
	public static Collection<IAction> instancesToTest() {
		Range<String> r = new Range<>();
		StringValue s = new StringValue("test");
		r.add(s);
		e.setRange(r);
		return Arrays.<IAction>asList(new AllowAction(e, s), new DeSelectDecisionAction(b), new SelectDecisionAction(b),
				new DisAllowAction(e, s), new SetValueAction(e, s));
	}

}
