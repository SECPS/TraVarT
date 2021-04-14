package at.jku.cps.travart.ppr.dsl.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import at.jku.cps.travart.core.common.IWriter;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.sqi.ppr.dsl.serializer.AssemblySequenceDslSerializer;
import at.sqi.ppr.dsl.serializer.AttributeDslSerializer;
import at.sqi.ppr.dsl.serializer.ConstraintDslSerializer;
import at.sqi.ppr.dsl.serializer.DslSerializerException;
import at.sqi.ppr.dsl.serializer.ProcessDslSerializer;
import at.sqi.ppr.dsl.serializer.ProductDslSerializer;
import at.sqi.ppr.dsl.serializer.ResourceDslSerializer;
import at.sqi.ppr.model.AssemblySequence;

public class PprDslWriter implements IWriter<AssemblySequence> {

	@Override
	public void write(final AssemblySequence asq, final Path path)
			throws IOException, NotSupportedVariablityTypeException {
		AttributeDslSerializer attributeWriter = new AttributeDslSerializer();
		ProductDslSerializer productWriter = new ProductDslSerializer();
		ResourceDslSerializer resourceWriter = new ResourceDslSerializer();
		ProcessDslSerializer processWriter = new ProcessDslSerializer();
		ConstraintDslSerializer constraintWriter = new ConstraintDslSerializer();
		AssemblySequenceDslSerializer asqWriter = new AssemblySequenceDslSerializer(attributeWriter, productWriter,
				resourceWriter, processWriter, constraintWriter);
		try {
			String asqString = asqWriter.serializeAssemblySequence(asq);
			FileWriter writer = new FileWriter(path.toFile());
			writer.append(asqString);
			writer.flush();
		} catch (DslSerializerException e) {
			throw new IOException(e);
		}
	}

}
