package at.jku.cps.travart.ppr.dsl.common;

import at.sqi.ppr.model.AssemblySequence;

public final class Utility {

	private Utility() {
	}

	public static void printModelStatistics(final AssemblySequence sequence) {
		System.out.println("PPR DSL Model: ");
		System.out.println("#Products: " + sequence.getProducts().size());
		System.out.println("#Processes: " + sequence.getProcesses().size());
		System.out.println("#Constraints: " + sequence.getConstraints().size());
	}
}
