package at.jku.cps.travart.dopler.decision.parser;

import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.impl.DMCSVHeader;
import at.jku.cps.travart.dopler.io.DecisionModelReader;

public class VisibilityParserTest {
	ConditionParser vp;
	IDecisionModel dm;

	@Before
	public void setUp() throws Exception {
		Path toRead = Paths
				.get(new VisibilityParserTest().getClass().getClassLoader().getResource("DOPLERToolsDM.csv").toURI());
		DecisionModelReader reader = new DecisionModelReader();
		try {
			dm = reader.read(toRead);
			vp = new ConditionParser(dm);
		} catch (IOException | NotSupportedVariablityTypeException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testVisibilityParserNoNull() {
		ThrowingRunnable r = () -> {
			vp = new ConditionParser(null);
		};
		assertThrows(NullPointerException.class, r);
	}

	@Test
	public void testParseNoNull() {
		ThrowingRunnable r = () -> {
			vp.parse(null);
		};
		assertThrows(NullPointerException.class, r);
	}

	@Test
	public void testParse() throws FileNotFoundException, IOException, URISyntaxException {
		Iterable<CSVRecord> parse = CSVFormat.EXCEL.withDelimiter(';').withHeader(DMCSVHeader.stringArray())
				.withFirstRecordAsHeader().parse(new FileReader(new File(new VisibilityParserTest().getClass()
						.getClassLoader().getResource("DOPLERToolsDM.csv").toURI())));
		for (CSVRecord record : parse) {
			String visiblity = record.get(DMCSVHeader.VISIBLITY);
			vp.parse(visiblity);
		}
	}

}
