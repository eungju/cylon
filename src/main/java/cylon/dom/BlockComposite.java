package cylon.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cylon.support.ObjectSupport;

public abstract class BlockComposite extends ObjectSupport implements DomNode {
	List<Block> children;

	BlockComposite(Block...children) {
		this.children = new ArrayList<Block>();
		this.children.addAll(Arrays.asList(children));
	}
	
	public void addChild(Block child) {
		children.add(child);
	}

	public String toString() {
		return DomUtils.toString(this, children.toArray());
	}
}
