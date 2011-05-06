package cylon.dom;

public interface DomNode {
	void accept(DomVisitor visitor);
}
