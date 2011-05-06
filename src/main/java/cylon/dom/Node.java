package cylon.dom;

public interface Node {
	void accept(DomVisitor visitor);
}
