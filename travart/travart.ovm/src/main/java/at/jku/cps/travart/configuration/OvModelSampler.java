package at.jku.cps.travart.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import at.jku.cps.travart.core.common.IConfigurable;
import at.jku.cps.travart.core.common.ISampler;
import at.jku.cps.travart.ovm.common.OvModelUtils;
import at.jku.cps.travart.ovm.model.IIdentifiable;
import at.jku.cps.travart.ovm.model.IOvModel;
import at.jku.cps.travart.ovm.model.IOvModelElement;
import at.jku.cps.travart.ovm.model.impl.Identifiable;
import at.jku.cps.travart.ovm.transformation.DefaultOvModelTransformationProperties;

/**
 * This class provides utils for sampling a model.
 *
 * @author johannstoebich
 */
public class OvModelSampler implements ISampler<IOvModel> {

	private static final long MAX_NUMBER_OF_VALID_CONFIGS = 1_000_000L;

	private final Set<Map<IConfigurable, Boolean>> validConfigs = new HashSet<>();

	@Override
	public Set<Map<IConfigurable, Boolean>> sample(final IOvModel ovModel) {
		final Set<IConfigurable> configurables = OvModelUtils.getIConfigurable(ovModel).keySet().stream().filter(
				key -> !key.getName().contains(DefaultOvModelTransformationProperties.CONSTRAINT_VARIATION_POINT_PREFIX))
				.collect(Collectors.toSet());
		validConfigs.clear();
		long configcounter = 0;
		long maxConfigs = (long) Math.pow(2, configurables.size());
		for (long i = 0; i < maxConfigs; i++) {
			String bin = Long.toBinaryString(i);

			while (bin.length() < configurables.size()) {
				bin = "0" + bin;
			}
			resetConfiguration(configurables);
			applyConfiguration(configurables, bin);
			// no afterselection necessary, as all possible configurations are tested
			configcounter++;
			if (ovModel.isValid()) {
				Map<IConfigurable, Boolean> config = OvModelUtils.getIConfigurable(ovModel);
				Iterator<Entry<IConfigurable, Boolean>> mapIterator = config.entrySet().iterator();
				while (mapIterator.hasNext()) {
					Entry<IConfigurable, Boolean> entry = mapIterator.next();
					if (entry.getKey().getName()
							.contains(DefaultOvModelTransformationProperties.CONSTRAINT_VARIATION_POINT_PREFIX)) {
						mapIterator.remove();
					}
				}
				validConfigs.add(config);

				// cancel if max number of valid configurations is reached
				if (validConfigs.size() >= MAX_NUMBER_OF_VALID_CONFIGS) {
					System.out.println("Generated " + configcounter
							+ " configurations to reach the maximal number of valid configurations");
					return validConfigs;
				}
			}
		}
		return validConfigs;
	}

	/**
	 * This method applys an binary configuration to the set of configuration.
	 *
	 * @param configuration
	 * @param bin
	 */
	private void applyConfiguration(final Set<IConfigurable> configurations, final String bin) {
		if (configurations.size() != bin.length()) {
			throw new IllegalArgumentException("Lenghts must be of the same size.");
		}
		int i = 0;
		for (final IConfigurable configuration : configurations) {
			configuration.setSelected(bin.charAt(i) == '1');
			i++;
		}
	}

	/**
	 * This method resets an binary configuration to the set of configuration.
	 *
	 * @param configuration
	 */
	private void resetConfiguration(final Set<IConfigurable> configurations) {
		for (final IConfigurable configuration : configurations) {
			configuration.setSelected(false);
		}
	}

	/**
	 * Verifys a a sample OVModel.
	 *
	 * @param ovm     The OVM which should be verified.
	 * @param samples The samples which should be set.
	 * @return
	 */
	@Override
	public boolean verifySampleAs(final IOvModel ovm, final Map<IConfigurable, Boolean> sample) {
		final List<IIdentifiable> samplesAsIdentifiable = new ArrayList<>();
		for (final Entry<IConfigurable, Boolean> entry : sample.entrySet()) {
			if (entry.getValue()) {
				samplesAsIdentifiable.add(new Identifiable() {

					{
						setInternalId(0);
						setName(entry.getKey().getName());
					}
				});
			}
		}
		resetConfiguration(OvModelUtils.getIConfigurable(ovm).keySet());
		setSample(ovm, samplesAsIdentifiable);
		return ovm.isValid();
	}

	/**
	 * This method sets a sample of identifyables to a model.
	 *
	 * @param ovm           the ovModel
	 * @param identifiables
	 */
	private void setSample(final IOvModel ovm, final List<IIdentifiable> identifiables) {
		for (final IIdentifiable identifiable : identifiables) {
			final IOvModelElement element = ovm.getElement(identifiable);
			if (element != null) {
				if (!(element instanceof IConfigurable)) {
					throw new IllegalArgumentException(
							"The argument " + identifiable + " is not of type " + IConfigurable.class + ".");
				}
				((IConfigurable) element).setSelected(true);
			}
		}
		ovm.afterSelection();
	}
}
