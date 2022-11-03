package at.jku.cps.travart.core.helpers;

import at.jku.cps.travart.core.common.FeatureMetaData;
import at.jku.cps.travart.core.transformation.DefaultModelTransformationProperties;
import de.vill.model.Attribute;
import de.vill.model.Feature;
import de.vill.model.Group;
import de.vill.model.constraint.AndConstraint;
import de.vill.model.constraint.Constraint;
import de.vill.model.constraint.ImplicationConstraint;
import de.vill.model.constraint.LiteralConstraint;
import de.vill.model.constraint.NotConstraint;
import de.vill.model.constraint.OrConstraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UVLUtils {
    public static boolean isAbstract(final Feature feature) {
        if (feature.getAttributes().get("abstract") == null) {
            return false;
        }

        return Boolean.parseBoolean(feature.getAttributes().get("abstract").getValue().toString());
    }

    public static Object getAttributeValue(final Feature feature, final String attributeName) {
        if (feature.getAttributes().get(attributeName) == null) {
            return null;
        }

        return feature.getAttributes().get(attributeName).getValue();
    }

    public static List<Feature> getChildren(final Feature feature) {
        final List<Feature> children = new ArrayList<>();
        feature.getChildren()
                .forEach(
                        group -> children.addAll(group.getFeatures())
                );

        return children;
    }

    public static Boolean checkGroupType(final Feature feature, final Group.GroupType groupType) {
        // if there is a parent then ok else current is root
        final Optional<Feature> parent = TraVarTUtils.getParent(feature, feature, null);
        return parent.map(
                        value -> value.getChildren()
                                .stream()
                                .anyMatch(group -> group.GROUPTYPE.equals(groupType))
                )
                .orElse(false);
    }

    public static boolean isComplexConstraint(final Constraint constraint) {
        if (constraint instanceof LiteralConstraint) {
            return false;
        }
        boolean isComplex = false;
        for (final Constraint child : constraint.getConstraintSubParts()) {
            isComplex = isComplex || !(child instanceof LiteralConstraint);
        }
        return isComplex;
    }


    public static boolean isRequires(final Constraint constraint) {
        if (!(constraint instanceof OrConstraint) || constraint.getConstraintSubParts().size() != 2) {
            return false;
        }
        final OrConstraint or = (OrConstraint) constraint;
        final Constraint left = or.getLeft();
        final Constraint right = or.getRight();
        // Not(A) or B || A or Not(B) --> Both are implies constraints
        return isNegativeLiteral(left) && isPositiveLiteral(right)
                || isPositiveLiteral(left) && isNegativeLiteral(right);
    }

    public static Constraint getFirstNegativeLiteral(final Constraint constraint) {
        if (isNegativeLiteral(constraint)) {
            return constraint;
        }
        for (final Constraint child : constraint.getConstraintSubParts()) {
            if (isNegativeLiteral(child)) {
                return child;
            }
        }
        return null;
    }


    public static Constraint getFirstPositiveLiteral(final Constraint constraint) {
        if (isPositiveLiteral(constraint)) {
            return constraint;
        }
        for (final Constraint child : constraint.getConstraintSubParts()) {
            if (isPositiveLiteral(child)) {
                return child;
            }
        }
        return null;
    }

    public static boolean isExcludes(final Constraint constraint) {
        if (!(constraint instanceof OrConstraint) || constraint.getConstraintSubParts().size() != 2) {
            return false;
        }
        final OrConstraint or = (OrConstraint) constraint;
        final Constraint left = or.getLeft();
        final Constraint right = or.getRight();
        // Not(A) or Not(B) --> excludes
        // TODO: check
        return (isNegativeLiteral(left)) && (isNegativeLiteral(right));
    }

    public static boolean isNegativeLiteral(final Constraint constraint) {
        return (constraint instanceof NotConstraint) && (((NotConstraint) constraint).getContent() instanceof LiteralConstraint);
    }

    public static boolean isPositiveLiteral(final Constraint constraint) {
        return constraint instanceof LiteralConstraint;
    }

    public static int getMaxDepth(final Constraint constraint) {
        int count = 1;
        for (final Constraint child : constraint.getConstraintSubParts()) {
            final int childCount = getMaxDepth(child) + 1;
            if (childCount > count) {
                count = childCount;
            }
        }
        return count;
    }

    // todo: check
    public static Constraint getRightConstraint(final Constraint constraint) {
        if (constraint instanceof AndConstraint) {
            return ((AndConstraint) constraint).getRight();
        } else if (constraint instanceof OrConstraint) {
            return ((OrConstraint) constraint).getRight();
        } else if (constraint instanceof ImplicationConstraint) {
            return ((ImplicationConstraint) constraint).getRight();
        }

        // not yet implemented
        return null;
    }

    private static List<Feature> findRoots(final Map<String, FeatureMetaData> featureMetaDataMap) {
        final List<Feature> roots = new ArrayList<>();
        for (final String key : featureMetaDataMap.keySet()) {
            if (Boolean.FALSE.equals(featureMetaDataMap.get(key).getHasParent())) {
                roots.add(featureMetaDataMap.get(key).getFeature());
            }
        }
        return roots;
    }

    public static String deriveFeatureModelRoot(final Map<String, FeatureMetaData> featureMetaDataMap,
                                                final String rootName
    ) {
        final List<Feature> roots = findRoots(featureMetaDataMap);
        if (roots.size() != 1) {
            // artificial root - abstract and hidden
            final Feature artificialRoot = new Feature(rootName);
            artificialRoot.getAttributes().put(
                    "abstract",
                    new Attribute("abstract", true)
            );
            artificialRoot.getAttributes().put(
                    "hidden",
                    new Attribute("hidden", true)
            );
            // add property to identify the virtual root
            artificialRoot.getAttributes().put(
                    "ARTIFICIAL_MODEL_NAME",
                    new Attribute("ARTIFICIAL_MODEL_NAME", DefaultModelTransformationProperties.ARTIFICIAL_MODEL_NAME)
            );
            featureMetaDataMap.put(rootName, new FeatureMetaData(
                    Boolean.FALSE,
                    null,
                    null,
                    artificialRoot,
                    new HashMap<>()
            ));

            for (final Feature feature : roots) {
                final FeatureMetaData temp = featureMetaDataMap.get(feature.getFeatureName());
                temp.setHasParent(Boolean.TRUE);
                temp.setParentName(artificialRoot.getFeatureName());
            }

            return rootName;
        }

        final Feature root = roots.get(0);
        return root.getFeatureName();
    }
}

























