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
package at.jku.cps.travart.core.basic;

import java.util.List;

import at.jku.cps.travart.core.common.IPrettyPrinter;
import at.jku.cps.travart.core.common.ISerializer;
import at.jku.cps.travart.core.exception.NotSupportedVariabilityTypeException;

/**
 * A pretty printer that supports a text-based representation based on the
 * serialization provided by an
 * {@link at.jku.cps.travart.core.common.ISerializer}.
 */
public class DefaultPrettyPrinter<T> implements IPrettyPrinter<T> {
    private final ISerializer<T> serializer;

    /**
     * Creates a default pretty printer.
     *
     * @param serializer the serializer which will provide the text-based
     * representation of this printer. It must use a text-based and human
     * readable format.
     */
    public DefaultPrettyPrinter(ISerializer<T> serializer) {
        if(!serializer.getFormat().isText() && serializer.getFormat().isHumanReadable()) {

        }
        this.serializer = serializer;
    }

    @Override
    public List<REPRESENTATION> representations() {
        return List.of(REPRESENTATION.TEXT);
    }

    @Override
    public String toText(T model) throws NotSupportedVariabilityTypeException {
        return serializer.serialize(model);
    }

    @Override
    public String[][] toTable(T model) throws NotSupportedVariabilityTypeException {
        throw new NotSupportedVariabilityTypeException("The default pretty printer does not support tabular representation for any model.");
    }

    @Override
    public String toSvg(T model) throws NotSupportedVariabilityTypeException {
        throw new NotSupportedVariabilityTypeException("The default pretty printer does not support svg representation for any model.");
    }

}
