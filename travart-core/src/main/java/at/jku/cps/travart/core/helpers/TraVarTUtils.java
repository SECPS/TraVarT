package at.jku.cps.travart.core.helpers;

import at.jku.cps.travart.core.common.IConfigurable;
import de.vill.model.Feature;
import de.vill.model.Group;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


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