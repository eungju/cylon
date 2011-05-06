package cylon.dom;

import cylon.support.ObjectSupport;

public class Unformatted extends ObjectSupport implements Text {
	private String text;
	
	public Unformatted(String text) {
		this.text = text;
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}

	public String getText() {
		return text;
	}
	
	public String toString() {
		return DomUtils.toString(this, new Object[] {text});
	}
}
