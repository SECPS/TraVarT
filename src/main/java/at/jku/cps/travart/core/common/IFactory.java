package at.jku.cps.travart.core.common;

public interface IFactory<T> {

	String getId();

	boolean initExtension();

	T create();
}
