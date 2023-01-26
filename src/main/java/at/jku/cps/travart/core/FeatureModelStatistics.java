package at.jku.cps.travart.core;

import at.jku.cps.travart.core.common.IStatistics;
import de.vill.model.FeatureModel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeatureModelStatistics implements IStatistics<FeatureModel> {
    @Override
    public long getVariabilityElementsCount(final FeatureModel model) {
        return model.getFeatureMap().size();
    }

    @Override
    public long getConstraintsCount(final FeatureModel model) {
        return model.getConstraints().size();
    }

    @Override
    public void logModelStatistics(final Logger logger, final FeatureModel model) {
        logger.log(Level.INFO, String.format("Root Name: %s", model.getRootFeature().getFeatureName()));
        //        logger.log(Level.INFO, String.format("#Features: %s", FeatureUtils.getNumberOfFeatures(fm)));
        //        logger.log(Level.INFO, String.format("#Abstract Features: %s", countAbstractFeatures(FeatureUtils.getFeatures(fm))));
        //        logger.log(Level.INFO, String.format("#Mandatory Features: %s", countMandatorySetFeatures(FeatureUtils.getFeatures(fm))));
        //        logger.log(Level.INFO, String.format("#Optional Features: %s", countOptionalFeatures(FeatureUtils.getFeatures(fm))));
        //        logger.log(Level.INFO, String.format("#Or groups: %s", countOrGroups(FeatureUtils.getFeatures(fm))));
        //        logger.log(Level.INFO, String.format("#Xor groups: %s", countXorGroups(FeatureUtils.getFeatures(fm))));
        //        logger.log(Level.INFO, String.format("#Constraints: %s", FeatureUtils.getConstraintCount(fm)));
        //        logger.log(Level.INFO, String.format("#Complex Constraints groups: %s", countComplexConstraints(FeatureUtils.getConstraints(fm))));
        //        logger.log(Level.INFO, String.format("Tree height: %s", computeFMHeight(FeatureUtils.getRoot(fm))));
    }
}
