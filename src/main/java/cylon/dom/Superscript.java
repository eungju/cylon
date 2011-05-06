package cylon.dom;

public class Superscript extends TextComposite implements Text {
	public Superscript(Text...children) {
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
