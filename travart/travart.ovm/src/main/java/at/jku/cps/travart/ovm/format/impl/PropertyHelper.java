package at.jku.cps.travart.ovm.format.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.ovgu.featureide.fm.core.base.IPropertyContainer;
import de.ovgu.featureide.fm.core.io.xml.XmlFeatureModelFormat;

/**
 * This class represents an extension of the {@link XmlFeatureModelFormat}. It
 * is used to store the properties of a feature model precisely by using the
 * provided methods of the base class. These properties are for example the
 * location of the file menu.
 *
 * @author johannstoebich
 */
public class PropertyHelper extends XmlFeatureModelFormat {

	private static PropertyHelper instance;

	private PropertyHelper() {

	}

	/**
	 * This method returns the singelton instance of the PropertyHelper.
	 *
	 * @return the instance.
	 */
	public static PropertyHelper getSingeltonInstance() {
		if (instance == null) {
			instance = new PropertyHelper();
		}
		return instance;
	}

	/**
	 * This method writes the feature model properties into an element. This
	 * properties can be separated into GRAPHICS, CALCULATIONS and PROPERTY
	 * properties.
	 *
	 * @param doc        the document in which the properties should be written.
	 * @param properties the container which contains the properties.
	 * @param element    the XML-{@link Element} where the properties should be
	 *                   stored.
	 */
	public static void writeProperties(Document doc, IPropertyContainer properties, Element element) {
		getSingeltonInstance().addProperties(doc, properties, element);
	}

	/**
	 * This method read the feature model properties. This properties can be
	 * separated into GRAPHICS, CALCULATIONS and PROPERTY properties.
	 *
	 * @param properties the container in which the properties should be stored.
	 * @param element    the XML-{@link Element} containing the properties.
	 */
	public static void readProperties(IPropertyContainer properties, Element element) {
		getSingeltonInstance().parseFeatureModelProperties(properties, element);
	}

	/**
	 * This method checks if the XML-{@link Element} contains feature model
	 * properties. This properties can be separated into GRAPHICS, CALCULATIONS and
	 * PROPERTY properties.
	 *
	 * @param element the XML-{@link Element} containing the properties.
	 */
	public boolean isFeatureModelProperties(Element element) {
		final String nodeName = element.getNodeName();
		switch (nodeName) {
		case GRAPHICS:
		case CALCULATIONS:
		case PROPERTY:
			return true;
		default:
			return false;
		}
	}

	protected void parseFeatureModelProperties(IPropertyContainer properties, Element e) {
		for (final Element propertyElement : getElements(e.getChildNodes())) {
			final String nodeName = propertyElement.getNodeName();
			switch (nodeName) {
			case GRAPHICS:
				parseProperty(properties, propertyElement, GRAPHICS);
				break;
			case CALCULATIONS:
				parseProperty(properties, propertyElement, CALCULATIONS);
				break;
			case PROPERTY:
				parseProperty(properties, propertyElement, null);
				break;
			}
		}
	}
}
