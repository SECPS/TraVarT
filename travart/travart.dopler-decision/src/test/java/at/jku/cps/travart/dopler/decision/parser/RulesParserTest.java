package at.jku.cps.travart.dopler.decision.parser;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.exc.ParserException;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.io.DecisionModelReader;

public class RulesParserTest {
	RulesParser rp;
	IDecisionModel dm;

	@Before
	public void setUp() throws Exception {
		Path toRead = Paths
				.get(new RulesParserTest().getClass().getClassLoader().getResource("DOPLERToolsDM.csv").toURI());
		DecisionModelReader reader = new DecisionModelReader();
		try {
			dm = reader.read(toRead);
			rp = new RulesParser(dm);
		} catch (IOException | NotSupportedVariablityTypeException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testRulesParser() {
		ThrowingRunnable t = () -> {
			new RulesParser(null);
		};
		assertThrows(NullPointerException.class, t);
	}

	@Test(expected= NullPointerException.class)
	public void testParseNoNullDecision() {
			String[] csvRules = { "test", "test1", "test2" };
			rp.parse(null, csvRules);
	}
	
	@Test(expected= NullPointerException.class)
	public void testParseNoNullCSVRule() {
			rp.parse(new BooleanDecision("test"), null);
	}
	
	@Test(expected= ParserException.class)
	public void testParseParserException() {
		String csvRules = "if (ALL) { test = true; }";
		String[] CSVruleSplit = TraVarTUtils.splitString(csvRules, "if");
		dm.forEach(d -> rp.parse(d, CSVruleSplit));
	}

	@Test
	public void testParse() {
		String csvRules = "if (!ALL) { ALL = true; }";
		String[] CSVruleSplit = TraVarTUtils.splitString(csvRules, "if");
		dm.forEach(d -> rp.parse(d, CSVruleSplit).isEmpty());
	}

}
