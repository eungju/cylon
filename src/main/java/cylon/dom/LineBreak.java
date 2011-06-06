package cylon.dom;

import cylon.support.ObjectSupport;

public class LineBreak extends ObjectSupport implements Text {
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}
}
