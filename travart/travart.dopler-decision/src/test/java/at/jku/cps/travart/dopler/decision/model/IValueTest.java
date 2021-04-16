package at.jku.cps.travart.dopler.decision.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import at.jku.cps.travart.dopler.decision.exc.RangeValueException;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanValue;
import at.jku.cps.travart.dopler.decision.model.impl.DoubleValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;

@RunWith(Parameterized.class)
public class IValueTest {

	@SuppressWarnings("rawtypes")
	private final IValue val;

	@SuppressWarnings("rawtypes")
	public IValueTest(final IValue v) {
		val = v;
	}

	@Test
	public void testGetValue() {
		if (val instanceof EnumDecision) {
			assertEquals(new StringValue(" "), val.getValue());
		} else {
			val.getValue();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSetValue() throws RangeValueException {
		if (!(val instanceof ADecision)) {
			ThrowingRunnable r = () -> val.setValue(null);
			assertThrows(Throwable.class, r);
		} else if (val instanceof NumberDecision) {
			val.setValue(new DoubleValue(2.5));
		} else if (val instanceof BooleanDecision) {
			val.setValue(BooleanValue.getFalse());
		} else if (val instanceof StringDecision) {
			val.setValue(new StringValue("testest"));
		}
	}

	@SuppressWarnings("rawtypes")
	@Parameterized.Parameters
	public static Collection<IValue> instancesToTest() {
		return Arrays.<IValue>asList(BooleanValue.getTrue(), new DoubleValue(1.3), new StringValue("test"),
				new BooleanDecision("id"), new EnumDecision("id"), new NumberDecision("id"), new StringDecision("id")

		);
	}

}
