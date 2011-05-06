package cylon.dom;

public class Document extends BlockComposite {
	public Document(Block...children) {
		super(children);
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (Block each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}
}
