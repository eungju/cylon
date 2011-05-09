package cylon.dom;

import java.util.ArrayList;
import java.util.List;

public class Link extends TextComposite implements Text {
	private String uri;
	
	public Link(String uri, Text...children) {
		super(children);
		this.uri = uri;
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (Text each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);
	}

	public String getUri() {
		return uri;
	}
	
	public boolean hasDescription() {
		return children.size() > 0;
	}

	public String toString() {
		List<Object> temp = new ArrayList<Object>();
		temp.add(uri);
		temp.addAll(children);
		return DomUtils.toString(this, temp.toArray());
	}
}
