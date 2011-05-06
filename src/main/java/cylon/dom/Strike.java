package cylon.dom;

public class Strike extends TextComposite implements Text {
	public Strike(Text...children) {
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
