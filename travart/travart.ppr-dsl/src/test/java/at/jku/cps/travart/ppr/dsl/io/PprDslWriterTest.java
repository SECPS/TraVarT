package at.jku.cps.travart.ppr.dsl.io;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;

import at.sqi.ppr.model.AssemblySequence;

public class PprDslWriterTest {
	private final String testResourcePath = "src/test/resources/";
	AssemblySequence aSequence;
	PprDslWriter writer;
	Path dslFileRead;
	Path dslFileWrite;

	@Before
	public void setUp() throws Exception {
		dslFileWrite = Paths.get(testResourcePath + "pprdsltext.dsl");
		writer = new PprDslWriter();
		dslFileRead = Paths
				.get(new PprDslWriterTest().getClass().getClassLoader().getResource("shiftfork.dsl").toURI());
		aSequence = new AssemblySequence();
		PprDslReader reader = new PprDslReader();
		aSequence = reader.read(dslFileRead);
	}
}
