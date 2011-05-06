package cylon.dom;

public class Note extends TextComposite implements Block {	
	public Note(Text... children) {
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
