package cylon.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cylon.support.ObjectSupport;

public abstract class ItemList extends ObjectSupport implements Block {
	List<ListItem> children;

	ItemList(ListItem...children) {
		this.children = new ArrayList<ListItem>();
		this.children.addAll(Arrays.asList(children));
	}

	public void addChild(ListItem listItem) {
		children.add(listItem);
	}

	public String toString() {
		return DomUtils.toString(this, children.toArray());
	}
}
