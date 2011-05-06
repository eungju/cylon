package cylon.dom;

public class Paragraph extends TextComposite implements Block {
	private int indent; 
	
	public Paragraph(int indent, Text...children) {
		super(children);
		this.indent = indent;
	}
	
	public int getIndent() {
		return indent;
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (Text each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}
}
