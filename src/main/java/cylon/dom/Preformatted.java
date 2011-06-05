package cylon.dom;

import cylon.support.ObjectSupport;

public class Preformatted extends ObjectSupport implements Block {
	private String text;

	public Preformatted(String text) {
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
