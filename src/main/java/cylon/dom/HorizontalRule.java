package cylon.dom;

import cylon.support.ObjectSupport;

public class HorizontalRule extends ObjectSupport implements Block {

	public HorizontalRule() {
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		return DomUtils.toString(this, new Object[] {});
	}
}
