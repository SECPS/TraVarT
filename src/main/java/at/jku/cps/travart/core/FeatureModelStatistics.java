/*******************************************************************************
 * TODO: explanation what the class does
 *
 * @author Kevin Feichtinger
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import at.jku.cps.travart.core.common.IStatistics;
import at.jku.cps.travart.core.helpers.TraVarTUtils;
import de.vill.model.Feature;
import de.vill.model.FeatureModel;
import de.vill.model.Group;

public class FeatureModelStatistics implements IStatistics<FeatureModel> {

	private static FeatureModelStatistics instance;

	private FeatureModelStatistics() {
	}

	public static FeatureModelStatistics getInstance() {
		if (instance == null) {
			instance = new FeatureModelStatistics();
		}
		return instance;
	}

	@Override
	public long getVariabilityElementsCount(final FeatureModel fm) {
		return Objects.requireNonNull(fm).getFeatureMap().size();
	}

	@Override
	public long getConstraintsCount(final FeatureModel fm) {
		return TraVarTUtils.getFeatureConstraints(fm).size() + TraVarTUtils.getGlobalConstraints(fm).size()
				+ TraVarTUtils.getLiteralConstraints(fm).size() + TraVarTUtils.getOwnConstraints(fm).size();
	}

	@Override
	public void logModelStatistics(final Logger logger, final FeatureModel fm) {
		logger.log(Level.INFO, "Root Name: {0}", fm.getRootFeature().getFeatureName());
		logger.log(Level.INFO, "#Features: {0}", getVariabilityElementsCount(fm));
		logger.log(Level.INFO, "#Abstract Features: {0}", countAbstractFeatures(fm));
		logger.log(Level.INFO, "#Mandatory Features: {0}", countMandatoryFeatures(fm));
		logger.log(Level.INFO, "#Optional Features: {0}", countOptionalFeatures(fm));
		logger.log(Level.INFO, "#Or groups: {0}", countOrGroups(fm));
		logger.log(Level.INFO, "#Xor groups: {0}", countXorGroups(fm));
		logger.log(Level.INFO, "#Constraints: {0}", getConstraintsCount(fm));
		logger.log(Level.INFO, "Tree height: {0}", computeFMHeight(TraVarTUtils.getRoot(fm)));
	}

	private long countAbstractFeatures(final FeatureModel fm) {
		return TraVarTUtils.getFeatures(fm).stream().filter(TraVarTUtils::isAbstract).count();
	}

	private long countMandatoryFeatures(final FeatureModel fm) {
		return TraVarTUtils.getFeatures(fm).stream().filter(TraVarTUtils::isMandatory).count();
	}

	private long countOptionalFeatures(final FeatureModel fm) {
		return TraVarTUtils.getFeatures(fm).stream()
				.filter(f -> TraVarTUtils.checkGroupType(f, Group.GroupType.OPTIONAL)).count();
	}

	private long countOrGroups(final FeatureModel fm) {
		return countGroupType(fm, Group.GroupType.OR);
	}

	private long countXorGroups(final FeatureModel fm) {
		return countGroupType(fm, Group.GroupType.ALTERNATIVE);
	}

	private long countGroupType(final FeatureModel fm, final Group.GroupType grouptype) {
		long count = 0;
		for (Feature feature : TraVarTUtils.getFeatures(fm)) {
			count += TraVarTUtils.countGroup(feature, grouptype);
		}
		return count;
	}

	private long computeFMHeight(final Feature feature) {
		if (!TraVarTUtils.hasChildren(feature)) {
			return 0;
		}
		long maxDepth = Integer.MIN_VALUE;
		for (Feature child : TraVarTUtils.getChildren(feature)) {
			long depth = computeFMHeight(child);
			if (maxDepth < depth) {
				maxDepth = depth;
			}
		}
		return 1 + maxDepth;
	}
}
