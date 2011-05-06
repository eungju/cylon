package cylon.dom;

import cylon.support.ObjectSupport;

public class Code extends ObjectSupport implements Text {
	private String text;

	public Code(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}
}
