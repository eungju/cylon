package cylon.dom;


public class Quotation extends TextComposite implements Block {	
	public Quotation(Text...children) {
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
