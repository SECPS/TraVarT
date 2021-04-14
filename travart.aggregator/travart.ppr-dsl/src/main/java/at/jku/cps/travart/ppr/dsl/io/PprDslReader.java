package at.jku.cps.travart.ppr.dsl.io;

import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.sqi.ppr.dsl.reader.DslReader;
import at.sqi.ppr.dsl.reader.exceptions.DslParsingException;
import at.sqi.ppr.model.AssemblySequence;

public class PprDslReader implements IReader<AssemblySequence> {

	@Override
	public AssemblySequence read(final Path path) throws IOException, NotSupportedVariablityTypeException {
		DslReader dslReader = new DslReader();
		try {
			return dslReader.readDsl(path.toString());
		} catch (DslParsingException e) {
			throw new NotSupportedVariablityTypeException(e);
		}
	}
}
