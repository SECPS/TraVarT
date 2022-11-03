package at.jku.cps.travart.core.common;

import de.vill.model.Feature;
import de.vill.model.Group;

import java.util.Map;

public class FeatureMetaData {
    String parentName;
    Group.GroupType parentGroupType;
    Feature feature;
    Map<Group.GroupType, Group> groups;
    Boolean hasParent;

    public FeatureMetaData(final Boolean hasParent, final String parentName, final Group.GroupType parentGroupType, final Feature feature, final Map<Group.GroupType, Group> groups) {
        this.hasParent = hasParent;
        this.parentName = parentName;
        this.parentGroupType = parentGroupType;
        this.feature = feature;
        this.groups = groups;
    }

    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(final String parentName) {
        this.parentName = parentName;
    }

    public Group.GroupType getParentGroupType() {
        return this.parentGroupType;
    }

    public void setParentGroupType(final Group.GroupType parentGroupType) {
        this.parentGroupType = parentGroupType;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public void setFeature(final Feature feature) {
        this.feature = feature;
    }

    public Map<Group.GroupType, Group> getGroups() {
        return this.groups;
    }

    public void setGroups(final Map<Group.GroupType, Group> groups) {
        this.groups = groups;
    }

    public Boolean getHasParent() {
        return this.hasParent;
    }

    public void setHasParent(final Boolean hasParent) {
        this.hasParent = hasParent;
    }
}
