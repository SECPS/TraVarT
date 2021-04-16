package at.jku.cps.travart.core.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.prop4j.Node;

import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.IFeatureModelFactory;

public final class TraVarTUtils {

	private TraVarTUtils() {
	}

	public static Node retriveLeftOperand(final Node node) {
		return node.getChildren()[0];
	}

	public static String retriveLeftOperandName(final Node node) {
		Node left = retriveLeftOperand(node);
		return left.getContainedFeatures().get(0);
	}

	public static Node retriveRightOperand(final Node node) {
		Objects.checkIndex(1, node.getChildren().length);
		return node.getChildren()[1];
	}

	public static String retriveRightOperandName(final Node node) {
		Node right = retriveRightOperand(node);
		return right.getContainedFeatures().get(0);
	}

	public static String[] splitString(final String toSplit, final String delimiter) {
		return Arrays.stream(toSplit.split(delimiter)).map(String::trim).filter(s -> !s.isEmpty() && !s.isBlank())
				.toArray(String[]::new);
	}

	public static boolean isEnumerationType(final IFeature feature) {
		return FeatureUtils.isOr(feature) || FeatureUtils.isAlternative(feature) || FeatureUtils.isMultiple(feature);
	}

	/**
	 * Finds all features of the feature model, which do not have a parent and
	 * defines either an artificial root feature (list of roots.size() > 1) or sets
	 * the one root feature as the root feature of the feature model.
	 *
	 * @param factory - The feature model factory to create features (artificial
	 *                root).
	 * @param fm      - The feature model to create the root feature for.
	 */
	public static void deriveFeatureModelRoot(final IFeatureModelFactory factory, final IFeatureModel fm) {
		List<IFeature> roots = findRoots(fm);
		if (roots.size() != 1) {
			// artificial root - abstract and hidden feature
			IFeature rootFeature = factory.createFeature(fm, DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME);
			FeatureUtils.addFeature(fm, rootFeature);
			FeatureUtils.setRoot(fm, rootFeature);
			FeatureUtils.setOr(rootFeature);
			FeatureUtils.setAbstract(rootFeature, true);
			FeatureUtils.setHiddden(rootFeature, true);
			for (IFeature feature : roots) {
				FeatureUtils.addChild(rootFeature, feature);
			}
		} else {
			// make this only root to the root of the model
			IFeature root = roots.get(0);
			FeatureUtils.setRoot(fm, root);
		}
	}

	private static List<IFeature> findRoots(final IFeatureModel fm) {
		List<IFeature> roots = new ArrayList<>();
		for (IFeature feature : FeatureUtils.getFeatures(fm)) {
			if (FeatureUtils.getParent(feature) == null) {
				roots.add(feature);
			}
		}
		return roots;
	}

	public static boolean isParentFeatureOf(final IFeature child, final IFeature parent) {
		if (child == null || parent == null) {
			return false;
		}
		IFeature iterate = FeatureUtils.getParent(child);
		String parentName = FeatureUtils.getName(parent);
		while (iterate != null) {
			if (FeatureUtils.getName(iterate).equals(parentName)) {
				return true;
			}
			iterate = FeatureUtils.getParent(iterate);
		}
		return false;
	}

	public static void printModelStatistics(final IFeatureModel fm) {
		System.out.println("Feature Model Statistics: ");
		System.out.println("Root Name: " + FeatureUtils.getRoot(fm).getName());
		System.out.println("#Features: " + FeatureUtils.getNumberOfFeatures(fm));
		System.out.println("#Constraints: " + FeatureUtils.getConstraintCount(fm));
		System.out.println("Tree height: " + computeFMHeight(FeatureUtils.getRoot(fm)));
	}

	private static int computeFMHeight(final IFeature feature) {
		if (!FeatureUtils.hasChildren(feature)) {
			return 0;
		}
		int maxDepth = Integer.MIN_VALUE;
		for (IFeature child : FeatureUtils.getChildren(feature)) {
			int depth = computeFMHeight(child);
			if (maxDepth < depth) {
				maxDepth = depth;
			}
		}
		return 1 + maxDepth;
	}
}
