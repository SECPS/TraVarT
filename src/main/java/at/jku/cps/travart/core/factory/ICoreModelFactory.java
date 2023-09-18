/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * Represents the base interface for the core model (pivot model) of TraVarT.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.factory;

import at.jku.cps.travart.core.common.IFactory;
import de.vill.model.Feature;
import de.vill.model.FeatureModel;
import de.vill.model.constraint.AndConstraint;
import de.vill.model.constraint.Constraint;
import de.vill.model.constraint.EquivalenceConstraint;
import de.vill.model.constraint.ImplicationConstraint;
import de.vill.model.constraint.LiteralConstraint;
import de.vill.model.constraint.NotConstraint;
import de.vill.model.constraint.OrConstraint;
import de.vill.model.constraint.ParenthesisConstraint;

public interface ICoreModelFactory extends IFactory<FeatureModel> {

	Feature createFeature(String id);

	ImplicationConstraint createImplicationConstraint(Constraint left, Constraint right);

	EquivalenceConstraint createEquivalenceConstraint(Constraint left, Constraint right);

	AndConstraint createAndConstraint(Constraint left, Constraint right);

	OrConstraint createOrConstraint(Constraint left, Constraint right);

	NotConstraint createNotConstraint(Constraint constraint);

	ParenthesisConstraint createParenthesisConstraint(Constraint constraint);

	LiteralConstraint createLiteralConstraint(String id);
}
