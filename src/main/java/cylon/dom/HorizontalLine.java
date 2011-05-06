package cylon.dom;

import cylon.support.ObjectSupport;

public class HorizontalLine extends ObjectSupport implements Block {

	public HorizontalLine() {
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		return DomUtils.toString(this, new Object[] {});
	}
}
