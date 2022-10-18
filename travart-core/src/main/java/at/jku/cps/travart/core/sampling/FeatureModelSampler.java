package at.jku.cps.travart.core.sampling;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.core.common.ISampler;
import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;
import de.ovgu.featureide.fm.core.analysis.cnf.LiteralSet;
import de.ovgu.featureide.fm.core.analysis.cnf.formula.FeatureModelFormula;
import de.ovgu.featureide.fm.core.analysis.cnf.generator.configuration.twise.TWiseConfigurationGenerator;
import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.configuration.Configuration;
import de.ovgu.featureide.fm.core.configuration.ConfigurationPropagator;
import de.ovgu.featureide.fm.core.configuration.Selection;
import de.ovgu.featureide.fm.core.job.LongRunningWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class FeatureModelSampler implements ISampler<IFeatureModel> {

    private static final int INVALID_COUNT = 10;

    private IFeatureModel lastFm;
    private Set<Map<IConfigurable, Boolean>> samples;
    private Set<Map<IConfigurable, Boolean>> invalidSamples;

    @Override
    public Set<Map<IConfigurable, Boolean>> sampleValidConfigurations(final IFeatureModel fm)
            throws NotSupportedVariabilityTypeException {
        if (this.lastFm != fm) {
            this.init(fm);
        }
        return this.samples;
    }

    @Override
    public Set<Map<IConfigurable, Boolean>> sampleValidConfigurations(final IFeatureModel fm, final long maxNumber)
            throws NotSupportedVariabilityTypeException {
        if (this.lastFm != fm) {
            this.init(fm);
        }
        // return only the first maxNumber elements if necessary
        return this.samples.size() > maxNumber ? this.samples.stream().limit(maxNumber).collect(Collectors.toSet()) : this.samples;
    }

    @Override
    public Set<Map<IConfigurable, Boolean>> sampleInvalidConfigurations(final IFeatureModel fm)
            throws NotSupportedVariabilityTypeException {
        if (this.lastFm != fm) {
            this.init(fm);
        }
        return this.invalidSamples;
    }

    @Override
    public Set<Map<IConfigurable, Boolean>> sampleInvalidConfigurations(final IFeatureModel fm, final long maxNumber)
            throws NotSupportedVariabilityTypeException {
        if (this.lastFm != fm) {
            this.init(fm);
        }
        return this.invalidSamples.size() > maxNumber ? this.invalidSamples.stream().limit(maxNumber).collect(Collectors.toSet())
                : this.invalidSamples;
    }

    private void init(final IFeatureModel fm) throws NotSupportedVariabilityTypeException {
        this.lastFm = fm;
        this.samples = this.sample(fm);
        this.invalidSamples = this.sampleInvalid(fm, this.samples);
    }

    private Set<Map<IConfigurable, Boolean>> sample(final IFeatureModel fm) throws NotSupportedVariabilityTypeException {
        final List<List<String>> configurations = this.findConfigurations(fm);
        final Set<Map<IConfigurable, Boolean>> configurables = new HashSet<>();
        for (final List<String> fmSample : configurations) {
            final Map<IConfigurable, Boolean> config = new HashMap<>();
            for (final String selectedFeature : fmSample) {
                final IConfigurable configurable = new IConfigurable() {
                    boolean selected;

                    @Override
                    public boolean isSelected() {
                        return this.selected;
                    }

                    @Override
                    public void setSelected(final boolean selected) {
                        this.selected = selected;
                    }

                    @Override
                    public String getName() {
                        return selectedFeature;
                    }
                };
                configurable.setSelected(true);
                config.put(configurable, true);
            }
            configurables.add(config);
        }
        return configurables;
    }

    private Set<Map<IConfigurable, Boolean>> sampleInvalid(final IFeatureModel fm,
                                                           final Set<Map<IConfigurable, Boolean>> samples) throws NotSupportedVariabilityTypeException {
        final Random rand = new Random();
        final Set<Map<IConfigurable, Boolean>> invalidSamples = new HashSet<>();
        for (final Map<IConfigurable, Boolean> sample : samples) {
            for (int count = 0; count < INVALID_COUNT; count++) {
                final int featureSwitch = rand.nextInt(sample.size());
                final Map<IConfigurable, Boolean> invalid = new HashMap<>();
                int i = 0;
                for (final Entry<IConfigurable, Boolean> entry : sample.entrySet()) {
                    final IConfigurable key = entry.getKey();
                    Boolean value = entry.getValue();
                    if (i == featureSwitch) {
                        key.setSelected(!key.isSelected());
                        value = !value;
                    }
                    invalid.put(key, value);
                    i++;
                }
                // the randomly changed configuration my be valid so do not add it to the
                // invalid samples
                if (!this.verifySampleAs(fm, invalid)) {
                    invalidSamples.add(invalid);
                }
            }
        }
        return invalidSamples;
    }

    private List<List<String>> findConfigurations(final IFeatureModel fm) throws NotSupportedVariabilityTypeException {
        final FeatureModelFormula formula = new FeatureModelFormula(fm);
        final List<LiteralSet> samples = LongRunningWrapper.runMethod(new TWiseConfigurationGenerator(formula.getCNF(), 1));
//		List<LiteralSet> samples = LongRunningWrapper
//				.runMethod(new RandomConfigurationGenerator(formula.getCNF(), 1_000_000));
        final List<List<String>> configurations = new ArrayList<>(samples.size());
        for (final LiteralSet sample : samples) {
            final List<String> names = formula.getCNF().getVariables().convertToString(sample);
            configurations.add(names);
        }
        return configurations;
    }

    @Override
    public boolean verifySampleAs(final IFeatureModel fm, final Map<IConfigurable, Boolean> sample)
            throws NotSupportedVariabilityTypeException {
        final FeatureModelFormula formula = new FeatureModelFormula(fm);
        final Configuration config = new Configuration(formula);
        for (final Entry<IConfigurable, Boolean> entry : sample.entrySet()) {
            final IFeature feature = FeatureUtils.getFeature(fm, entry.getKey().getName());
            if (feature != null) {
                final String featureName = FeatureUtils.getName(feature);
                if (entry.getValue()) {
                    config.setManual(featureName, Selection.SELECTED);
                } else {
                    config.setManual(featureName, Selection.UNSELECTED);
                }
            }
        }
        final ConfigurationPropagator prop = new ConfigurationPropagator(formula, config);
        final boolean valid = LongRunningWrapper.runMethod(prop.isValidNoHidden());
        return valid;
    }
}
