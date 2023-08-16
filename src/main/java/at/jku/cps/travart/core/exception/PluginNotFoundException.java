/*******************************************************************************
 * An exception indicating something went wrong with the plugin management of TraVarT.
 *
 * @author Kevin Feichtinger
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.exception;

@SuppressWarnings("serial")
public class PluginNotFoundException extends Exception {

	/**
	 * Creates a new exception with the given message.
	 *
	 * @param message a string.
	 */
	public PluginNotFoundException(final String message) {
		super(message);
	}

	/**
	 * Creates a new exception with the given sub exception.
	 *
	 * @param e an exception.
	 */
	public PluginNotFoundException(final Exception e) {
		super(e);
	}
}
