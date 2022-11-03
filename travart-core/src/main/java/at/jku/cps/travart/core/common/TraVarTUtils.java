package at.jku.cps.travart.core.common;

import de.vill.model.Feature;
import de.vill.model.Group;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


//
//import at.jku.cps.travart.core.exception.ConditionCreationException;
//import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
//import de.ovgu.featureide.fm.core.base.FeatureUtils;
//import de.ovgu.featureide.fm.core.base.IConstraint;
//import de.ovgu.featureide.fm.core.base.IFeature;
//import de.ovgu.featureide.fm.core.base.IFeatureModel;
//import de.ovgu.featureide.fm.core.base.IFeatureModelFactory;
//import org.prop4j.Literal;
//import org.prop4j.Node;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
public class TraVarTUtils {
    public static String[] splitString(final String toSplit, final String delimiter) {
        return Arrays.stream(toSplit.split(delimiter))
                .map(String::trim)
                .filter(s -> !s.isEmpty() && !s.isBlank())
                .toArray(String[]::new);
    }

    /**
     * Creates a Set of names of the selected IConfigurable names.
     *
     * @param samples - the samples of the feature model.
     * @return A Set of name sets of the configuration samples.
     */
    public static Set<Set<String>> createConfigurationNameSet(final Set<Map<IConfigurable, Boolean>> samples) {
        final Set<Set<String>> configurations = new HashSet<>();
        for (final Map<IConfigurable, Boolean> sample : samples) {
            final Set<String> sampleNames = new HashSet<>();
            for (final Map.Entry<IConfigurable, Boolean> sampleEntry : sample.entrySet()) {
                if (sampleEntry.getValue()) {
                    sampleNames.add(sampleEntry.getKey().getName());
                }
            }
            configurations.add(sampleNames);
        }
        return configurations;
    }

    /**
     * Iterate over the {@code createConfigurationNameSet} configurations and find
     * the common feature names of the configurations
     *
     * @param samples - the samples of the feature model.
     * @return A Set of name sets of the configuration samples.
     */
    public static Set<String> getCommonConfigurationNameSet(final Set<Map<IConfigurable, Boolean>> samples) {
        final Set<String> commonNames = new HashSet<>();
        final Set<Set<String>> configurations = createConfigurationNameSet(samples);
        final Iterator<Set<String>> iterator = configurations.iterator();
        Set<String> element = iterator.next();
        iterator.remove();
        commonNames.addAll(element);
        while (iterator.hasNext()) {
            element = iterator.next();
            commonNames.retainAll(element);
        }
        return commonNames;
    }

    /**
     * Finds all features of the feature model, which do not have a parent and
     * defines either an artificial root feature (list of roots.size() > 1) or sets
     * the one root feature as the root feature of the feature model.
     *
     * @param model     - The feature model to create the root feature for.
     * @param rootName  - The name of the feature model root feature.
     * @param isOrGroup - If a virtual root has to be created, {@code isOrGroup}
     *                  sets the group constraint to an Or group if {@code true} and
     *                  to an And group otherwise.
     */
//    public static void deriveFeatureModelRoot(final FeatureModel model, final String rootName, final boolean isOrGroup) {
//        final List<Feature> roots = findRoots(model);
//        if (roots.size() != 1) {
//            // artificial root - abstract and hidden feature
//            final IFeature rootFeature = factory.createFeature(fm, rootName);
//            FeatureUtils.addFeature(fm, rootFeature);
//            FeatureUtils.setRoot(fm, rootFeature);
//            if (isOrGroup) {
//                FeatureUtils.setOr(rootFeature);
//            } else {
//                FeatureUtils.setAnd(rootFeature);
//            }
//            FeatureUtils.setAbstract(rootFeature, true);
//            FeatureUtils.setHiddden(rootFeature, true);
//            FeatureUtils.setMandatory(rootFeature, true);
//            for (final Feature feature : roots) {
//                FeatureUtils.addChild(rootFeature, feature);
//            }
//            // add property to identify the virtual root
//            rootFeature.getCustomProperties().set(DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
//                    String.class.toString(), DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME);
//        } else {
//            // make this only root to the mandatory root of the model
//            final Feature root = roots.get(0);
//            FeatureUtils.setMandatory(root, true);
//            FeatureUtils.setRoot(fm, root);
//        }
//    }
//
//    private static List<Feature> findRoots(final FeatureModel model) {
//        final List<Feature> roots = new ArrayList<>();
//        for (final Feature feature : FeatureUtils.getFeatures(model)) {
//            if (FeatureUtils.getParent(feature) == null) {
//                roots.add(feature);
//            }
//        }
//        return roots;
//    }
//
    public static boolean isParentFeatureOf(final Feature child, final Feature parent) {
        if (child == null || parent == null) {
            return false;
        }
        Optional<Feature> iterate = getParent(child, child, null);
        final String parentName = parent.getFeatureName();
        while (iterate.isPresent()) {
            if (iterate.get().getFeatureName().equals(parentName)) {
                return true;
            }
            iterate = getParent(iterate.get(), iterate.get(), null);
        }

        return false;
    }

    public static boolean isEnumerationType(final Feature feature) {
        return feature.getChildren().stream()
                .anyMatch(
                        group -> (
                                group.GROUPTYPE.equals(Group.GroupType.ALTERNATIVE)
                                        || group.GROUPTYPE.equals(Group.GroupType.OR)
                                // todo: there was something multiple here, check if it works without it :D
                                // || group.GROUPTYPE.equals(Group.GroupType.GROUP_CARDINALITY)
                        )
                );
    }

    public static Optional<Feature> getParent(final Feature feature, final Feature root, final Feature parent) {
        if (root.getChildren().isEmpty()) {
            return Optional.of(parent);
        }

        final List<Feature> children = UVLUtils.getChildren(root);
        if (children.contains(feature)) {
            return Optional.of(root);
        }

        return children.stream()
                .map(child -> getParent(feature, child, parent))
                .flatMap(Optional::stream)
                .findFirst();
    }
}
//
//    private TraVarTUtils() {
//    }
//
//    public static String[] splitString(final String toSplit, final String delimiter) {
//        return Arrays.stream(toSplit.split(delimiter))
//                .map(String::trim)
//                .filter(s -> !s.isEmpty() && !s.isBlank())
//                .toArray(String[]::new);
//    }
//

//
//    public static boolean isVirtualRootFeature(final IFeature feature) {
//        return FeatureUtils.getName(feature).equals(DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME)
//                && FeatureUtils.isAbstract(feature) && FeatureUtils.isHidden(feature)
//                || feature.getCustomProperties().has(DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
//                String.class.toString());
//    }
//
//    /**
//     * Finds all features of the feature model, which do not have a parent and
//     * defines either an artificial root feature (list of roots.size() > 1) or sets
//     * the one root feature as the root feature of the feature model.
//     *
//     * @param factory   - The feature model factory to create features (artificial
//     *                  root).
//     * @param fm        - The feature model to create the root feature for.
//     * @param rootName  - The name of the feature model root feature.
//     * @param isOrGroup - If a virtual root has to be created, {@code isOrGroup}
//     *                  sets the group constraint to an Or group if {@code true} and
//     *                  to an And group otherwise.
//     */
//    public static void deriveFeatureModelRoot(final IFeatureModelFactory factory, final IFeatureModel fm,
//                                              final String rootName, final boolean isOrGroup) {
//        final List<IFeature> roots = findRoots(fm);
//        if (roots.size() != 1) {
//            // artificial root - abstract and hidden feature
//            final IFeature rootFeature = factory.createFeature(fm, rootName);
//            FeatureUtils.addFeature(fm, rootFeature);
//            FeatureUtils.setRoot(fm, rootFeature);
//            if (isOrGroup) {
//                FeatureUtils.setOr(rootFeature);
//            } else {
//                FeatureUtils.setAnd(rootFeature);
//            }
//            FeatureUtils.setAbstract(rootFeature, true);
//            FeatureUtils.setHiddden(rootFeature, true);
//            FeatureUtils.setMandatory(rootFeature, true);
//            for (final IFeature feature : roots) {
//                FeatureUtils.addChild(rootFeature, feature);
//            }
//            // add property to identify the virtual root
//            rootFeature.getCustomProperties().set(DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME,
//                    String.class.toString(), DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME);
//        } else {
//            // make this only root to the mandatory root of the model
//            final IFeature root = roots.get(0);
//            FeatureUtils.setMandatory(root, true);
//            FeatureUtils.setRoot(fm, root);
//        }
//    }
//
//    /**
//     * Finds all features of the feature model, which do not have a parent and
//     * defines either an artificial root feature (list of roots.size() > 1) or sets
//     * the one root feature as the root feature of the feature model. Equals calling
//     * {@code deriveFeatureModelRoot(final IFeatureModelFactory factory,
//     * final IFeatureModel fm, final String rootName, final boolean isOrGroup)} with
//     * the {@code rootName} set to
//     * {@code DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME}.
//     *
//     * @param factory   - The feature model factory to create features (artificial
//     *                  root).
//     * @param fm        - The feature model to create the root feature for.
//     * @param isOrGroup - if a virtual root has to be created, {@code isOrGroup}
//     *                  sets the group constraint to an Or group if {@code true} and
//     *                  to an And group otherwise.
//     */
//    public static void deriveFeatureModelRoot(final IFeatureModelFactory factory, final IFeatureModel fm,
//                                              final boolean isOrGroup) {
//        deriveFeatureModelRoot(factory, fm, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME, isOrGroup);
//    }
//
//    /**
//     * Finds all features of the feature model, which do not have a parent and
//     * defines either an artificial root feature (list of roots.size() > 1) or sets
//     * the one root feature as the root feature of the feature model. Equals calling
//     * {@code deriveFeatureModelRoot(final IFeatureModelFactory factory,
//     * final IFeatureModel fm, final String rootName, final boolean isOrGroup)} with
//     * the {@code rootName} set to
//     * {@code DefaultModelTransformationProperties.ARTIFICIAL_ROOT_NAME} and
//     * {@code isOrGroup} set to {@code true}.
//     *
//     * @param factory - The feature model factory to create features (artificial
//     *                root).
//     * @param fm      - The feature model to create the root feature for.
//     */
//    public static void deriveFeatureModelRoot(final IFeatureModelFactory factory, final IFeatureModel fm) {
//        deriveFeatureModelRoot(factory, fm, DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME, true);
//    }
//
//    private static List<IFeature> findRoots(final IFeatureModel fm) {
//        final List<IFeature> roots = new ArrayList<>();
//        for (final IFeature feature : FeatureUtils.getFeatures(fm)) {
//            if (FeatureUtils.getParent(feature) == null) {
//                roots.add(feature);
//            }
//        }
//        return roots;
//    }
//
//    public static boolean isParentFeatureOf(final IFeature child, final IFeature parent) {
//        if (child == null || parent == null) {
//            return false;
//        }
//        IFeature iterate = FeatureUtils.getParent(child);
//        final String parentName = FeatureUtils.getName(parent);
//        while (iterate != null) {
//            if (FeatureUtils.getName(iterate).equals(parentName)) {
//                return true;
//            }
//            iterate = FeatureUtils.getParent(iterate);
//        }
//        return false;
//    }
//
//    public static void logModelStatistics(final Logger logger, final IFeatureModel fm) {
//        logger.log(Level.INFO, String.format("Root Name: %s", FeatureUtils.getRoot(fm).getName()));
//        logger.log(Level.INFO, String.format("#Features: %s", FeatureUtils.getNumberOfFeatures(fm)));
//        logger.log(Level.INFO,
//                String.format("#Abstract Features: %s", countAbstractFeatures(FeatureUtils.getFeatures(fm))));
//        logger.log(Level.INFO,
//                String.format("#Mandatory Features: %s", countMandatorySetFeatures(FeatureUtils.getFeatures(fm))));
//        logger.log(Level.INFO,
//                String.format("#Optional Features: %s", countOptionalFeatures(FeatureUtils.getFeatures(fm))));
//        logger.log(Level.INFO, String.format("#Or groups: %s", countOrGroups(FeatureUtils.getFeatures(fm))));
//        logger.log(Level.INFO, String.format("#Xor groups: %s", countXorGroups(FeatureUtils.getFeatures(fm))));
//        logger.log(Level.INFO, String.format("#Constraints: %s", FeatureUtils.getConstraintCount(fm)));
//        logger.log(Level.INFO, String.format("#Complex Constraints groups: %s",
//                countComplexConstraints(FeatureUtils.getConstraints(fm))));
//        logger.log(Level.INFO, String.format("Tree height: %s", computeFMHeight(FeatureUtils.getRoot(fm))));
//    }
//
//    public static String getRootFeatureName(final IFeatureModel fm) {
//        return FeatureUtils.getRoot(fm).getName();
//    }
//
//    public static long getNumberOfFeatures(final IFeatureModel fm) {
//        return FeatureUtils.getNumberOfFeatures(fm);
//    }
//
//    public static long getNumberOfAbstractFeatures(final IFeatureModel fm) {
//        return countAbstractFeatures(FeatureUtils.getFeatures(fm));
//    }
//
//    public static long getNumberOfMandatorySetFeatures(final IFeatureModel fm) {
//        return countMandatorySetFeatures(FeatureUtils.getFeatures(fm));
//    }
//
//    public static long getNumberOfOptionalFeatures(final IFeatureModel fm) {
//        return countOptionalFeatures(FeatureUtils.getFeatures(fm));
//    }
//
//    public static long getNumberOfOrGroups(final IFeatureModel fm) {
//        return countOrGroups(FeatureUtils.getFeatures(fm));
//    }
//
//    public static long getNumberOfXorGroups(final IFeatureModel fm) {
//        return countXorGroups(FeatureUtils.getFeatures(fm));
//    }
//
//    public static long getNumberOfConstraints(final IFeatureModel fm) {
//        return FeatureUtils.getConstraintCount(fm);
//    }
//
//    public static long getNumberOfComplexConstraints(final IFeatureModel fm) {
//        return countComplexConstraints(FeatureUtils.getConstraints(fm));
//    }
//
//    public static long getFeatureModelHeight(final IFeatureModel fm) {
//        return computeFMHeight(FeatureUtils.getRoot(fm));
//    }
//
//    private static long countComplexConstraints(final List<IConstraint> constraints) {
//        return constraints.stream().filter(c -> c.getContainedFeatures().size() >= 3).count();
//    }
//
//    private static long countXorGroups(final Collection<IFeature> features) {
//        return features.stream().filter(FeatureUtils::isAlternative).count();
//    }
//
//    private static long countOrGroups(final Collection<IFeature> features) {
//        return features.stream().filter(FeatureUtils::isOr).count();
//    }
//
//    private static long countOptionalFeatures(final Collection<IFeature> features) {
//        return features.stream().filter(f -> !FeatureUtils.isMandatory(f)).count();
//    }
//
//    private static long countMandatorySetFeatures(final Collection<IFeature> features) {
//        return features.stream().filter(FeatureUtils::isMandatorySet).count();
//    }
//
//    private static long countAbstractFeatures(final Collection<IFeature> features) {
//        return features.stream().filter(FeatureUtils::isAbstract).count();
//    }
//
//    private static int computeFMHeight(final IFeature feature) {
//        if (!FeatureUtils.hasChildren(feature)) {
//            return 0;
//        }
//        int maxDepth = Integer.MIN_VALUE;
//        for (final IFeature child : FeatureUtils.getChildren(feature)) {
//            final int depth = computeFMHeight(child);
//            if (maxDepth < depth) {
//                maxDepth = depth;
//            }
//        }
//        return 1 + maxDepth;
//    }
//
//    /**
//     * Creates a Set of names of the selected IConfigurable names.
//     *
//     * @param samples - the samples of the feature model.
//     * @return A Set of name sets of the configuration samples.
//     */
//    public static Set<Set<String>> createConfigurationNameSet(final Set<Map<IConfigurable, Boolean>> samples) {
//        final Set<Set<String>> configurations = new HashSet<>();
//        for (final Map<IConfigurable, Boolean> sample : samples) {
//            final Set<String> sampleNames = new HashSet<>();
//            for (final Map.Entry<IConfigurable, Boolean> sampleEntry : sample.entrySet()) {
//                if (sampleEntry.getValue()) {
//                    sampleNames.add(sampleEntry.getKey().getName());
//                }
//            }
//            configurations.add(sampleNames);
//        }
//        return configurations;
//    }
//


//
//    public static boolean isInItSelfConstraint(final IConstraint constraint) {
//        if (Prop4JUtils.nodeChildrenCount(constraint.getNode()) != 2) {
//            return false;
//        }
//        final Literal left = Prop4JUtils.getLeftLiteral(constraint.getNode());
//        final Literal right = Prop4JUtils.getRightLiteral(constraint.getNode());
//        return left != null && right != null && left.equals(right);
//    }
//
//    public static Node consumeToBinaryCondition(final List<IFeature> features, final Class<? extends Node> clazz,
//                                                final boolean negative) throws ConditionCreationException {
//        if (features.isEmpty()) {
//            throw new ConditionCreationException(new IllegalArgumentException("Set of decisions is empty!"));
//        }
//        if (features.stream().anyMatch(d -> d == null)) {
//            throw new ConditionCreationException(
//                    new IllegalArgumentException("Set of decisions contains a null value"));
//        }
//        if (features.size() == 1) {
//            if (features.get(0) == null) {
//                throw new ConditionCreationException(new IllegalArgumentException("Only existing decision is null!"));
//            }
//            return negative ? Prop4JUtils.createNot(Prop4JUtils.createLiteral(features.get(0)))
//                    : Prop4JUtils.createLiteral(features.get(0));
//        }
//        try {
//            final List<Node> nodes = new ArrayList<>(features.size());
//            // get the constructor
//            final Constructor<? extends Node> constructor = clazz.getConstructor(Object[].class);
//            // take the first two and create the first ABinaryCondition
//            Node first = Prop4JUtils.createLiteral(features.remove(0));
//            Node second = Prop4JUtils.createLiteral(features.remove(0));
//            if (negative) {
//                first = Prop4JUtils.createNot(Prop4JUtils.createLiteral(first));
//                second = Prop4JUtils.createNot(Prop4JUtils.createLiteral(second));
//            }
//            nodes.add(first);
//            nodes.add(second);
//            while (!features.isEmpty()) {
//                Node next = Prop4JUtils.createLiteral(features.remove(0));
//                if (negative) {
//                    next = Prop4JUtils.createNot(next);
//                }
//                nodes.add(next);
//            }
//            final Node[] parameters = new Node[nodes.size()];
//            nodes.toArray(parameters);
//            final Node condition = constructor.newInstance(new Object[]{parameters});
//            return condition;
//        } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//                | NoSuchMethodException | SecurityException e) {
//            throw new ConditionCreationException(e);
//        }
//    }
//}
