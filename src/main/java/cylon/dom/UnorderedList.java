package cylon.dom;

public class UnorderedList extends ItemList {
	public UnorderedList(ListItem...children) {
		super(children);
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (ListItem each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}

	boolean listType() {
		return false;
	}
}