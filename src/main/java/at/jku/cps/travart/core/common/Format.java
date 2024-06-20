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

import java.util.Objects;

/**
 * This class represents a data storage or serialization format in which a
 * variability model might be represented.
 *
 * @author Jakob Gretenkort
 */
public class Format {
    private final String name;
    private final String extension;
    private final boolean isText;
    private final boolean isHumanReadable;

    /**
     * Create a format.
     *
     * @param name The name of the format.
     * @param extension The file extension used when writing models to
     *                  disk in this format.
     * @param isText Whether the format is text-based.
     * @param isHumanReadable Whether the format is human readable.
     */
    public Format(String name, String extension, boolean isText, boolean isHumanReadable) {
        this.name = name;
        this.extension = extension;
        this.isText = isText;
        this.isHumanReadable = isHumanReadable;
    }

    /**
     * The name of the format.
     * @return the name of the format.
     */
    public String name() {
        return name;
    }

    /**
     * The file extension used when writing models to disk in this format.
     * @return the file extension used when writing models to disk in this format.
     */
    public String extension() {
        return extension;
    }

    /**
     * Whether the format is text-based.
     * @return whether the format is text-based.
     */
    public boolean isText() {
        return isText;
    }

    /**
     * Whether the format is human readable.
     * @return whether the format is human readable.
     */
    public boolean isHumanReadable() {
        return isHumanReadable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, extension, isText, isHumanReadable);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Format)) {
            return false;
        } else {
            Format other = (Format) obj;
            return Objects.equals(this.name, other.name) &&
                Objects.equals(this.extension, other.extension) &&
                Objects.equals(this.isText, other.isText) &&
                Objects.equals(this.isHumanReadable, other.isHumanReadable);
        }
    }

    @Override
    public String toString() {
        return "Format [name=" + this.name +
            "extension=" + this.extension +
            "isText=" + this.isText +
            "isHumanReadable=" + this.isHumanReadable + "]";
    }
}
