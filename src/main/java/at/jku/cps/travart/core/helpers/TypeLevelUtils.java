/*******************************************************************************
 * TODO: explanation what the class does
 *  
 * @author Kevin Feichtinger
 *  
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
package at.jku.cps.travart.core.helpers;

/**
 * This class contains the utility functions associated with the TYPE-level of
 * UVL.
 *
 * @author Prankur Agarwal
 */
public class TypeLevelUtils {
//    private TypeLevelUtils() {
//    }
//
//    /**
//     * Function to create a NumericFeatureConstraint given that right is a LiteralConstraint.
//     *
//     * @param left     the LHS of the constraint
//     * @param right    the RHS of the constraint
//     * @param operator the operator of the constraint
//     * @return constraint of type NumericFeatureConstraint
//     * @throws NotSupportedVariabilityTypeException
//     */
//    public static NumericFeatureConstraint createNumericFeatureConstraint(final String left, final LiteralConstraint right, final String operator)
//        throws NotSupportedVariabilityTypeException {
//        return createNumericFeatureConstraint(new LiteralConstraint(left), right, operator, Boolean.FALSE);
//    }
//
//    /**
//     * Function to create a NumericFeatureConstraint given that right is a constant.
//     *
//     * @param left     the LHS of the constraint
//     * @param right    the RHS of the constraint
//     * @param operator the operator of the constraint
//     * @return constraint of type NumericFeatureConstraint
//     * @throws NotSupportedVariabilityTypeException
//     */
//    public static NumericFeatureConstraint createNumericFeatureConstraint(final String left, final String right, final String operator)
//        throws NotSupportedVariabilityTypeException {
//        return createNumericFeatureConstraint(new LiteralConstraint(left), new LiteralConstraint(right), operator, Boolean.TRUE);
//    }
//
//    private static NumericFeatureConstraint createNumericFeatureConstraint(final LiteralConstraint left, final LiteralConstraint right,
//                                                                           final String operator,
//                                                                           final Boolean isRightConstant)
//        throws NotSupportedVariabilityTypeException {
//        switch (operator) {
//            case ">":
//                return new NumericFeatureGreaterConstraint(
//                    left,
//                    right,
//                    isRightConstant
//                );
//            case ">=":
//                return new NumericFeatureGreaterEqualsConstraint(
//                    left,
//                    right,
//                    isRightConstant
//                );
//            case "<":
//                return new NumericFeatureLowerConstraint(
//                    left,
//                    right,
//                    isRightConstant
//                );
//            case "<=":
//                return new NumericFeatureLowerEqualsConstraint(
//                    left,
//                    right,
//                    isRightConstant
//                );
//            case "==":
//                return new NumericFeatureEqualsConstraint(
//                    left,
//                    right,
//                    isRightConstant
//                );
//            case "!=":
//            case "<>":
//                return new NumericFeatureNotEqualsConstraint(
//                    left,
//                    right,
//                    isRightConstant
//                );
//            default:
//                throw new NotSupportedVariabilityTypeException("Operator - " + operator + " transformation not supported");
//        }
//    }
}
