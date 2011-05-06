package cylon.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cylon.support.ObjectSupport;

public abstract class TextComposite extends ObjectSupport implements Node {
	List<Text> children;

	TextComposite(Text...children) {
		this.children = new ArrayList<Text>();
		this.children.addAll(Arrays.asList(children));
	}
	
	public void addChild(Text child) {
		children.add(child);
	}

	public String toString() {
		return DomUtils.toString(this, children.toArray());
	}
}
