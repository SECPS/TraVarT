package at.jku.cps.travart.ppr.dsl.io;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.sqi.ppr.model.AssemblySequence;

public class PprDslReaderTest {
	Path dslPath;
	AssemblySequence aSequence;
	PprDslReader reader;

	@Before
	public void setUp() throws Exception {
		dslPath = Paths
				.get(new PprDslReaderTest().getClass().getClassLoader().getResource("rocker-switches.dsl").toURI());
		aSequence = new AssemblySequence();
		reader = new PprDslReader();
	}

	@Test
	public void testRead() throws IOException, NotSupportedVariablityTypeException {
		// TODO potentially create tests that specifically fail the reading of records
		// or fetching of the model name.
		reader.read(dslPath);
	}
}
