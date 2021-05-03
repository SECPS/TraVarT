package at.jku.cps.travart.dopler.io;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import at.jku.cps.travart.core.common.IReader;
import at.jku.cps.travart.core.common.TraVarTUtils;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.common.DecisionModelUtils;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.factory.impl.DecisionModelFactory;
import at.jku.cps.travart.dopler.decision.impl.DMCSVHeader;
import at.jku.cps.travart.dopler.decision.impl.DecisionModel;
import at.jku.cps.travart.dopler.decision.model.ADecision;
import at.jku.cps.travart.dopler.decision.model.ADecision.DecisionType;
import at.jku.cps.travart.dopler.decision.model.ICondition;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Cardinality;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Range;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;
import at.jku.cps.travart.dopler.decision.parser.ConditionParser;
import at.jku.cps.travart.dopler.decision.parser.RulesParser;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecisionModelReader implements IReader<IDecisionModel> {

	private static final char DELIMITER = ';';

	private static final String CARDINALITY_NOT_SUPPORTED_ERROR = "Cardinality %s not supported for decision of type %s";

	private final DecisionModelFactory factory;

	public DecisionModelReader() {
		factory = DecisionModelFactory.getInstance();
	}

	@Override
	public IDecisionModel read(final Path path) throws IOException, NotSupportedVariablityTypeException {
		Objects.requireNonNull(path);
		DecisionModel dm = factory.create();
		dm.setName(path.getFileName().toString());
		dm.setSourceFile(path.toAbsolutePath().toString());
		dm.setAddPrefix(false);

		try (Reader in = new FileReader(path.toFile(),StandardCharsets.UTF_8)) {
			Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(DELIMITER).withHeader(DMCSVHeader.stringArray())
					.withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) {
				String id = record.get(DMCSVHeader.ID).trim();
				String typeString = record.get(DMCSVHeader.TYPE).trim();
				ADecision decision = null;
				if (DecisionType.BOOLEAN.equalString(typeString)) {
					decision = factory.createBooleanDecision(id);
				} else if (DecisionType.ENUM.equalString(typeString)) {
					decision = factory.createEnumDecision(id);
				} else if (DecisionType.DOUBLE.equalString(typeString)) {
					decision = factory.createNumberDecision(id);
				} else if (DecisionType.STRING.equalString(typeString)) {
					decision = factory.createStringDecision(id);
				} else {
					throw new NotSupportedVariablityTypeException(typeString);
				}

				assert decision != null;
				decision.setQuestion(record.get(DMCSVHeader.QUESTION).trim());

				String range = record.get(DMCSVHeader.RANGE);
				if (!range.isBlank()) {
					if (DecisionModelUtils.isNumberDecision(decision)) {
						String[] ranges = TraVarTUtils.splitString(range, "-");
						Range<Double> valueRange = factory.createNumberValueRange(ranges);
						decision.setRange(valueRange);
					} else {
						String[] options = TraVarTUtils.splitString(range, "\\|");
						Range<String> valueOptions = factory.createEnumValueOptions(options);
						decision.setRange(valueOptions);
					}
				}

				String cardinality = record.get(DMCSVHeader.CARDINALITY).trim();
				if (!cardinality.isEmpty()) {
					String[] values = TraVarTUtils.splitString(cardinality, ":");
					if (!(decision instanceof EnumDecision) || !(values.length == 2)) {
						throw new NotSupportedVariablityTypeException(String.format(CARDINALITY_NOT_SUPPORTED_ERROR,
								cardinality, decision.getClass().getCanonicalName()));
					}
					Cardinality c = factory.createCardinality(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
					((EnumDecision) decision).setCardinality(c);
				}
				dm.add(decision);
			}
		}

		try (Reader secondIn = new FileReader(path.toFile(),StandardCharsets.UTF_8)) {
			Iterable<CSVRecord> secondParse = CSVFormat.EXCEL.withDelimiter(DELIMITER)
					.withHeader(DMCSVHeader.stringArray()).withFirstRecordAsHeader().parse(secondIn);
			ConditionParser vParser = new ConditionParser(dm);
			RulesParser rParser = new RulesParser(dm);

			for (CSVRecord record : secondParse) {
				String id = record.get(DMCSVHeader.ID);
				IDecision decision = dm.find(id);

				String csvRules = record.get(DMCSVHeader.RULES);
				if (!csvRules.isEmpty()) {
					// TODO: Find better way to spit the string of rules (decision names may have
					// "if" as part of their names)
					String[] CSVruleSplit = TraVarTUtils.splitString(csvRules, "if");
					Set<Rule> rules = rParser.parse(decision, CSVruleSplit);
					decision.addRules(rules);
				}

				String visiblity = record.get(DMCSVHeader.VISIBLITY);
				ICondition v = vParser.parse(visiblity);
				decision.setVisibility(v);
			}
		}
		return dm;
	}
}
