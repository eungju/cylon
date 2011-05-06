package cylon.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cylon.support.ObjectSupport;

public class Table  extends ObjectSupport implements Block {
	private List<TableRow> children;
	
	public Table(TableRow...children) {
		this.children = new ArrayList<TableRow>();
		this.children.addAll(Arrays.asList(children));
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (TableRow each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);	
	}

	public void addChild(TableRow child) {
		children.add(child);
	}

	public String toString() {
		return DomUtils.toString(this, children.toArray());
	}
}
