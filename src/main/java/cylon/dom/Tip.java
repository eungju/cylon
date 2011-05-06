package cylon.dom;

public class Tip extends TextComposite implements Block {	
	public Tip(Text... children) {
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
