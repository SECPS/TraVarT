package at.jku.cps.travart.ppr.dsl.transformation;

import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_DEFAULT_VALUE_KEY_PRAEFIX;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_DEFAULT_VALUE_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_DESCRIPTION_KEY_PRAEFIX;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_DESCRIPTION_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_ID_KEY_PRAEFIX;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_TYPE_KEY_PRAEFIX;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_TYPE_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_UNIT_KEY_PRAEFIX;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_UNIT_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_VALUE_KEY_PRAEFIX;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.ATTRIBUTE_VALUE_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.CHILDREN_PRODUCTS_LIST_NAME_NR_;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.CHILDREN_PRODUCTS_LIST_NAME_NR_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.CHILDREN_PRODUCTS_LIST_SIZE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.CHILDREN_PRODUCTS_LIST_SIZE_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.IMPLEMENTED_PRODUCTS_LIST_NAME_NR_;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.IMPLEMENTED_PRODUCTS_LIST_NAME_NR_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.IMPLEMENTED_PRODUCTS_LIST_SIZE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.IMPLEMENTED_PRODUCTS_LIST_SIZE_TYPE;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.NAME_ATTRIBUTE_KEY;
import static at.jku.cps.travart.ppr.dsl.transformation.DefaultPprDslTransformationProperties.NAME_ATTRIBUTE_TYPE;

import java.util.List;
import java.util.stream.Collectors;

import org.prop4j.Literal;
import org.prop4j.Node;

import at.jku.cps.travart.core.common.IModelTransformer;
import at.jku.cps.travart.core.common.Prop4JUtils;
import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import at.sqi.ppr.dsl.reader.constants.DslConstants;
import at.sqi.ppr.model.AssemblySequence;
import at.sqi.ppr.model.NamedObject;
import at.sqi.ppr.model.product.Product;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IConstraint;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;

public class FeatureModelToPprDslTransformer implements IModelTransformer<IFeatureModel, AssemblySequence> {

	private AssemblySequence asq;

	@Override
	public AssemblySequence transform(final IFeatureModel fm) throws NotSupportedVariablityTypeException {
		asq = new AssemblySequence();
		convertFeature(FeatureUtils.getRoot(fm));
		restoreAttributes(FeatureUtils.getRoot(fm));
		convertConstraints(FeatureUtils.getConstraints(fm));
		return asq;
	}

	private void convertFeature(final IFeature feature) throws NotSupportedVariablityTypeException {
		if (!FeatureUtils.getName(feature).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME)) {
			Product product = new Product();
			product.setId(FeatureUtils.getName(feature));
			product.setName(restoreNameFromProperties(feature, product));
			product.setAbstract(FeatureUtils.isAbstract(feature));
			restoreAttributesFromProperties(feature, product);
			asq.getProducts().put(product.getId(), product);
		}
		for (IFeature child : FeatureUtils.getChildren(feature)) {
			if (FeatureUtils.getName(feature).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME)
					|| !isEnumSubFeature(child)) {
				convertFeature(child);
			}
		}
	}

	private void restoreAttributes(final IFeature feature) {
		if (!FeatureUtils.getName(feature).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME)) {
			Product product = getProductFromId(FeatureUtils.getName(feature));
			assert product != null;
			restoreChildrenListOfProducts(feature, product);
			restoreImplementsListOfProducts(feature, product);
		}
		for (IFeature child : FeatureUtils.getChildren(feature)) {
			if (FeatureUtils.getName(feature).equals(DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME)
					|| !isEnumSubFeature(child)) {
				restoreAttributes(child);
			}
		}
	}

	private String restoreNameFromProperties(final IFeature feature, final Product product) {
		String name = feature.getCustomProperties().get(NAME_ATTRIBUTE_KEY, NAME_ATTRIBUTE_TYPE);
		return name != null ? name : product.getId();
	}

	private void restoreAttributesFromProperties(final IFeature feature, final Product product) {
		List<String> attributeNames = feature.getCustomProperties().getProperties().stream()
				.filter(entry -> entry.getKey().startsWith(ATTRIBUTE_ID_KEY_PRAEFIX))
				.map(entry -> entry.getKey().substring(ATTRIBUTE_ID_KEY_PRAEFIX.length())).collect(Collectors.toList());

		for (String attributeName : attributeNames) {
			NamedObject attribute = new NamedObject();
			attribute.setName(attributeName);

			attribute.setEntityType(DslConstants.ATTRIBUTE_ENTITY);

			attribute.setDescription(feature.getCustomProperties()
					.get(ATTRIBUTE_DESCRIPTION_KEY_PRAEFIX + attributeName, ATTRIBUTE_DESCRIPTION_TYPE));
			attribute.setUnit(
					feature.getCustomProperties().get(ATTRIBUTE_UNIT_KEY_PRAEFIX + attributeName, ATTRIBUTE_UNIT_TYPE));

			String type = feature.getCustomProperties().get(ATTRIBUTE_TYPE_KEY_PRAEFIX + attributeName,
					ATTRIBUTE_TYPE_TYPE);
			attribute.setType(type);

			switch (type.toLowerCase()) {
			case "number":
				Double defaultValue = Double.parseDouble(feature.getCustomProperties()
						.get(ATTRIBUTE_DEFAULT_VALUE_KEY_PRAEFIX + attributeName, ATTRIBUTE_DEFAULT_VALUE_TYPE));
				attribute.setDefaultValue(defaultValue);
				String valueStr = feature.getCustomProperties().get(ATTRIBUTE_VALUE_KEY_PRAEFIX + attribute.getName(),
						ATTRIBUTE_VALUE_TYPE);
				if (valueStr != null) {
					Double value = Double.parseDouble(valueStr);
					attribute.setValue(value);
				}
				break;
			case "string":
				attribute.setDefaultValue(feature.getCustomProperties()
						.get(ATTRIBUTE_DEFAULT_VALUE_KEY_PRAEFIX + attributeName, ATTRIBUTE_DEFAULT_VALUE_TYPE));
				attribute.setValue(feature.getCustomProperties().get(ATTRIBUTE_VALUE_KEY_PRAEFIX + attribute.getName(),
						ATTRIBUTE_VALUE_TYPE));
				break;
			}

			product.getAttributes().put(attributeName, attribute);
			asq.getDefinedAttributes().put(attributeName, attribute);
			asq.getProductAttributes().put(attributeName, attribute);
		}
	}

	private void restoreImplementsListOfProducts(final IFeature feature, final Product product) {
		String sizeStr = feature.getCustomProperties().get(IMPLEMENTED_PRODUCTS_LIST_SIZE,
				IMPLEMENTED_PRODUCTS_LIST_SIZE_TYPE);
		if (sizeStr != null) {
			int size = Integer.parseInt(sizeStr);
			for (int i = 0; i < size; i++) {
				String productName = feature.getCustomProperties().get(IMPLEMENTED_PRODUCTS_LIST_NAME_NR_ + i,
						IMPLEMENTED_PRODUCTS_LIST_NAME_NR_TYPE);
				Product implementedProduct = getProductFromId(productName);
				assert implementedProduct != null;
				product.getImplementedProducts().add(implementedProduct);
			}
		}
	}

	private void restoreChildrenListOfProducts(final IFeature feature, final Product product) {
		String sizeStr = feature.getCustomProperties().get(CHILDREN_PRODUCTS_LIST_SIZE,
				CHILDREN_PRODUCTS_LIST_SIZE_TYPE);
		if (sizeStr != null) {
			int size = Integer.parseInt(sizeStr);
			for (int i = 0; i < size; i++) {
				String productName = feature.getCustomProperties().get(CHILDREN_PRODUCTS_LIST_NAME_NR_ + i,
						CHILDREN_PRODUCTS_LIST_NAME_NR_TYPE);
				Product childrenProduct = getProductFromId(productName);
				assert childrenProduct != null;
				product.getChildren().add(childrenProduct);
			}
		}
	}

	private void convertConstraints(final List<IConstraint> constraints) {
		for (IConstraint constraint : constraints) {
			convertConstraintNodeRec(constraint.getNode());
		}
	}

	private void convertConstraintNodeRec(final Node node) {
		// create a CNF from nodes enables separating the concerns how to transform the
		// different groups.
		// A requires B <=> CNF: Not(A) or B
		// A excludes B <=> CNF: Not(A) or Not(B)
		Node cnfNode = node.toCNF();
		if (Prop4JUtils.isComplexNode(cnfNode)) {
			for (Node child : cnfNode.getChildren()) {
				convertConstraintNodeRec(child);
			}
		} else {
			convertConstraintNode(cnfNode);
		}
	}

	private void convertConstraintNode(final Node cnfNode) {
		// node is an implies --> requires attribute
		if (Prop4JUtils.isRequires(cnfNode)) {
			Node sourceLiteral = Prop4JUtils.getFirstNegativeLiteral(cnfNode);
			Node targetLiteral = Prop4JUtils.getFirstPositiveLiteral(cnfNode);
			if (Prop4JUtils.isLiteral(sourceLiteral) && Prop4JUtils.isLiteral(targetLiteral)) {
				// node is an implies --> requires attribute
				Product sourceProduct = getProductFromId(Prop4JUtils.getLiteralName((Literal) sourceLiteral));
				Product targetProduct = getProductFromId(Prop4JUtils.getLiteralName((Literal) targetLiteral));
				sourceProduct.getRequires().add(targetProduct);
			} else {
				// TODO: create constraint from it
			}
		}
		// node is an excludes --> excludes attribute
		else if (Prop4JUtils.isExcludes(cnfNode)) {
			Node sourceLiteral = Prop4JUtils.getLeftNode(cnfNode);
			Node targetLiteral = Prop4JUtils.getRightNode(cnfNode);
			if (Prop4JUtils.isLiteral(sourceLiteral) && Prop4JUtils.isLiteral(targetLiteral)) {
				// node is an excludes --> excludes attribute
				Product sourceProduct = getProductFromId(Prop4JUtils.getLiteralName((Literal) sourceLiteral));
				Product targetProduct = getProductFromId(Prop4JUtils.getLiteralName((Literal) targetLiteral));
				sourceProduct.getExcludes().add(targetProduct);
			} else {
				// TODO: create constraint from it
			}
		} else {
			// TODO: create constraint from it
		}
	}

	private Product getProductFromId(final String productId) {
		return asq.getProducts().get(productId);
	}

	private boolean isEnumSubFeature(final IFeature feature) {
		// works as each tree in FeatureIDE has only one cardinality across all
		// sub-features.
		IFeature parent = FeatureUtils.getParent(feature);
		return parent != null && TraVarTUtils.isEnumerationType(parent);
	}
}
