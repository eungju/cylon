package cylon.creole;

import cylon.dom.Node;

import java.util.ArrayDeque;
import java.util.Deque;

public class DomTrunk {
	private Deque<Node> nodes = new ArrayDeque<Node>();

	public void descend(Node node) {
		nodes.push(node);
	}
	
	public Node ascend() {
		return nodes.pop();
	}

	@SuppressWarnings("unchecked")
	public <T extends Node> T ascendAndAssert(T node) {
		Node e = nodes.pop();
		assertNode(node, e);
		return (T) e;
	}

	@SuppressWarnings("unchecked")
	public <T extends Node> T ascendAndAssert(Class<T> type) {
		Node e = nodes.pop();
		assertNode(type, e);
		return (T) e;
	}

	/**
	 * exclusive
	 */
	@SuppressWarnings("unchecked")
	public <T extends Node> T ascendUntil(Class<T> type) {
		while (!nodes.isEmpty()) {
			if (is(type)) {
				return (T) nodes.peek();
			}
			nodes.pop();
		}
		throw new IllegalStateException("");
	}

	/**
	 * inclusive
	 */
	@SuppressWarnings("unchecked")
	public <T extends Node> T ascendTo(Class<T> type) {
		while (!nodes.isEmpty()) {
			if (is(type)) {
				return (T) nodes.pop();
			}
			nodes.pop();
		}
		throw new IllegalStateException("");
	}

	/**
	 * inclusive
	 */
	@SuppressWarnings("unchecked")
	public <T extends Node> T ascendTo(T node) {
		while (!nodes.isEmpty()) {
			if (nodes.peek() == node) {
				return (T) nodes.pop();
			}
			nodes.pop();
		}
		throw new IllegalStateException("");
	}

	public boolean is(Class<? extends Node> type) {
		return type.isAssignableFrom(nodes.peek().getClass());
	}

	public boolean isDescendedFrom(Class<? extends Node> type) {
		for (Node each: nodes) {
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

    public int count(Class<? extends Node> type) {
        int result = 0;
        for (Node each : nodes) {
            if (type.isAssignableFrom(each.getClass())) {
                result++;
            }
        }
        return result;
    }
}
