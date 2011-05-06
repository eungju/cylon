package cylon.dom;

public class Italic extends TextComposite implements Text {
	public Italic(Text...children) {
		super(children);
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (Text each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}
}
