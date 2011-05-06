package cylon.dom;

public class OrderedList extends ItemList {
	public OrderedList(ListItem...children) {
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
		return true;
	}
}
