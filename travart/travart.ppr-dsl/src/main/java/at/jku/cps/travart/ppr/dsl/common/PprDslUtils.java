package at.jku.cps.travart.ppr.dsl.common;

import java.util.logging.Level;
import java.util.logging.Logger;

import at.sqi.ppr.model.AssemblySequence;

public final class PprDslUtils {

	private PprDslUtils() {
	}

	public static void logModelStatistics(final Logger logger, final AssemblySequence seq) {
		logger.log(Level.INFO, "PPR DSL Model: ");
		logger.log(Level.INFO,
				String.format("#Products: %s", seq.getProducts() != null ? seq.getProducts().size() : 0));
		logger.log(Level.INFO,
				String.format("#Processes: %s", seq.getProcesses() != null ? seq.getProcesses().size() : 0));
		logger.log(Level.INFO,
				String.format("#Constraints: %s", seq.getConstraints() != null ? seq.getConstraints().size() : 0));
	}
}
