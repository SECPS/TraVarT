//package at.jku.cps.travart.core.common;
//
//import de.ovgu.featureide.fm.core.base.FeatureUtils;
//import de.ovgu.featureide.fm.core.base.IFeature;
//import de.vill.model.Feature;
//import de.vill.model.constraint.AndConstraint;
//import de.vill.model.constraint.ImplicationConstraint;
//import de.vill.model.constraint.LiteralConstraint;
//import de.vill.model.constraint.NotConstraint;
//import de.vill.model.constraint.OrConstraint;
//import org.prop4j.And;
//import org.prop4j.Implies;
//import org.prop4j.Literal;
//import org.prop4j.Node;
//import org.prop4j.Not;
//import org.prop4j.Or;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * This class implements utility methods to handle the constraint nodes of
// * FeatureIDE in CNF form.
// *
// * @author kevinfeichtinger
// */
//public final class Prop4jUtils {
//    private Prop4jUtils() {
//
//    }
//
//    public static boolean isAbstract(final Feature feature) {
//        if (feature.getAttributes().get("abstract") == null) {
//            return false;
//        }
//
//        return Boolean.parseBoolean(feature.getAttributes().get("abstract").getValue().toString());
//    }
//
//    public static int featureChildrenCount(final Feature feature) {
//        return feature.getChildren().size();
//    }
//
//    public static Feature retrieveLeftOperand(final Feature feature) {
//        Objects.checkIndex(0, feature.getChildren().size());
//        Objects.checkIndex(0, feature.getChildren().get(0).getFeatures().size());
//        return feature.getChildren().get(0).getFeatures().get(0);
//    }
//
//    public static String retrieveLeftOperandName(final Feature feature) {
//        final Feature left = retrieveLeftOperand(feature);
//        // todo: check
//        return left.getFeatureName();
//    }
//
//    public static Feature retrieveRightOperand(final Feature feature) {
//        Objects.checkIndex(0, feature.getChildren().size());
//        Objects.checkIndex(1, feature.getChildren().get(0).getFeatures().size());
//        return feature.getChildren().get(0).getFeatures().get(1);
//    }
//
//    public static String retrieveRightOperandName(final Feature feature) {
//        final Feature right = retrieveRightOperand(feature);
//        // todo: check
//        return right.getFeatureName();
//    }
//
//    // todo: ask if Literal == LiteralConstraint or just Feature
//    public static int countLiterals(final Node node) {
//        if (isLiteral(node)) {
//            return 1;
//        }
//        int count = 0;
//        for (final Node child : node.getChildren()) {
//            if (isLiteral(child)) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    public static int countNegativeLiterals(final Node node) {
//        if (isNegativeLiteral(node)) {
//            return 1;
//        }
//        int count = 0;
//        if (!isLiteral(node)) {
//            for (final Node child : node.getChildren()) {
//                if (isNegativeLiteral(child)) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    public static int countPositiveLiterals(final Node node) {
//        if (isPositiveLiteral(node)) {
//            return 1;
//        }
//        int count = 0;
//        if (!isLiteral(node)) {
//            for (final Node child : node.getChildren()) {
//                if (isPositiveLiteral(child)) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    // todo: check whether feature or constraints as parameters
//    public static AndConstraint createAnd(final Feature left, final Feature right) {
//        return new AndConstraint(new LiteralConstraint(left.getFeatureName()), new LiteralConstraint(right.getFeatureName()));
//    }
//
//    // todo: check whether feature or constraints as parameters
//    public static ImplicationConstraint createImplies(final Feature condition, final Feature action) {
//        return new ImplicationConstraint(new LiteralConstraint(condition.getFeatureName()), new LiteralConstraint(action.getFeatureName()));
//    }
//
//    // todo: don't know what to do here
//    public static Literal createLiteral(final Object operand) {
//        if (operand instanceof IFeature) {
//            return new Literal(FeatureUtils.getName((IFeature) operand));
//        }
//        return new Literal(operand);
//    }
//
//    // todo: check whether feature or constraints as parameters
//    public static NotConstraint createNot(final Feature feature) {
//        return new NotConstraint(new LiteralConstraint(feature.getFeatureName()));
//    }
//
//    // todo: check whether feature or constraints as parameters
//    public static OrConstraint createOr(final Feature left, final Feature right) {
//        return new OrConstraint(new LiteralConstraint(left.getFeatureName()), new LiteralConstraint(right.getFeatureName()));
//    }
//
//    public static Node getFirstNegativeLiteral(final Node node) {
//        if (isNegativeLiteral(node)) {
//            return node;
//        }
//        for (final Node child : node.getChildren()) {
//            if (isNegativeLiteral(child)) {
//                return child;
//            }
//        }
//        return null;
//    }
//
//    public static Node getFirstPositiveLiteral(final Node node) {
//        if (isPositiveLiteral(node)) {
//            return node;
//        }
//        for (final Node child : node.getChildren()) {
//            if (isPositiveLiteral(child)) {
//                return child;
//            }
//        }
//        return null;
//    }
//
//    public static Node getLeftNode(final Node node) {
//        return node.getChildren()[0];
//    }
//
//    public static Literal getLeftLiteral(final Node node) {
//        return isLiteral(getLeftNode(node)) ? (Literal) getLeftNode(node) : null;
//    }
//
//    public static String getLiteralName(final Literal literal) {
//        return literal.getContainedFeatures().get(0);
//    }
//
//    public static Set<Node> getLiterals(final Node node) {
//        if (isLiteral(node)) {
//            return Collections.singleton(node);
//        }
//        final Set<Node> literals = new HashSet<>();
//        for (final Node child : node.getChildren()) {
//            if (isLiteral(child)) {
//                literals.add(child);
//            }
//        }
//        return literals;
//    }
//
//    public static Set<Node> getNegativeLiterals(final Node node) {
//        if (isNegativeLiteral(node)) {
//            return Collections.singleton(node);
//        }
//        final Set<Node> literals = new HashSet<>();
//        if (!isLiteral(node)) {
//            for (final Node child : node.getChildren()) {
//                if (isNegativeLiteral(child)) {
//                    literals.add(child);
//                }
//            }
//        }
//        return literals;
//    }
//
//    public static Set<Node> getPositiveLiterals(final Node node) {
//        if (isPositiveLiteral(node)) {
//            return Collections.singleton(node);
//        }
//        final Set<Node> literals = new HashSet<>();
//        if (!isLiteral(node)) {
//            for (final Node child : node.getChildren()) {
//                if (isPositiveLiteral(child)) {
//                    literals.add(child);
//                }
//            }
//        }
//        return literals;
//    }
//
//    public static Node getRightNode(final Node node) {
//        return node.getChildren()[1];
//    }
//
//    public static Literal getRightLiteral(final Node node) {
//        return isLiteral(getRightNode(node)) ? (Literal) getRightNode(node) : null;
//    }
//
//    public static boolean hasNegativeLiteral(final Node node) {
//        if (isLiteral(node)) {
//            return isNegativeLiteral(node);
//        }
//        for (final Node child : node.getChildren()) {
//            if (isNegativeLiteral(child)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean hasPositiveLiteral(final Node node) {
//        if (isLiteral(node)) {
//            return isPositiveLiteral(node);
//        }
//        for (final Node child : node.getChildren()) {
//            if (isPositiveLiteral(child)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean isAnd(final Node node) {
//        return node instanceof And;
//    }
//
//    public static boolean isImplies(final Node node) {
//        return node instanceof Implies;
//    }
//
//    public static boolean isComplexNode(final Node node) {
//        if (isLiteral(node)) {
//            return false;
//        }
//        boolean isComplex = false;
//        for (final Node child : node.getChildren()) {
//            isComplex = isComplex || !isLiteral(child);
//        }
//        return isComplex;
//    }
//
//    public static boolean isExcludes(final Node node) {
//        if (!isOr(node) || node.getChildren().length != 2) {
//            return false;
//        }
//        final Or or = (Or) node;
//        final Node left = getLeftNode(or);
//        final Node right = getRightNode(or);
//        // Not(A) or Not(B) --> excludes
//        return isNegativeLiteral(left) && isNegativeLiteral(right);
//    }
//
//    public static boolean isNot(final Node node) {
//        return node instanceof Not;
//    }
//
//    public static boolean isLiteral(final Node node) {
//        return node instanceof Literal;
//    }
//
//    public static boolean isNegativeLiteral(final Node node) {
//        return isLiteral(node) && !((Literal) node).positive;
//    }
//
//    public static boolean isOr(final Node node) {
//        return node instanceof Or;
//    }
//
//    public static boolean isPositiveLiteral(final Node node) {
//        return isLiteral(node) && ((Literal) node).positive;
//    }
//
//    public static boolean isSingleFeatureRequires(final Node node) {
//        if (!isOr(node) || node.getChildren().length != 1) {
//            return false;
//        }
//        final Or or = (Or) node;
//        final Node left = getLeftNode(or);
//        return isPositiveLiteral(left);
//    }
//
//    public static boolean isSingleFeatureExcludes(final Node node) {
//        if (!isOr(node) || node.getChildren().length != 1) {
//            return false;
//        }
//        final Or or = (Or) node;
//        final Node left = getLeftNode(or);
//        return isNegativeLiteral(left);
//    }
//
//    public static boolean isRequires(final Node node) {
//        if (!isOr(node) || node.getChildren().length != 2) {
//            return false;
//        }
//        final Or or = (Or) node;
//        final Node left = getLeftNode(or);
//        final Node right = getRightNode(or);
//        // Not(A) or B || A or Not(B) --> Both are implies constraints
//        return isNegativeLiteral(left) && isPositiveLiteral(right)
//                || isPositiveLiteral(left) && isNegativeLiteral(right);
//    }
//
//    public static Node consumeToOrGroup(final List<Literal> literals, final boolean negative) {
//        if (literals.isEmpty()) {
//            throw new IllegalArgumentException(new IllegalArgumentException("Set of literals is empty!"));
//        }
//        if (literals.size() == 1) {
//            if (negative) {
//                literals.get(0).positive = false;
//            }
//            return literals.get(0);
//        }
//        // take the first two and create the first ABinaryCondition
//        final Literal first = literals.remove(0);
//        final Literal second = literals.remove(0);
//        if (negative) {
//            first.positive = false;
//            second.positive = false;
//        }
//        Or condition = createOr(first, second);
//        while (!literals.isEmpty()) {
//            final Literal next = literals.remove(0);
//            if (negative) {
//                next.positive = false;
//            }
//            condition = createOr(next, condition);
//        }
//        return condition;
//    }
//
//    public static Node consumeToAndGroup(final List<Literal> literals, final boolean negative) {
//        if (literals.isEmpty()) {
//            throw new IllegalArgumentException(new IllegalArgumentException("Set of literals is empty!"));
//        }
//        if (literals.size() == 1) {
//            if (negative) {
//                literals.get(0).positive = false;
//            }
//            return literals.get(0);
//        }
//        // take the first two and create the first ABinaryCondition
//        final Literal first = literals.remove(0);
//        final Literal second = literals.remove(0);
//        if (negative) {
//            first.positive = false;
//            second.positive = false;
//        }
//        And condition = createAnd(first, second);
//        while (!literals.isEmpty()) {
//            final Literal next = literals.remove(0);
//            if (negative) {
//                next.positive = false;
//            }
//            condition = createAnd(next, condition);
//        }
//        return condition;
//    }
//}
