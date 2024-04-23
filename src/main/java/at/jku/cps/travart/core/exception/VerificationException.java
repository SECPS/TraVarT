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
package at.jku.cps.travart.core.exception;

import org.logicng.formulas.Formula;

public class VerificationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates a VerificationException with the given message.
	 *
	 * @param message a string.
	 */
	private final transient Formula fm1;
	private final transient Formula fm2;
	private final transient String unsatCore;
	public VerificationException(final String message, Formula fm1, Formula fm2,String unsatCore) {
		super(message);
		this.unsatCore=unsatCore;
		this.fm1=fm1;
		this.fm2=fm2;
	}
	
	public String getUnsatCore() {
		return unsatCore;
	}
	
	public Formula getFormulaOne() {
		return fm1;
	}
	
	public Formula getFormulaTwo() {
		return fm2;
	}

}
