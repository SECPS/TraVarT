package at.jku.cps.travart.ovm.format.impl;

import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.ABSTRACT;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.ALTERNATIVE;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.DESCRIPTION;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.HIDDEN;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.MANDATORY_CHILDREN;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.MAX_CHOICES;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.MIN_CHOICES;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.NAME;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OPTIONAL;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OPTIONAL_CHILDREN;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_CONSTRAINT_METAINFORMATION;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_EXCLUDES_CONSTRAINT;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_EXCLUDES_CONSTRAINT_REFERENCE;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_METAINFORMATION;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_REQUIRES_CONSTRAINT;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_REQUIRES_CONSTRAINT_REFERENCE;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_VARIANT;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_VARIANT_REFERENCE;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_VARIATION_BASE_METAINFORMATION;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_VARIATION_POINT;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.OV_MODEL_VARIATION_POINT_REFERENCE;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.PART_OF_OVMODEL_ROOT;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.REFERENCED_CONSTRAINTS;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import at.jku.cps.travart.ovm.common.OvModelUtils;
import at.jku.cps.travart.ovm.factory.IOvModelFactory;
import at.jku.cps.travart.ovm.format.impl.exc.OvModelSerialisationException;
import at.jku.cps.travart.ovm.format.impl.exc.OvModelWrongCountOfElements;
import at.jku.cps.travart.ovm.format.impl.exc.OvModelWrongElementException;
import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModel;
import at.jku.cps.travart.ovm.model.IOvModelElement;
import at.jku.cps.travart.ovm.model.IOvModelMetainformation;
import at.jku.cps.travart.ovm.model.IOvModelVariant;
import at.jku.cps.travart.ovm.model.IOvModelVariationBase;
import at.jku.cps.travart.ovm.model.IOvModelVariationBaseMetainformation;
import at.jku.cps.travart.ovm.model.IOvModelVariationPoint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelConstraintMetainformation;
import at.jku.cps.travart.ovm.model.constraint.IOvModelExcludesConstraint;
import at.jku.cps.travart.ovm.model.constraint.IOvModelRequiresConstraint;

/**
 * This class provides the methods for reading an {@link IOvModel} from a XML
 * document.
 *
 * @author johannstoebich
 */
public class ReadHelper {

	/**
	 * This method reads an {@link IOvModel} from an document. First it reads the
	 * properties, then the metainformation and finally its variation points and
	 * constraints.
	 *
	 * @param document the XML-document from which the {@link IOvModel} should be
	 *                 read.
	 * @param ovModel  the model to which the read elements should be added.
	 * @param factory  the factory which creates the {@link IOvModel}.
	 * @return the finally read {@link IOvModel}
	 * @throws OvModelSerialisationException
	 */
	public static IOvModel readModel(Document document, IOvModel ovModel, IOvModelFactory factory)
			throws OvModelSerialisationException {
		final NodeList nodes = document.getElementsByTagName(OV_MODEL);
		if (nodes == null || nodes.getLength() != 1) {
			throw new OvModelWrongElementException(OV_MODEL);
		}
		final Node node = nodes.item(0);
		if (!(node instanceof Element)) {
			throw new OvModelWrongElementException(node.getNodeName());
		}

		readProperties((Element) node, ovModel, ovModel, factory);

		final Element metainformation = getChildElementByTagName((Element) node, OV_MODEL_METAINFORMATION, true);
		read(metainformation, ovModel.getMetainformation(), ovModel, factory);

		for (final Element element : getChildElements((Element) node, true)) {
			if (PropertyHelper.getSingeltonInstance().isFeatureModelProperties(element)) {
				continue;
			}
			if (element.getNodeName().contentEquals(OV_MODEL_METAINFORMATION)) {
				continue;
			}

			final IOvModelElement ovModelElement = read(element, ovModel, factory);
			if (ovModelElement instanceof IOvModelConstraint) {
				OvModelUtils.addConstraint(ovModel, (IOvModelConstraint) ovModelElement);
			} else {
				OvModelUtils.addVariationPoint(ovModel, (IOvModelVariationPoint) ovModelElement);
			}
		}

		return ovModel;
	}

	/**
	 * This method reads the XML-{@link Element}s of an {@link IOvModel}. Elements
	 * are either variants, variation points, excludes constraints or requires
	 * constraints. This mehtod can also handle references to one of these elements.
	 *
	 * @param element the XML-{@link Element} of the document which should be
	 *                parsed.
	 * @param ovModel the model which contains the already serialized objects.
	 * @param factory the factory used to create new elements.
	 * @return the created element.
	 * @throws OvModelSerialisationException
	 */
	private static IOvModelElement read(Element element, IOvModel ovModel, IOvModelFactory factory)
			throws OvModelSerialisationException {
		final String type = element.getNodeName();
		final String name = element.getAttribute(NAME);

		if (type.equals(OV_MODEL_VARIANT)) {
			final IOvModelVariant ovModelVariant = factory.createVariant(ovModel, name);
			read(element, ovModelVariant, ovModel, factory);
			return ovModelVariant;
		} else if (type.equals(OV_MODEL_VARIATION_POINT)) {
			final IOvModelVariationPoint ovModelVariationPoint = factory.createVariationPoint(ovModel, name);
			read(element, ovModelVariationPoint, ovModel, factory);
			return ovModelVariationPoint;
		} else if (type.equals(OV_MODEL_EXCLUDES_CONSTRAINT)) {
			final IOvModelExcludesConstraint ovModelExcludesConstraint = factory.createExcludesConstraint(ovModel);
			read(element, ovModelExcludesConstraint, ovModel, factory);
			return ovModelExcludesConstraint;
		} else if (type.equals(OV_MODEL_REQUIRES_CONSTRAINT)) {
			final IOvModelRequiresConstraint ovModelRequiresConstraint = factory.createRequiresConstraint(ovModel);
			read(element, ovModelRequiresConstraint, ovModel, factory);
			return ovModelRequiresConstraint;
		} else if (type.equals(OV_MODEL_VARIANT_REFERENCE) || type.equals(OV_MODEL_VARIATION_POINT_REFERENCE)
				|| type.equals(OV_MODEL_EXCLUDES_CONSTRAINT_REFERENCE)
				|| type.equals(OV_MODEL_REQUIRES_CONSTRAINT_REFERENCE)) {
			final IOvModelElement ovElement = ovModel.getElement(factory.createIdentifiable(0, name));
			if (ovElement == null) {
				throw new OvModelWrongElementException(type, name);
			}
			return ovElement;
		} else {
			throw new OvModelWrongElementException(type);
		}
	}

	/**
	 * This method populates an {@link IOvModelVariant}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the variant.
	 * @param object  the {@link IOvModelVariant} which should be populated.
	 * @param ovModel the {@link IOvModel} the newly created variant belongs to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 * @throws OvModelSerialisationException
	 */
	public static void read(Element element, IOvModelVariant object, IOvModel ovModel, IOvModelFactory factory)
			throws OvModelSerialisationException {
		readProperties(element, object, ovModel, factory);
	}

	/**
	 * This method populates an {@link IOvModelVariationPoint}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the variation point.
	 * @param object  the {@link IOvModelVariationPoint} which should be populated.
	 * @param ovModel the {@link IOvModel} the newly created variation point belongs
	 *                to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 * @throws OvModelSerialisationException
	 */
	public static void read(Element element, IOvModelVariationPoint object, IOvModel ovModel, IOvModelFactory factory)
			throws OvModelSerialisationException {
		readProperties(element, object, ovModel, factory);

		final String alternative = element.getAttribute(ALTERNATIVE);
		if (alternative != null && alternative.contentEquals(String.valueOf(true))) {
			OvModelUtils.setAlternative(object, true);
		} else {
			OvModelUtils.setAlternative(object, false);
		}

		final String minChoices = element.getAttribute(MIN_CHOICES);
		if (minChoices != null) {
			OvModelUtils.setMinChoices(object, Integer.valueOf(minChoices));
		}

		final String maxChoices = element.getAttribute(MAX_CHOICES);
		if (maxChoices != null) {
			OvModelUtils.setMaxChoices(object, Integer.valueOf(maxChoices));
		}

		final Element ovMandatoryChildren = getChildElementByTagName(element, MANDATORY_CHILDREN, false);
		if (ovMandatoryChildren != null) {
			for (final Element childElement : getChildElements(ovMandatoryChildren, true)) {
				final IOvModelVariationBase ovModelVariationBase = (IOvModelVariationBase) read(childElement, ovModel,
						factory);
				object.addMandatoryChild(ovModelVariationBase);
			}
		}

		final Element ovOptionalChildren = getChildElementByTagName(element, OPTIONAL_CHILDREN, false);
		if (ovOptionalChildren != null) {
			for (final Element childElement : getChildElements(ovOptionalChildren, true)) {
				final IOvModelVariationBase ovModelVariationBase = (IOvModelVariationBase) read(childElement, ovModel,
						factory);
				object.addOptionalChild(ovModelVariationBase);
			}
		}
	}

	/**
	 * This method populates an {@link IOvModelMetainformation}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the metainformation.
	 * @param object  the {@link IOvModelMetainformation} which should be populated.
	 * @param ovModel the {@link IOvModel} the newly created metainformation belongs
	 *                to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 * @throws OvModelSerialisationException
	 */
	public static void read(Element element, IOvModelMetainformation object, IOvModel ovModel,
			IOvModelFactory factory) {
		final String description = element.getAttribute(DESCRIPTION);
		object.setDescription(description);

		PropertyHelper.readProperties(object.getCustomProperties(), element);
	}

	/**
	 * This method populates an {@link IOvModelVariationBaseMetainformation}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the metainformation.
	 * @param object  the {@link IOvModelVariationBaseMetainformation} which should
	 *                be populated.
	 * @param ovModel the {@link IOvModel} the newly created metainformation belongs
	 *                to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 * @throws OvModelSerialisationException
	 */
	public static void read(Element element, IOvModelVariationBaseMetainformation object, IOvModel ovModel,
			IOvModelFactory factory) throws OvModelSerialisationException {
		final String abstractStr = element.getAttribute(ABSTRACT);
		if (abstractStr != null && abstractStr.contentEquals(String.valueOf(true))) {
			object.setAbstract(true);
		} else {
			object.setAbstract(false);
		}

		final String hidden = element.getAttribute(HIDDEN);
		if (hidden != null && hidden.contentEquals(String.valueOf(true))) {
			object.setHidden(true);
		} else {
			object.setHidden(false);
		}

		final String partOfOvModelRoot = element.getAttribute(PART_OF_OVMODEL_ROOT);
		if (partOfOvModelRoot != null && partOfOvModelRoot.contentEquals(String.valueOf(true))) {
			object.setPartOfOvModelRoot(true);
		} else {
			object.setPartOfOvModelRoot(false);
		}

		final String description = element.getAttribute(DESCRIPTION);
		object.setDescription(description);

		final Element referencedConstraints = getChildElementByTagName(element, REFERENCED_CONSTRAINTS, false);
		if (referencedConstraints != null) {
			for (final Element childElement : getChildElements(referencedConstraints, true)) {
				final IOvModelElement constraint = read(childElement, ovModel, factory);
				if (!(constraint instanceof IOvModelConstraint)) {
					throw new OvModelWrongElementException(childElement.getTagName(), childElement.getNodeName());
				}
				object.getReferencedConstraints().add((IOvModelConstraint) constraint);
			}
		}

		PropertyHelper.readProperties(object.getCustomProperties(), element);
	}

	/**
	 * This method populates an {@link IOvModelConstraintMetainformation}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the metainformation.
	 * @param object  the {@link IOvModelConstraintMetainformation} which should be
	 *                populated.
	 * @param ovModel the {@link IOvModel} the newly created metainformation belongs
	 *                to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 * @throws OvModelSerialisationException
	 */
	public static void read(Element element, IOvModelConstraintMetainformation object, IOvModel ovModel,
			IOvModelFactory factory) {
		final String description = element.getAttribute(DESCRIPTION);
		object.setDescription(description);

		PropertyHelper.readProperties(object.getCustomProperties(), element);
	}

	/**
	 * This method populates an {@link IOvModelExcludesConstraint}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the constraint.
	 * @param object  the {@link IOvModelExcludesConstraint} which should be
	 *                populated.
	 * @param ovModel the {@link IOvModel} the newly created constraint belongs to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 * @throws OvModelSerialisationException
	 */
	public static void read(Element element, IOvModelExcludesConstraint object, IOvModel ovModel,
			IOvModelFactory factory) throws OvModelSerialisationException {
		readProperties(element, object, ovModel, factory);
	}

	/**
	 * This method populates an {@link IOvModelRequiresConstraint}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the constraint.
	 * @param object  the {@link IOvModelRequiresConstraint} which should be
	 *                populated.
	 * @param ovModel the {@link IOvModel} the newly created constraint belongs to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 * @throws OvModelSerialisationException
	 */
	public static void read(Element element, IOvModelRequiresConstraint object, IOvModel ovModel,
			IOvModelFactory factory) throws OvModelSerialisationException {
		readProperties(element, object, ovModel, factory);
	}

	/**
	 * This method is a helper method which provides functionalities to read the
	 * content of an {@link IIdentifiable}. An {@link IIdentifiable} is an interface
	 * which is extended multiple times and therefore this shared method is created
	 * to read its properties.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the {@link IIdentifiable}.
	 * @param object  the {@link IIdentifiable} which should be populated.
	 * @param ovModel the {@link IOvModel} the newly created identifiable belongs
	 *                to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 */
	public static void readProperties(Element element, IIdentifiable object, IOvModel ovModel,
			IOvModelFactory factory) {
		OvModelUtils.setName(object, element.getAttribute(NAME));
	}

	/**
	 * This method is a helper method which provides functionalities to read the
	 * content of an {@link IOvModel}.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the {@link IOvModel}.
	 * @param object  the {@link IOvModel} which should be populated.
	 * @param ovModel the newly created {@link IOvModel}.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 */
	public static void readProperties(Element element, IOvModel object, IOvModel ovModel, IOvModelFactory factory) {
		readProperties(element, (IIdentifiable) object, ovModel, factory);
	}

	/**
	 * This method is a helper method which provides functionalities to read the
	 * content of an {@link IOvModelElement}. An {@link IOvModelElement} is an
	 * interface which is extended multiple times and therefore this shared method
	 * is created to read its properties.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the {@link IOvModelElement}.
	 * @param object  the {@link IOvModelElement} which should be populated.
	 * @param ovModel the {@link IOvModel} the newly created OvElement belongs to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 */
	public static void readProperties(Element element, IOvModelElement object, IOvModel ovModel,
			IOvModelFactory factory) {
		readProperties(element, (IIdentifiable) object, ovModel, factory);
	}

	/**
	 * This method is a helper method which provides functionalities to read the
	 * content of an {@link IOvModelVariationBase}. An {@link IOvModelVariationBase}
	 * is an interface which is extended by variation points and variants and
	 * therefore this shared method is created to read its properties.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the {@link IOvModelVariationBase}.
	 * @param object  the {@link IOvModelVariationBase} which should be populated.
	 * @param ovModel the {@link IOvModel} the newly created variation base belongs
	 *                to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 */
	public static void readProperties(Element element, IOvModelVariationBase object, IOvModel ovModel,
			IOvModelFactory factory) throws OvModelSerialisationException {
		readProperties(element, (IOvModelElement) object, ovModel, factory);

		final String optional = element.getAttribute(OPTIONAL);
		if (optional != null && optional.contentEquals(String.valueOf(true))) {
			OvModelUtils.setOptional(object, true);
		} else {
			OvModelUtils.setOptional(object, false);
		}

		final Element metainformation = getChildElementByTagName(element, OV_MODEL_VARIATION_BASE_METAINFORMATION,
				true);
		read(metainformation, object.getMetainformation(), ovModel, factory);
	}

	/**
	 * This method is a helper method which provides functionalities to read the
	 * content of an {@link IOvModelConstraint}. An {@link IOvModelConstraint} is an
	 * interface which is extended by excludes and requires constraints and
	 * therefore this shared method is created to read its properties.
	 *
	 * @param element the XML-{@link Element} which contains the serialization of
	 *                the {@link IOvModelConstraint}.
	 * @param object  the {@link IOvModelConstraint} which should be populated.
	 * @param ovModel the {@link IOvModel} the newly created constraint belongs to.
	 * @param factory the factory that should be used for creating the contents of
	 *                the {@link IOvModel}.
	 */
	public static void readProperties(Element node, IOvModelConstraint object, IOvModel ovModel,
			IOvModelFactory factory) throws OvModelSerialisationException {
		readProperties(node, (IOvModelElement) object, ovModel, factory);

		final Element metainformation = getChildElementByTagName(node, OV_MODEL_CONSTRAINT_METAINFORMATION, true);
		read(metainformation, object.getMetainformation(), ovModel, factory);

		final List<Element> elements = getChildElements(node, true);
		elements.removeIf(element -> element.getTagName().equals(OV_MODEL_CONSTRAINT_METAINFORMATION));
		if (elements.size() != 2) {
			throw new OvModelWrongCountOfElements(node.getNodeName(), 2, elements.size());
		}

		final IOvModelElement source = read(elements.get(0), ovModel, factory);
		final IOvModelElement target = read(elements.get(1), ovModel, factory);

		if (!(source instanceof IOvModelVariationBase)) {
			throw new OvModelWrongElementException("Source cannot be of type " + source.getClass());
		}
		if (!(target instanceof IOvModelVariationBase)) {
			throw new OvModelWrongElementException("Target cannot be of type " + target.getClass());
		}

		object.setSource((IOvModelVariationBase) source);
		object.setTarget((IOvModelVariationBase) target);
	}

	/**
	 * This method searches threw all child elements and returns the first
	 * child-element if found. If more than one have been found an excpetion is
	 * thrown.
	 *
	 * @param element                    the XML-{@link Element} which should be
	 *                                   searched threw.
	 * @param throwExceptionWhenNotFound determines if when no elements have been
	 *                                   found an exception should be thrown or null
	 *                                   should be returned.
	 * @param name                       the name which the XML-{@link Element} must
	 *                                   match.
	 * @return the found element, otherwise null or an exception.
	 * @throws OvModelSerialisationException
	 */
	private static Element getChildElementByTagName(Element element, String name, boolean throwExceptionWhenNotFound)
			throws OvModelSerialisationException {
		final List<Element> childElements = getChildElementsByTagName(element, name);
		if (childElements.size() == 0 && !throwExceptionWhenNotFound) {
			return null;
		} else if (childElements.size() != 1) {
			throw new OvModelWrongCountOfElements(name, 1, childElements.size());
		}

		return childElements.get(0);
	}

	/**
	 * This method searches threw all child elements to find all elements having a
	 * specific name.
	 *
	 * @param parent the XML-{@link Element} which should be searched threw.
	 * @param name   the name which the XML-{@link Element} must match.
	 * @return the found elements, otherwise an empty list.
	 * @throws OvModelSerialisationException
	 */
	private static List<Element> getChildElementsByTagName(Element parent, String name)
			throws OvModelSerialisationException {
		final List<Element> childElements = new ArrayList<>();
		for (final Element child : getChildElements(parent, false)) {
			if (name.equals(child.getNodeName())) {
				childElements.add(child);
			}
		}
		return childElements;
	}

	/**
	 * This method returns all child elements from an element.
	 *
	 * @param parent                                          the
	 *                                                        XML-{@link Element}
	 *                                                        where all child
	 *                                                        elements should be
	 *                                                        returned.
	 * @param thowExceptionOnEverythingElseThanElementAndText determines whether an
	 *                                                        exception should be
	 *                                                        throw whenever the
	 *                                                        XML-{@link Element}
	 *                                                        contains something
	 *                                                        else than
	 *                                                        child-elements and
	 *                                                        texts.
	 * @return the list of elements.
	 * @throws OvModelSerialisationException
	 */
	private static List<Element> getChildElements(Element parent,
			boolean thowExceptionOnEverythingElseThanElementAndText) throws OvModelSerialisationException {
		final List<Element> childElements = new ArrayList<>();
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child instanceof Element) {
				childElements.add((Element) child);
				continue;
			}
			if (child instanceof Text) {
				continue;
			}
			if (thowExceptionOnEverythingElseThanElementAndText) {
				throw new OvModelWrongElementException(child.getNodeName());
			}
		}
		return childElements;
	}
}
