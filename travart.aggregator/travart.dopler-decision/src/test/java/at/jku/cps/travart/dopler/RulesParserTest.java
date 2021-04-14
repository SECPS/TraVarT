package at.jku.cps.travart.dopler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.impl.DMCSVHeader;
import at.jku.cps.travart.dopler.decision.model.ADecision.DecisionType;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import at.jku.cps.travart.dopler.decision.model.impl.StringValue;
import at.jku.cps.travart.dopler.decision.parser.RulesParser;
import at.jku.cps.travart.dopler.io.DecisionModelReader;

public class RulesParserTest {

	Path filePath;

	@Before
	public void setUp() throws Exception {
		filePath = Paths.get(RulesParserTest.class.getClassLoader().getResource("DOPLERToolsDM.csv").toURI());
	}

	private IDecisionModel getDecisionModel() {
		IDecisionModel dm = null;
		DecisionModelReader reader = new DecisionModelReader();
		try {
			dm = reader.read(filePath);
		} catch (IOException e) {
			fail("IO Exception occured while reading Decision Model");
			e.printStackTrace();
		} catch (NotSupportedVariablityTypeException e) {
			fail("Variability type was not supported while reading Decision Model");
			e.printStackTrace();
		}
		return dm;
	}

	@Test
	public void rulesParserNotNull() {
		RulesParser rp = new RulesParser(getDecisionModel());
		assertNotNull(rp);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void rulesNotEmpty() throws IOException, NotSupportedVariablityTypeException {
		IDecisionModel dm = getDecisionModel();
		RulesParser rp = new RulesParser(dm);
		Reader secondIn = new FileReader(filePath.toFile());
		Iterable<CSVRecord> secondParse = CSVFormat.EXCEL.withDelimiter(';').withHeader(DMCSVHeader.stringArray())
				.withFirstRecordAsHeader().parse(secondIn);
		for (CSVRecord c : secondParse) {
			String csvRules = c.get(DMCSVHeader.RULES);
			if (!csvRules.isBlank()) {
				String[] CSVruleSplit = TraVarTUtils.splitString(csvRules, "if");
				IDecision decision = null;
				String typeString = c.get(DMCSVHeader.TYPE);
				if (DecisionType.BOOLEAN.equalString(typeString)) {
					decision = new BooleanDecision("");
				} else if (DecisionType.ENUM.equalString(typeString)) {
					decision = new EnumDecision("");
				} else if (DecisionType.DOUBLE.equalString(typeString)) {
					decision = new NumberDecision("");
				} else if (DecisionType.STRING.equalString(typeString)) {
					decision = new StringDecision("");
				} else {
					throw new NotSupportedVariablityTypeException(typeString);
				}
				String range = c.get(DMCSVHeader.RANGE);
				String[] options = TraVarTUtils.splitString(range, "\\|");
				Range<String> valueOptions = createValueOptions(decision, options);
				decision.setRange(valueOptions);
				assert !rp.parse(decision, CSVruleSplit).isEmpty();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Range<String> createValueOptions(final IDecision decision, final String[] options) {
		Range<String> range = new Range();
		for (String o : options) {
			range.add(new StringValue(o));
		}
		return range;
	}
}
