package at.jku.cps.travart.ovm.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import at.jku.cps.travart.core.common.IWriter;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.ovm.format.impl.WriteHelper;
import at.jku.cps.travart.ovm.format.impl.exc.OvModelSerialisationException;
import at.jku.cps.travart.ovm.model.IOvModel;

public class OvModelWriter implements IWriter<IOvModel> {

	private static final String INDENT_CONSTANT = "{http://xml.apache.org/xslt}indent-amount";
	private static final String DEFAULT_INDET_NUMBER = "4";

	@Override
	public void write(final IOvModel ovModel, final Path path) throws IOException, NotSupportedVariablityTypeException {
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			WriteHelper.writeModel(ovModel, document);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(INDENT_CONSTANT, DEFAULT_INDET_NUMBER);
			DOMSource source = new DOMSource(document);
			FileWriter writer = new FileWriter(path.toFile());
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
		} catch (ParserConfigurationException | OvModelSerialisationException | TransformerFactoryConfigurationError
				| TransformerException e) {
			throw new IOException(e);
		}
	}
}
