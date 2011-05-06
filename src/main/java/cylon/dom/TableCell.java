package cylon.dom;

public class TableCell extends TextComposite {
	private boolean head;
	
	public TableCell(boolean head, Text...children) {
		super(children);
		this.head = head;
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (Text each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);		
	}

	public boolean isHead() {
		return head;
	}
}
