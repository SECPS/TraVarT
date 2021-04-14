package at.jku.cps.travart.ovm.io;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.ovm.factory.impl.OvModelFactory;
import at.jku.cps.travart.ovm.format.impl.ReadHelper;
import at.jku.cps.travart.ovm.format.impl.exc.OvModelSerialisationException;
import at.jku.cps.travart.ovm.model.IOvModel;

public class OvModelReader implements IReader<IOvModel> {

	private final OvModelFactory factory;

	public OvModelReader() {
		factory = OvModelFactory.getInstance();
	}

	@Override
	public IOvModel read(final Path path) throws IOException, NotSupportedVariablityTypeException {
		try {
			IOvModel ovModel = factory.create();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path.toFile());
			ovModel = ReadHelper.readModel(document, ovModel, factory);
			return ovModel;
		} catch (OvModelSerialisationException | SAXException | ParserConfigurationException e) {
			throw new IOException(e);
		}
	}
}
