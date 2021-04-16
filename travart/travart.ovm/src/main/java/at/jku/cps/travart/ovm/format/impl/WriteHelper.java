package at.jku.cps.travart.ovm.format.impl;

import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.ABSTRACT;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.ALTERNATIVE;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.DESCRIPTION;
import static at.jku.cps.travart.ovm.format.impl.OvModelXmlPersistanceProperties.FACTORY_ID;
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
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import at.jku.cps.travart.ovm.common.OvModelUtils;
import at.jku.cps.travart.ovm.format.impl.exc.OvModelSerialisationException;
import at.jku.cps.travart.ovm.format.impl.exc.OvModelSerialisationNotSupported;
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
 * This class provides the methods for writing an {@link IOvModel} to an
 * XML-document.
 *
 * @author johannstoebich
 */
public class WriteHelper {

	/**
	 * This method is a helper method which provides functionalities to write an
	 * {@link IOvModel} into an XML-Document. First the variation points which are
	 * part of the root feature tree, then the constraints and finally the variation
	 * points which are not part of the root feature tree are written.
	 *
	 * @param ovModel the {@link IOvModel} which should be written.
	 * @param doc     the document to which the {@link IOvModel} should be written.
	 * @throws OvModelSerialisationException
	 */
	public static void writeModel(IOvModel ovModel, Document doc) throws OvModelSerialisationException {
		final List<IOvModelElement> alreadySerialisedElements = new ArrayList<>();

		final Element ovModelElement = doc.createElement(OV_MODEL);
		doc.appendChild(ovModelElement);

		writeProperties(ovModel, ovModelElement, alreadySerialisedElements);

		ovModelElement.setAttribute(FACTORY_ID, ovModel.getFactoryId());

		write(ovModel.getMetainformation(), ovModelElement, alreadySerialisedElements);

		// write variant points of constraints after the constraints itself which will
		// leads to nicer serialization
		// (because they can then refer to constraints with a Reference).
		final List<IOvModelVariationPoint> variantPointsNotPartOfRoot = new ArrayList<>();
		for (final IOvModelVariationPoint variationPoint : OvModelUtils.getVariationPoints(ovModel)) {
			if (OvModelUtils.isPartOfOvModelRoot(variationPoint)) {
				write(variationPoint, ovModelElement, alreadySerialisedElements);
			} else {
				variantPointsNotPartOfRoot.add(variationPoint);
			}
		}

		for (final IOvModelConstraint constraints : OvModelUtils.getConstraints(ovModel)) {
			if (constraints instanceof IOvModelExcludesConstraint) {
				write((IOvModelExcludesConstraint) constraints, ovModelElement, alreadySerialisedElements);
			} else {
				write((IOvModelRequiresConstraint) constraints, ovModelElement, alreadySerialisedElements);
			}
		}

		// write variant points of constraints after the constraints itself which will
		// leads to nicer serialization.
		for (final IOvModelVariationPoint variationPoint : variantPointsNotPartOfRoot) {
			write(variationPoint, ovModelElement, alreadySerialisedElements);
		}
	}

	/**
	 * This method writes an {@link IOvModelElement} to an XML-{@link Node}. It uses
	 * other write methods to write the content respectively because
	 * {@link IOvModelElement} is abstract.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which will be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	private static void write(IOvModelElement object, Node node, Collection<IOvModelElement> alreadySerialisedElements)
			throws OvModelSerialisationException {

		if (object instanceof IOvModelVariant) {
			write((IOvModelVariant) object, node, alreadySerialisedElements);
		} else if (object instanceof IOvModelVariationPoint) {
			write((IOvModelVariationPoint) object, node, alreadySerialisedElements);
		} else if (object instanceof IOvModelExcludesConstraint) {
			write((IOvModelExcludesConstraint) object, node, alreadySerialisedElements);
		} else if (object instanceof IOvModelRequiresConstraint) {
			write((IOvModelRequiresConstraint) object, node, alreadySerialisedElements);
		} else {
			throw new OvModelSerialisationNotSupported(object.getClass());
		}
	}

	/**
	 * This method writes an {@link IOvModelVariant} to an XML-{@link Node}.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which will be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	public static void write(IOvModelVariant object, Node node, Collection<IOvModelElement> alreadySerialisedElements)
			throws OvModelSerialisationException {
		if (alreadySerialisedElements.contains(object)) {
			final Element ovModelVariantReference = node.getOwnerDocument().createElement(OV_MODEL_VARIANT_REFERENCE);
			node.appendChild(ovModelVariantReference);

			writeProperties((IIdentifiable) object, ovModelVariantReference, alreadySerialisedElements);
		} else {
			final Element ovModelVariant = node.getOwnerDocument().createElement(OV_MODEL_VARIANT);
			node.appendChild(ovModelVariant);

			writeProperties(object, ovModelVariant, alreadySerialisedElements);

			alreadySerialisedElements.add(object);
		}

	}

	/**
	 * This method writes an {@link IOvModelVariationPoint} to an XML-{@link Node}.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	public static void write(IOvModelVariationPoint object, Node node,
			Collection<IOvModelElement> alreadySerialisedElements) throws OvModelSerialisationException {
		if (alreadySerialisedElements.contains(object)) {
			final Element ovModelVariantReference = node.getOwnerDocument()
					.createElement(OV_MODEL_VARIATION_POINT_REFERENCE);
			node.appendChild(ovModelVariantReference);

			writeProperties((IIdentifiable) object, ovModelVariantReference, alreadySerialisedElements);
		} else {

			final Element ovModelVariationPoint = node.getOwnerDocument().createElement(OV_MODEL_VARIATION_POINT);
			node.appendChild(ovModelVariationPoint);

			writeProperties(object, ovModelVariationPoint, alreadySerialisedElements);

			final Attr alternative = node.getOwnerDocument().createAttribute(ALTERNATIVE);
			alternative.setValue(String.valueOf(OvModelUtils.isAlternative(object)));
			ovModelVariationPoint.setAttributeNode(alternative);

			final Attr minChoices = node.getOwnerDocument().createAttribute(MIN_CHOICES);
			minChoices.setValue(String.valueOf(OvModelUtils.getMinChoices(object)));
			ovModelVariationPoint.setAttributeNode(minChoices);

			final Attr maxChoices = node.getOwnerDocument().createAttribute(MAX_CHOICES);
			maxChoices.setValue(String.valueOf(OvModelUtils.getMaxChoices(object)));
			ovModelVariationPoint.setAttributeNode(maxChoices);

			if (OvModelUtils.getMandatoryChildren(object).size() > 0) {
				final Element ovMandatoryChildren = node.getOwnerDocument().createElement(MANDATORY_CHILDREN);
				ovModelVariationPoint.appendChild(ovMandatoryChildren);
				for (final IOvModelVariationBase mandatoryChildren : OvModelUtils.getMandatoryChildren(object)) {
					write(mandatoryChildren, ovMandatoryChildren, alreadySerialisedElements);
				}
			}

			if (OvModelUtils.getOptionalChildren(object).size() > 0) {
				final Element ovOptionalChildren = node.getOwnerDocument().createElement(OPTIONAL_CHILDREN);
				ovModelVariationPoint.appendChild(ovOptionalChildren);
				for (final IOvModelVariationBase optionalChildren : OvModelUtils.getOptionalChildren(object)) {
					write(optionalChildren, ovOptionalChildren, alreadySerialisedElements);
				}
			}

			alreadySerialisedElements.add(object);
		}
	}

	/**
	 * This method writes an {@link IOvModelMetainformation} to an XML-{@link Node}.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	public static void write(IOvModelMetainformation object, Node node,
			Collection<IOvModelElement> alreadySerialisedElements) {
		final Element ovModelMetainformation = node.getOwnerDocument().createElement(OV_MODEL_METAINFORMATION);
		node.appendChild(ovModelMetainformation);

		final Attr description = node.getOwnerDocument().createAttribute(DESCRIPTION);
		description.setValue(object.getDescription());
		ovModelMetainformation.setAttributeNode(description);

		PropertyHelper.writeProperties(node.getOwnerDocument(), object.getCustomProperties(), ovModelMetainformation);
	}

	/**
	 * This method writes an {@link IOvModelVariationBaseMetainformation} to an
	 * XML-{@link Node}.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	public static void write(IOvModelVariationBaseMetainformation object, Node node,
			Collection<IOvModelElement> alreadySerialisedElements) throws OvModelSerialisationException {
		final Element ovModelMetainformation = node.getOwnerDocument()
				.createElement(OV_MODEL_VARIATION_BASE_METAINFORMATION);
		node.appendChild(ovModelMetainformation);

		final Attr attAbstract = node.getOwnerDocument().createAttribute(ABSTRACT);
		attAbstract.setValue(String.valueOf(object.isAbstract()));
		ovModelMetainformation.setAttributeNode(attAbstract);

		final Attr hidden = node.getOwnerDocument().createAttribute(HIDDEN);
		hidden.setValue(String.valueOf(object.isHidden()));
		ovModelMetainformation.setAttributeNode(hidden);

		final Attr partOfOvModelRoot = node.getOwnerDocument().createAttribute(PART_OF_OVMODEL_ROOT);
		partOfOvModelRoot.setValue(String.valueOf(object.isPartOfOvModelRoot()));
		ovModelMetainformation.setAttributeNode(partOfOvModelRoot);

		final Attr description = node.getOwnerDocument().createAttribute(DESCRIPTION);
		description.setValue(object.getDescription());
		ovModelMetainformation.setAttributeNode(description);

		if (object.getReferencedConstraints() != null && object.getReferencedConstraints().size() > 0) {
			final Element referencedConstraints = node.getOwnerDocument().createElement(REFERENCED_CONSTRAINTS);
			ovModelMetainformation.appendChild(referencedConstraints);
			for (final IOvModelConstraint referencedConstraint : object.getReferencedConstraints()) {
				write(referencedConstraint, referencedConstraints, alreadySerialisedElements);
			}
		}

		PropertyHelper.writeProperties(node.getOwnerDocument(), object.getCustomProperties(), ovModelMetainformation);
	}

	/**
	 * This method writes an {@link IOvModelConstraintMetainformation} to an
	 * XML-{@link Node}.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	public static void write(IOvModelConstraintMetainformation object, Node node,
			Collection<IOvModelElement> alreadySerialisedElements) {
		final Element ovModelMetainformation = node.getOwnerDocument()
				.createElement(OV_MODEL_CONSTRAINT_METAINFORMATION);
		node.appendChild(ovModelMetainformation);

		final Attr description = node.getOwnerDocument().createAttribute(DESCRIPTION);
		description.setValue(object.getDescription());
		ovModelMetainformation.setAttributeNode(description);

		PropertyHelper.writeProperties(node.getOwnerDocument(), object.getCustomProperties(), ovModelMetainformation);
	}

	/**
	 * This method writes an {@link IOvModelExcludesConstraint} to an
	 * XML-{@link Node}.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	public static void write(IOvModelExcludesConstraint object, Node node,
			Collection<IOvModelElement> alreadySerialisedElements) throws OvModelSerialisationException {
		if (alreadySerialisedElements.contains(object)) {
			final Element ovModelVariantReference = node.getOwnerDocument()
					.createElement(OV_MODEL_EXCLUDES_CONSTRAINT_REFERENCE);
			node.appendChild(ovModelVariantReference);

			writeProperties((IIdentifiable) object, ovModelVariantReference, alreadySerialisedElements);
		} else {
			final Element ovModelExcludesConstraint = node.getOwnerDocument()
					.createElement(OV_MODEL_EXCLUDES_CONSTRAINT);
			node.appendChild(ovModelExcludesConstraint);

			writeProperties(object, ovModelExcludesConstraint, alreadySerialisedElements);

			alreadySerialisedElements.add(object);
		}
	}

	/**
	 * This method writes an {@link IOvModelRequiresConstraint} to an
	 * XML-{@link Node}.
	 *
	 * @param object                    the content which should be written.
	 * @param node                      the XML-{@link Node} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 * @throws OvModelSerialisationException
	 */
	public static void write(IOvModelRequiresConstraint object, Node node,
			Collection<IOvModelElement> alreadySerialisedElements) throws OvModelSerialisationException {

		if (alreadySerialisedElements.contains(object)) {
			final Element ovModelVariantReference = node.getOwnerDocument()
					.createElement(OV_MODEL_REQUIRES_CONSTRAINT_REFERENCE);
			node.appendChild(ovModelVariantReference);

			writeProperties((IIdentifiable) object, ovModelVariantReference, alreadySerialisedElements);
		} else {
			final Element ovModelRequiresConstraint = node.getOwnerDocument()
					.createElement(OV_MODEL_REQUIRES_CONSTRAINT);
			node.appendChild(ovModelRequiresConstraint);

			writeProperties(object, ovModelRequiresConstraint, alreadySerialisedElements);

			alreadySerialisedElements.add(object);
		}
	}

	/**
	 * This method is a helper method which provides functionalities to write the
	 * properties of an {@link IIdentifiable}.
	 *
	 * @param object                    the {@link IIdentifiable} which should be
	 *                                  written.
	 * @param element                   the XML-{@link Element} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 */
	public static void writeProperties(IIdentifiable object, Element element,
			Collection<IOvModelElement> alreadySerialisedElements) {
		final Attr name = element.getOwnerDocument().createAttribute(NAME);
		name.setValue(OvModelUtils.getName(object));
		element.setAttributeNode(name);
	}

	/**
	 * This method is a helper method which provides functionalities to write the
	 * properties of an {@link IOvModel}.
	 *
	 * @param object                    the {@link IOvModel} which should be
	 *                                  written.
	 * @param element                   the XML-{@link Element} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 */
	public static void writeProperties(IOvModel object, Element element,
			Collection<IOvModelElement> alreadySerialisedElements) {
		writeProperties((IIdentifiable) object, element, alreadySerialisedElements);
	}

	/**
	 * This method is a helper method which provides functionalities to write the
	 * properties of an {@link IOvModelElement}.
	 *
	 * @param object                    the {@link IOvModelElement} which should be
	 *                                  written.
	 * @param element                   the XML-{@link Element} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 */
	public static void writeProperties(IOvModelElement object, Element element,
			Collection<IOvModelElement> alreadySerialisedElements) {
		writeProperties((IIdentifiable) object, element, alreadySerialisedElements);
	}

	/**
	 * This method is a helper method which provides functionalities to write the
	 * properties of an {@link IOvModelVariationBase}.
	 *
	 * @param object                    the {@link IOvModelVariationBase} which
	 *                                  should be written.
	 * @param element                   the XML-{@link Element} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 */
	public static void writeProperties(IOvModelVariationBase object, Element element,
			Collection<IOvModelElement> alreadySerialisedElements) throws OvModelSerialisationException {
		writeProperties((IOvModelElement) object, element, alreadySerialisedElements);

		final Attr optional = element.getOwnerDocument().createAttribute(OPTIONAL);
		optional.setValue(String.valueOf(OvModelUtils.isOptional(object)));
		element.setAttributeNode(optional);

		write(object.getMetainformation(), element, alreadySerialisedElements);
	}

	/**
	 * This method is a helper method which provides functionalities to write the
	 * properties of an {@link IOvModelConstraint}.
	 *
	 * @param object                    the {@link IOvModelConstraint} which should
	 *                                  be written.
	 * @param element                   the XML-{@link Element} which should be
	 *                                  populated.
	 * @param alreadySerialisedElements the OvElements which already have been
	 *                                  serialized (is used for creating references
	 *                                  to already serialized elements).
	 */
	public static void writeProperties(IOvModelConstraint object, Element element,
			Collection<IOvModelElement> alreadySerialisedElements) throws OvModelSerialisationException {
		writeProperties((IOvModelElement) object, element, alreadySerialisedElements);

		write(object.getMetainformation(), element, alreadySerialisedElements);

		write(OvModelUtils.getSource(object), element, alreadySerialisedElements);
		write(OvModelUtils.getTarget(object), element, alreadySerialisedElements);
	}
}
