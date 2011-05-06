package cylon.dom;

public class Subscript extends TextComposite implements Text {
	public Subscript(Text...children) {
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
