package cylon.dom;

public class Underline extends TextComposite implements Text {
	public Underline(Text...children) {
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
