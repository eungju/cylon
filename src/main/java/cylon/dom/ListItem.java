package cylon.dom;

import java.util.ArrayList;
import java.util.List;

public class ListItem extends TextComposite {
	private ItemList subList;

	public ListItem() {
		this(new Text[0], null);
	}
	
	public ListItem(Text[] children, ItemList subList) {
		super(children);
		this.subList = subList;
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (Text each : children) {
			each.accept(visitor);
		}
		if (hasList()) {
			subList.accept(visitor);
		}
		visitor.visitLeave(this);
	}

	public boolean hasText() {
		return children.size() > 0;
	}
	
	public boolean hasList() {
		return subList != null;
	}
	
	public void addList(ItemList list) {
		if (subList != null) {
			throw new IllegalStateException("Cannot have many sublists");
		}
		this.subList = list;
	}
	
	public String toString() {
		List<Object> temp = new ArrayList<Object>();
		temp.addAll(children);
		temp.add(subList);
		return DomUtils.toString(this, temp.toArray());
	}
}
