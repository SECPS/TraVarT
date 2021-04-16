package at.jku.cps.travart.dopler.decision.factory;

import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.impl.BooleanDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.NumberDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.StringDecision;
import de.ovgu.featureide.fm.core.base.IFactory;

public interface IDecisionModelFactory extends IFactory<DecisionModel> {

	BooleanDecision createBooleanDecision(String id);

	EnumDecision createEnumDecision(String id);

	NumberDecision createNumberDecision(String id);

	StringDecision createStringDecision(String id);

	Range<String> createEnumValueOptions(String[] options);

	Range<Double> createNumberValueRange(String[] ranges);

	Cardinality createCardinality(int min, int max);
}
