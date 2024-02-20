/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Dario Romano
 *
 * provides constants for use in the UVLVerifier class
 *
 * Copyright 2024 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.verify;

public enum LogicOperator {
	AND("&", "&"), OR("|", "|"), EQUALS("<=>", "<=>"), IMPLIES("=>", "=>"), NOT("~", "!");

	private String uvl;
	private String ng;

	LogicOperator(String uvl, String ng) {
		this.uvl = uvl;
		this.ng = ng;
	}

	public String uvl() {
		return uvl;

	}

	public String ng() {
		return ng;
	}

	@Override
	public String toString() {
		return this.uvl;
	}

}
