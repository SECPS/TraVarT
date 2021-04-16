package at.jku.cps.travart.dopler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.junit.Test;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.io.DecisionModelReader;

public class DMReaderTest {

	@Test
	public void testInternalAsserts() {
		IDecisionModel dm = null;
		DecisionModelReader reader = new DecisionModelReader();
		try {
			dm = reader.read(Paths.get(DMReaderTest.class.getClassLoader().getResource("DOPLERToolsDM.csv").toURI()));
		} catch (IOException e) {
			fail("IO Exception occured while reading Decision Model");
			e.printStackTrace();
		} catch (NotSupportedVariablityTypeException e) {
			fail("Variability type was not supported while reading Decision Model");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			fail("Path to reading Decision Model was invalid");
			e.printStackTrace();
		}
		assertNotNull(dm);
	}

}
