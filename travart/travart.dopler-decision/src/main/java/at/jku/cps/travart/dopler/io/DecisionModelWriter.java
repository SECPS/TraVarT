package at.jku.cps.travart.dopler.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import at.jku.cps.travart.core.common.IWriter;
import at.jku.cps.travart.core.common.exc.NotSupportedVariablityTypeException;
import at.jku.cps.travart.dopler.decision.IDecisionModel;
import at.jku.cps.travart.dopler.decision.impl.DMCSVHeader;
import at.jku.cps.travart.dopler.decision.model.IDecision;
import at.jku.cps.travart.dopler.decision.model.IValue;
import at.jku.cps.travart.dopler.decision.model.impl.EnumDecision;
import at.jku.cps.travart.dopler.decision.model.impl.Rule;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DecisionModelWriter implements IWriter<IDecisionModel> {

	private static final char DELIMITER = ';';

	@Override
	public void write(final IDecisionModel decisions, final Path path)
			throws IOException, NotSupportedVariablityTypeException {
		Objects.requireNonNull(decisions);
		Objects.requireNonNull(path);
		try (FileWriter out = new FileWriter(path.toFile(),StandardCharsets.UTF_8);
				CSVPrinter printer = new CSVPrinter(out,
						CSVFormat.EXCEL.withDelimiter(DELIMITER).withHeader(DMCSVHeader.stringArray()))) {
			for (IDecision decision : decisions) {
				Set<IValue> rangeSet = decision.getRange();
				StringBuilder rangeSetBuilder = new StringBuilder();
				if (rangeSet != null) {
					int i = 1;
					for (IValue value : rangeSet) {
						rangeSetBuilder.append(value);
						if (i != rangeSet.size()) {
							rangeSetBuilder.append(" | ");
						}
						i++;
					}
				}

				Set<Rule> rulesSet = decision.getRules();
				StringBuilder rulesSetBuilder = new StringBuilder();
				for (Rule rule : rulesSet) {
					rulesSetBuilder.append(rule);
				}

				String cardinalityString = decision instanceof EnumDecision
						? ((EnumDecision) decision).getCardinality().toString()
						: "";
				printer.printRecord(decision.getId(), decision.getQuestion(), decision.getType(),
						rangeSetBuilder.toString(), cardinalityString, rulesSetBuilder.toString(),
						decision.getVisiblity());
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
}
