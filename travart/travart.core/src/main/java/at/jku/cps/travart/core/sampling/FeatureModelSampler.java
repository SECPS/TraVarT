package at.jku.cps.travart.core.sampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.core.common.ISampler;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
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

public class FeatureModelSampler implements ISampler<IFeatureModel> {

	@Override
	public Set<Map<IConfigurable, Boolean>> sample(final IFeatureModel fm) throws NotSupportedVariablityTypeException {
		List<List<String>> configurations = findConfigurations(fm);
		Set<Map<IConfigurable, Boolean>> configurables = new HashSet<>();
		for (final List<String> fmSample : configurations) {
			Map<IConfigurable, Boolean> config = new HashMap<>();
			for (String selectedFeature : fmSample) {
				IConfigurable configurable = new IConfigurable() {
					boolean selected;

					@Override
					public void setSelected(final boolean selected) {
						this.selected = selected;
					}

					@Override
					public boolean isSelected() {
						return selected;
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

	private List<List<String>> findConfigurations(final IFeatureModel fm) throws NotSupportedVariablityTypeException {
		FeatureModelFormula formula = new FeatureModelFormula(fm);
		List<LiteralSet> samples = LongRunningWrapper.runMethod(new TWiseConfigurationGenerator(formula.getCNF(), 3));
		List<List<String>> configurations = new ArrayList<>(samples.size());
		for (LiteralSet sample : samples) {
			List<String> names = formula.getCNF().getVariables().convertToString(sample);
			configurations.add(names);
		}
		return configurations;
	}

	@Override
	public boolean verifySampleAs(final IFeatureModel fm, final Map<IConfigurable, Boolean> sample)
			throws NotSupportedVariablityTypeException {
		FeatureModelFormula formula = new FeatureModelFormula(fm);
		Configuration config = new Configuration(formula);
		for (Entry<IConfigurable, Boolean> entry : sample.entrySet()) {
			IFeature feature = FeatureUtils.getFeature(fm, entry.getKey().getName());
			if (feature != null) {
				String featureName = FeatureUtils.getName(feature);
				if (entry.getValue()) {
					config.setManual(featureName, Selection.SELECTED);
				} else {
					config.setManual(featureName, Selection.UNSELECTED);
				}
			}
		}
		ConfigurationPropagator prop = new ConfigurationPropagator(formula, config);
		return LongRunningWrapper.runMethod(prop.isValidNoHidden());
	}
}
