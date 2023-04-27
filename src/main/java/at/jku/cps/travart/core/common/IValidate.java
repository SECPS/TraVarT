/*******************************************************************************
 * TODO: explanation what the class does
 *  
 * @author Kevin Feichtinger
 *  
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.common;

/**
 * This is an interface defines a method in order to validate the variability
 * representation implementing it.
 *
 * @author Johann Stoebich
 */
public interface IValidate {

    /**
     * This method returns whether an configuration is valid or not.
     *
     * @return true whenever the current configuration is valid.
     */
    boolean isValid();
}
