package at.jku.cps.travart.core.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.prop4j.And;
import org.prop4j.Implies;
import org.prop4j.Literal;
import org.prop4j.Node;
import org.prop4j.Not;
import org.prop4j.Or;

import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;

/**
 * This class implements utility methods to handle the constraint nodes of
 * FeatureIDE in CNF form.
 *
 * @author kevinfeichtinger
 *
 */
public final class Prop4JUtils {

	public static int countLiterals(final Node node) {
		if (isLiteral(node)) {
			return 1;
		}
		int count = 0;
		for (Node child : node.getChildren()) {
			if (isLiteral(child)) {
				count++;
			}
		}
		return count;
	}

	public static int countNegativeLiterals(final Node node) {
		if (isNegativeLiteral(node)) {
			return 1;
		}
		int count = 0;
		if (!isLiteral(node)) {
			for (Node child : node.getChildren()) {
				if (isNegativeLiteral(child)) {
					count++;
				}
			}
		}
		return count;
	}

	public static int countPositiveLiterals(final Node node) {
		if (isPositiveLiteral(node)) {
			return 1;
		}
		int count = 0;
		if (!isLiteral(node)) {
			for (Node child : node.getChildren()) {
				if (isPositiveLiteral(child)) {
					count++;
				}
			}
		}
		return count;
	}

	public static And createAnd(final Node left, final Node right) {
		return new And(left, right);
	}

	public static Implies createImplies(final Node condition, final Node action) {
		return new Implies(condition, action);
	}

	public static Literal createLiteral(final Object operand) {
		if (operand instanceof IFeature) {
			return new Literal(FeatureUtils.getName((IFeature) operand));
		}
		return new Literal(operand);
	}

	public static Node createNot(final Node node) {
		return new Not(node);
	}

	public static Or createOr(final Node left, final Node right) {
		return new Or(left, right);
	}

	public static Node getFirstNegativeLiteral(final Node node) {
		if (isNegativeLiteral(node)) {
			return node;
		}
		for (Node child : node.getChildren()) {
			if (isNegativeLiteral(child)) {
				return child;
			}
		}
		return null;
	}

	public static Node getFirstPositiveLiteral(final Node node) {
		if (isPositiveLiteral(node)) {
			return node;
		}
		for (Node child : node.getChildren()) {
			if (isPositiveLiteral(child)) {
				return child;
			}
		}
		return null;
	}

	public static Node getLeftNode(final Node node) {
		return node.getChildren()[0];
	}

	public static String getLiteralName(final Literal literal) {
		return literal.getContainedFeatures().get(0);
	}

	public static Set<Node> getLiterals(final Node node) {
		if (isLiteral(node)) {
			return Collections.singleton(node);
		}
		Set<Node> literals = new HashSet<>();
		for (Node child : node.getChildren()) {
			if (isLiteral(child)) {
				literals.add(child);
			}
		}
		return literals;
	}

	public static Set<Node> getNegativeLiterals(final Node node) {
		if (isNegativeLiteral(node)) {
			return Collections.singleton(node);
		}
		Set<Node> literals = new HashSet<>();
		if (!isLiteral(node)) {
			for (Node child : node.getChildren()) {
				if (isNegativeLiteral(child)) {
					literals.add(child);
				}
			}
		}
		return literals;
	}

	public static Set<Node> getPositiveLiterals(final Node node) {
		if (isPositiveLiteral(node)) {
			return Collections.singleton(node);
		}
		Set<Node> literals = new HashSet<>();
		if (!isLiteral(node)) {
			for (Node child : node.getChildren()) {
				if (isPositiveLiteral(child)) {
					literals.add(child);
				}
			}
		}
		return literals;
	}

	public static Node getRightNode(final Node node) {
		return node.getChildren()[1];
	}

	public static boolean hasNegativeLiteral(final Node node) {
		if (isLiteral(node)) {
			return isNegativeLiteral(node);
		}
		for (Node child : node.getChildren()) {
			if (isNegativeLiteral(child)) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasPositiveLiteral(final Node node) {
		if (isLiteral(node)) {
			return isPositiveLiteral(node);
		}
		for (Node child : node.getChildren()) {
			if (isPositiveLiteral(child)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAnd(final Node node) {
		return node instanceof And;
	}

	public static boolean isComplexNode(final Node node) {
		if (isLiteral(node)) {
			return false;
		}
		boolean isComplex = false;
		for (Node child : node.getChildren()) {
			isComplex = isComplex || !isLiteral(child);
		}
		return isComplex;
	}

	public static boolean isExcludes(final Node node) {
		if (!isOr(node) || node.getChildren().length != 2) {
			return false;
		}
		Or or = (Or) node;
		Node left = getLeftNode(or);
		Node right = getRightNode(or);
		// Not(A) or Not(B) --> excludes
		return isNegativeLiteral(left) && isNegativeLiteral(right);
	}

	public static boolean isNot(final Node node) {
		return node instanceof Not;
	}

	public static boolean isLiteral(final Node node) {
		return node instanceof Literal;
	}

	public static boolean isNegativeLiteral(final Node node) {
		return isLiteral(node) && !((Literal) node).positive;
	}

	public static boolean isOr(final Node node) {
		return node instanceof Or;
	}

	public static boolean isPositiveLiteral(final Node node) {
		return isLiteral(node) && ((Literal) node).positive;
	}

	public static boolean isRequires(final Node node) {
		if (!isOr(node) || node.getChildren().length != 2) {
			return false;
		}
		Or or = (Or) node;
		Node left = getLeftNode(or);
		Node right = getRightNode(or);
		// Not(A) or B || A or Not(B) --> Both are implies constraints
		return isNegativeLiteral(left) && isPositiveLiteral(right)
				|| isPositiveLiteral(left) && isNegativeLiteral(right);
	}

	private Prop4JUtils() {

	}

	public static Node consumeToOrGroup(final List<Literal> literals, final boolean negative) {
		if (literals.isEmpty()) {
			throw new IllegalArgumentException(new IllegalArgumentException("Set of literals is empty!"));
		}
		if (literals.size() == 1) {
			if (negative) {
				literals.get(0).positive = false;
			}
			return literals.get(0);
		}
		// take the first two and create the first ABinaryCondition
		Literal first = literals.remove(0);
		Literal second = literals.remove(0);
		if (negative) {
			first.positive = false;
			second.positive = false;
		}
		Or condition = createOr(first, second);
		while (!literals.isEmpty()) {
			Literal next = literals.remove(0);
			if (negative) {
				next.positive = false;
			}
			condition = createOr(next, condition);
		}
		return condition;
	}

	public static Node consumeToAndGroup(final List<Literal> literals, final boolean negative) {
		if (literals.isEmpty()) {
			throw new IllegalArgumentException(new IllegalArgumentException("Set of literals is empty!"));
		}
		if (literals.size() == 1) {
			if (negative) {
				literals.get(0).positive = false;
			}
			return literals.get(0);
		}
		// take the first two and create the first ABinaryCondition
		Literal first = literals.remove(0);
		Literal second = literals.remove(0);
		if (negative) {
			first.positive = false;
			second.positive = false;
		}
		And condition = createAnd(first, second);
		while (!literals.isEmpty()) {
			Literal next = literals.remove(0);
			if (negative) {
				next.positive = false;
			}
			condition = createAnd(next, condition);
		}
		return condition;
	}
}
