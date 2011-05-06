package cylon.dom;

import cylon.support.ObjectSupport;

public class Heading extends ObjectSupport implements Block {
	private int level;
	private String title;

	public Heading(int level, String title) {
		this.level = level;
		this.title = title;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		return DomUtils.toString(this, new Object[] {level, title});
	}
}
