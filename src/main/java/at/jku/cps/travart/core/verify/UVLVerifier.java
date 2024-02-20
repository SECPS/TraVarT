/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Dario Romano
 *
 * Implements various utilities to verify transformation/optimization results
 *
 * Copyright 2024 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.verify;

import static at.jku.cps.travart.core.verify.LogicOperator.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.FormulaParser;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;

import de.vill.model.Feature;
import de.vill.model.FeatureModel;
import de.vill.model.constraint.Constraint;

public class UVLVerifier {

	/**
	 * forbid public constructor because this is purely a static utility class
	 */
	private UVLVerifier() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Checks two UVL models for equality. This should guarantee the same
	 * configuration space. Both models need to have an identical feature-set.
	 * 
	 * @param fm1 The first UVL model
	 * @param fm2 The second UVL model to compare it with
	 * @return true if models have same config space, false if not
	 */
	public static boolean equals(FeatureModel fm1, FeatureModel fm2) {
		FormulaFactory ff = new FormulaFactory();
		Formula formulaModel1 = getModelsAsFormula(ff, fm1);
		Formula formulaModel2 = getModelsAsFormula(ff, fm2);

		return formulaModel1.isEquivalentTo(formulaModel2);
	}

	/**
	 * Transforms a number of UVL constraints into a set of logicNG formula objects.
	 * 
	 * @param ff          the formulafactory used to create the formulas (needs to
	 *                    be identical for all formulas that need to be compared
	 *                    with the result of this method)
	 * @param constraints the uvl constraint objects to be translated.
	 * @return a set of formulas to be processed with logicNG
	 */
	public static Set<Formula> uvlConstraintstoFormulas(FormulaFactory ff, Collection<Constraint> constraints) {
		return constraints.stream()
				.map(c -> c.toString(false, Strings.EMPTY))
				.map(c -> c.replace(NOT.uvl(), NOT.ng()))
				.map(c -> UVLVerifier.parseUVL(ff, c))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	/**
	 * Transforms the model into a logic formula to be processes with logicNG
	 * 
	 * @param ff the formulafactory used to create the formulas (needs to be
	 *           identical for all formulas that need to be compared with the result
	 *           of this method)
	 * @param fm the uvl model from which to get the constraints
	 * @return a logic formula that represents the uvl model
	 */
	public static Formula getModelsAsFormula(FormulaFactory ff, FeatureModel fm) {
		Formula formula = ff.and(uvlConstraintstoFormulas(ff, fm.getConstraints()));
		return ff.and(formula, getFeaturesBasic(ff, fm));
	}

	/**
	 * parses a logicNG formula from an UVL-constraint represented by string s.
	 * Requires a pre-processing step where all negation signs "!" from uvl have
	 * been replaces by "~" needed by the parser.
	 * 
	 * @param ff the formulafactory used to create the formulas (needs to be
	 *           identical for all formulas that need to be compared with the result
	 *           of this method)
	 * @param s  the pre-processes constraint-string
	 * @return A formula object representing the constraints
	 */
	private static Formula parseUVL(FormulaFactory ff, String s) {
		final FormulaParser parser = new PropositionalParser(ff);
		try {
			return parser.parse(s);
		} catch (ParserException e) {
			return null;
		}
	}

	/**
	 * @deprecated Gets all features within the model and creates a formula like (A
	 *             | !A) and (B | !B) ... this is only used to get all literals
	 *             inside a big formula to check equality of to formal models. Since
	 *             the equality checker needs all literals to be present to generate
	 *             a valid result. This does not actually represent the logic of the
	 *             feature-tree and is to be replaced by a method that actually
	 *             processes that logic.
	 * @param ff the formulafactory used to create the formulas (needs to be
	 *           identical for all formulas that need to be compared with the result
	 *           of this method)
	 * @param fm the uvl model from which to get the individual features.
	 * @return A formula representing all features included in the featuremodel
	 */
	public static Formula getFeaturesBasic(FormulaFactory ff, FeatureModel fm) {
		List<Formula> signs = fm.getFeatureMap()
				.values()
				.stream()
				.map(Feature::getFeatureName)
				.map(n -> ff.or(ff.literal(n, true), ff.literal(n, false)))
				.collect(Collectors.toList());
		return ff.and(signs);
	}

	/**
	 * Generates a logic formula that represents the feature-tree of a UVL model
	 * 
	 * @param ff the formulafactory used to create the formulas (needs to be
	 *           identical for all formulas that need to be compared with the result
	 *           of this method)
	 * @param fm the uvl model from which to generate the formula
	 * @return
	 */
	private static Formula getFormulaFromUVLTree(FormulaFactory ff, FeatureModel fm) {
		Feature root = fm.getRootFeature();
//		TODO recursively build a formula that represents the feature tree
//		mabe some future work I guess
		return null;
	}

}
