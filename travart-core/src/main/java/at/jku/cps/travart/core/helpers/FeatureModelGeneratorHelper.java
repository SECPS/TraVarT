package at.jku.cps.travart.core.helpers;

import at.jku.cps.travart.core.common.FeatureMetaData;
import de.vill.model.Feature;
import de.vill.model.FeatureModel;
import de.vill.model.Group;
import de.vill.model.constraint.Constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureModelGeneratorHelper {
    public static Map<String, List<String>> createParentChildRelationship(final Map<String, FeatureMetaData> featureMetaDataMap) {
        final Map<String, List<String>> parentChildRelationshipMap = new HashMap<>();

        featureMetaDataMap.keySet()
                .forEach(
                        key -> {
                            if (featureMetaDataMap.get(key).getHasParent()) {
                                final List<String> children = parentChildRelationshipMap.getOrDefault(
                                        featureMetaDataMap.get(key).getParentName(),
                                        new ArrayList<>()
                                );
                                children.add(key);
                                parentChildRelationshipMap.put(
                                        featureMetaDataMap.get(key).getParentName(),
                                        children
                                );
                            }
                        }
                );


        return parentChildRelationshipMap;
    }

    public static FeatureModel generateModel(
            final FeatureModel model,
            final String rootFeatureName,
            final Map<String, FeatureMetaData> featureMetaDataMap,
            final List<Constraint> constraints,
            final Map<String, List<String>> parentToChildRelationshipMap
    ) {

        // create the tree structure of the features
        createTree(
                rootFeatureName,
                parentToChildRelationshipMap,
                featureMetaDataMap
        );

        // the root feature is already a tree with all the features
        model.setRootFeature(featureMetaDataMap.get(rootFeatureName).getFeature());

        // add constraints
        model.getOwnConstraints().addAll(constraints);

        // return result
        return model;
    }

    private static void createTree(final String current, final Map<String, List<String>> parentToChildRelationshipMap, final Map<String, FeatureMetaData> featureMetaDataMap) {
        // if no child, it means leaf node ie no processing needed
        final FeatureMetaData currentIVMLFeature = featureMetaDataMap.get(current);
        //if (Boolean.FALSE.equals(parentToChildRelationshipMap.containsKey(current))) {
        if (currentIVMLFeature.getHasParent()) {
            handleNonRootNodes(currentIVMLFeature, featureMetaDataMap);
            return;
        }

        // handle all the children first
        for (final String child : parentToChildRelationshipMap.get(current)) {
            createTree(child, parentToChildRelationshipMap, featureMetaDataMap);
        }

        // check if the current element is root node or not
        if (Boolean.FALSE.equals(currentIVMLFeature.getHasParent())) {
            handleRoot(currentIVMLFeature, featureMetaDataMap);
        } else {
            handleNonRootNodes(currentIVMLFeature, featureMetaDataMap);
        }
    }

    private static void handleRoot(final FeatureMetaData currentIVMLFeature, final Map<String, FeatureMetaData> featureMetaDataMap) {
        final Feature rootFeature = currentIVMLFeature.getFeature();
        rootFeature.getChildren().clear();
        for (final Group.GroupType group : currentIVMLFeature.getGroups().keySet()) {
            rootFeature.addChildren(currentIVMLFeature.getGroups().get(group));
        }
        currentIVMLFeature.setFeature(rootFeature);
        featureMetaDataMap.put(
                currentIVMLFeature.getParentName(),
                currentIVMLFeature
        );
    }

    private static void handleNonRootNodes(final FeatureMetaData currentIVMLFeature, final Map<String, FeatureMetaData> featureMetaDataMap) {
        final FeatureMetaData parentOfCurrent = featureMetaDataMap.get(currentIVMLFeature.getParentName());
        final Group.GroupType parentGroupType = currentIVMLFeature.getParentGroupType();
        final Map<Group.GroupType, Group> parentGroupMap = parentOfCurrent.getGroups();
        final Group group;
        if (parentGroupMap.containsKey(parentGroupType)) {
            group = parentGroupMap.get(parentGroupType);
        } else {
            group = new Group(parentGroupType);
        }
        group.getFeatures().add(currentIVMLFeature.getFeature());
        parentGroupMap.put(parentGroupType, group);

        // update the child and see the magic
        final Feature parentFeature = parentOfCurrent.getFeature();
        for (final Group.GroupType grp : parentOfCurrent.getGroups().keySet()) {
            parentFeature.getChildren().clear();
            parentFeature.addChildren(parentOfCurrent.getGroups().get(grp));
        }
        parentOfCurrent.setFeature(parentFeature);
        parentOfCurrent.setGroups(parentGroupMap);
        featureMetaDataMap.put(
                currentIVMLFeature.getParentName(),
                parentOfCurrent
        );
    }
}
