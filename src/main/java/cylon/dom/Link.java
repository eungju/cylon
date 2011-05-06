package cylon.dom;

import java.util.ArrayList;
import java.util.List;

public class Link extends TextComposite implements Text {
	private LinkTarget target;
	
	public Link(String target, Text...children) {
		super(children);
		this.target = new LinkTarget(target);
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (Text each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}

	public LinkTarget getTarget() {
		return target;
	}
	
	public boolean hasDescription() {
		return children.size() > 0;
	}

	public String toString() {
		List<Object> temp = new ArrayList<Object>();
		temp.add(target);
		temp.addAll(children);
		return DomUtils.toString(this, temp.toArray());
	}
}
