package cylon.dom;

import cylon.support.ObjectSupport;

import java.util.ArrayList;
import java.util.List;

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

    public String toString() {
        List<Object> temp = new ArrayList<Object>();
        temp.add(text);
        return DomUtils.toString(this, temp.toArray());
    }
}
