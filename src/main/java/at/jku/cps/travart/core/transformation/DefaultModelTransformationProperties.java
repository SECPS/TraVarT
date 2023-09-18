/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Kevin Feichtinger
 *
 * A set of constants used in a transformation.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.transformation;

public final class DefaultModelTransformationProperties {

	/**
	 * The default model name, if no variability model name is given.
	 */
	public static final String ARTIFICIAL_MODEL_NAME = "VAR_MODEL";

	public static final String ABSTRACT_ATTRIBUTE = "abstract";

	public static final String EXTENDED_FEATURE_MODEL = "extended__";

	public static final String HIDDEN_ATTRIBUTE = "hidden";

	public static final String INSTANCE_CARDINALTIY = "InstanceNum";
	public static final String INSTANCE_CARDINALTIY_DELIMITER = "..";

	private DefaultModelTransformationProperties() {
	}
}
