package at.jku.cps.travart.dopler.decision.model;

public class AActionTest {

//	@Test
//	public void nullVariableTest() {
//		ThrowingRunnable runner = () -> {
//			@SuppressWarnings("unused")
//			IAction a = new IAction(null, IValue.FALSE) {
//
//				@Override
//				public void execute() {
//					// TODO Auto-generated method stub
//
//				}
//
//				@Override
//				public boolean isSatisfied() {
//					// TODO Auto-generated method stub
//					return false;
//				}
//
//				@Override
//				public ICondition getVariable() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				@Override
//				public IValue getValue() {
//					// TODO Auto-generated method stub
//					return IValue.FALSE;
//				}
//
//			};
//		};
//		Assert.assertThrows(NullPointerException.class, runner);
//	}
//
//	@Test
//	public void nullValueTest() {
//		ThrowingRunnable runner = () -> {
//			@SuppressWarnings("unused")
//			IAction a = new IAction(new BooleanDecision("id"), null) {
//
//				@Override
//				public void execute() {
//				}
//
//				@Override
//				public boolean isSatisfied() {
//					return false;
//				}
//
//				@Override
//				public ICondition getVariable() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				@Override
//				public IValue getValue() {
//					// TODO Auto-generated method stub
//					return IValue.FALSE;
//				}
//			};
//		};
//		Assert.assertThrows(NullPointerException.class, runner);
//	}
//
//	@Test
//	public void passNullVariable() {
//		ThrowingRunnable runner = () -> {
//			IAction a = new IAction(new BooleanDecision("id"), IValue.FALSE) {
//
//				@Override
//				public void execute() {
//				}
//
//				@Override
//				public boolean isSatisfied() {
//					return false;
//				}
//
//				@Override
//				public ICondition getVariable() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				@Override
//				public IValue getValue() {
//					// TODO Auto-generated method stub
//					return IValue.FALSE;
//				}
//			};
//			a.setVariable(null);
//		};
//		Assert.assertThrows(NullPointerException.class, runner);
//	}
//
//	@Test
//	public void passNullValue() {
//		ThrowingRunnable runner = () -> {
//			IAction a = new IAction(new BooleanDecision("id"), IValue.FALSE) {
//
//				@Override
//				public void execute() {
//				}
//
//				@Override
//				public boolean isSatisfied() {
//					return false;
//				}
//
//				@Override
//				public ICondition getVariable() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//
//				@Override
//				public IValue getValue() {
//					// TODO Auto-generated method stub
//					return IValue.FALSE;
//				}
//			};
//			a.setValue(null);
//		};
//		Assert.assertThrows(NullPointerException.class, runner);
//	}
}
