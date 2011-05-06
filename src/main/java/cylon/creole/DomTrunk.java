package cylon.creole;

import cylon.dom.DomNode;

import java.util.ArrayDeque;
import java.util.Deque;

public class DomTrunk {
	private Deque<DomNode> trunk = new ArrayDeque<DomNode>();

	public void descend(DomNode node) {
		trunk.push(node);
	}
	
	public DomNode ascend() {
		return trunk.pop();
	}

	@SuppressWarnings("unchecked")
	public <T extends DomNode> T ascendAndAssert(T node) {
		DomNode e = trunk.pop();
		assertNode(node, e);
		return (T) e;
	}

	@SuppressWarnings("unchecked")
	public <T extends DomNode> T ascendAndAssert(Class<T> type) {
		DomNode e = trunk.pop();
		assertNode(type, e);
		return (T) e;
	}

	/**
	 * exclusive
	 */
	@SuppressWarnings("unchecked")
	public <T extends DomNode> T ascendUntil(Class<T> type) {
		while (!trunk.isEmpty()) {
			if (is(type)) {
				return (T)trunk.peek();
			}
			trunk.pop();
		}
		throw new IllegalStateException("");
	}

	/**
	 * inclusive
	 */
	@SuppressWarnings("unchecked")
	public <T extends DomNode> T ascendTo(Class<T> type) {
		while (!trunk.isEmpty()) {
			if (is(type)) {
				return (T)trunk.pop();
			}
			trunk.pop();
		}
		throw new IllegalStateException("");
	}

	/**
	 * inclusive
	 */
	@SuppressWarnings("unchecked")
	public <T extends DomNode> T ascendTo(T node) {
		while (!trunk.isEmpty()) {
			if (trunk.peek() == node) {
				return (T)trunk.pop();
			}
			trunk.pop();
		}
		throw new IllegalStateException("");
	}

	public boolean is(Class<? extends DomNode> type) {
		return type.isAssignableFrom(trunk.peek().getClass());
	}

	public boolean isDescendedFrom(Class<? extends DomNode> type) {
		for (DomNode each: trunk) {
			if (type.isAssignableFrom(each.getClass())) {
				return true;
			}
		}
		return false;
	}

	void assertNode(Object expected, Object actual) {
		if (expected != actual) {
			throw new AssertionError("expected: " + expected + " but was: " + actual);
		}
	}

	void assertNode(Class<?> expected, Object actual) {
		if (!expected.isAssignableFrom(actual.getClass())) {
			throw new AssertionError("expected: " + expected.getName() + " but was: " + actual.getClass().getName());
		}
	}
}
