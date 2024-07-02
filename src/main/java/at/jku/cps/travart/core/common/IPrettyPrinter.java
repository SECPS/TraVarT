/*******************************************************************************
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 * https://mozilla.org/MPL/2.0/.
 *
 * Contributors:
 *     @author Jakob Gretenkort
 *
 * The base interface for obtaining statistical data of a variability artifact.
 *
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

import java.util.List;

import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

/**
 * The interface defines a printer to create a human-readable representation of
 * a variability model of type <T>.
 *
 * @param <T> The type of model which should be represented.
 * @author Jakob Gretenkort
 */
public interface IPrettyPrinter<T> {
    /**
     * Lists only the representations supported by this printer in order from
     * most to least helpful.
     *
     * @return The ordered list of supported representations.
     */
    List<REPRESENTATION> representations();

    /**
     * Creates a text-based representation of the given variability model.
     *
     * @param model the variability model to create a representation of.
     * @return a text-based representation of the given model.
     * @throws NotSupportedVariabilityTypeException if the given variability
     *                                              model cannot be serialized
     *                                              to text.
     */
    String toText(T model) throws NotSupportedVariabilityTypeException;

    /**
     * Creates a table-like representation of the given variability model.
     *
     * @param model the variability model to create a representation of.
     * @return a table encoded as a 2D string. Should contain headers as the
     *         first row.
     * @throws NotSupportedVariabilityTypeException if the given variability
     *                                              model cannot be serialized
     *                                              to a table.
     */
    String[][] toTable(T model) throws NotSupportedVariabilityTypeException;

    /**
     * Creates an image representation of the given variability model.
     *
     * @param model the variability model to create a representation of.
     * @return a support vector graphic (SVG) as an XML string.
     * @throws NotSupportedVariabilityTypeException if the given variability
     *                                              model cannot be serialized
     *                                              to an svg.
     */
    String toSvg(T model) throws NotSupportedVariabilityTypeException;

    /**
     * The different types of human-readable representation used by TraVarT.
     * <p>
     * </p>
     * {@code SVG} a scalable vector graphig.
     * <p>
     * </p>
     * {@code TABLE} a tabular representation.
     * <p>
     * </p>
     * {@code TEXT} human-readable text.
     */

    enum REPRESENTATION {
        SVG, TABLE, TEXT
    }
}
