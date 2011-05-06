package cylon.dom;

import cylon.support.ObjectSupport;

public class Preformatted extends ObjectSupport implements Block {
	private String interpreter;
	private String text;

	public Preformatted(String interpreter, String text) {
		this.interpreter = interpreter;
		this.text = text;
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}

	public String getInterpreter() {
		return interpreter;
	}
	
	public boolean hasInterpreter() {
		return interpreter != null;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return DomUtils.toString(this, new Object[] {interpreter, text});
	}
}
