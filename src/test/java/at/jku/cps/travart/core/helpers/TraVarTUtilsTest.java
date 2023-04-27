/*******************************************************************************
 * TODO: explanation what the class does
 *  
 * @author Kevin Feichtinger
 *  
 * Copyright 2023 Johannes Kepler University Linz
 * LIT Cyber-Physical Systems Lab
 * All rights reserved
 *******************************************************************************/
//package at.jku.cps.travart.core.helpers;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import de.vill.model.constraint.AndConstraint;
//import de.vill.model.constraint.Constraint;
//import de.vill.model.constraint.LiteralConstraint;
//
//class TraVarTUtilsTest {
//	private Constraint middleConstraint;
//	private Constraint leftConstraint;
//	private Constraint rightConstraint;
//
//	@BeforeEach
//	void setUp() throws Exception {
//		leftConstraint=new LiteralConstraint("Left");
//		rightConstraint= new LiteralConstraint("Right");
//		middleConstraint= new AndConstraint(leftConstraint,rightConstraint);
//	}
//
//	@Test
//	void testGetRightConstraint() {
//		assertEquals(rightConstraint,TraVarTUtils.getRightConstraint(middleConstraint));
//	}
//
//	@Test
//	void testGetLeftConstraint() {
//		assertEquals(leftConstraint,TraVarTUtils.getLeftConstraint(middleConstraint));
//	}
//
//}
