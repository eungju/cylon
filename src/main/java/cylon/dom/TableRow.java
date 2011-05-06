package cylon.dom;

import java.util.ArrayList;
import java.util.Arrays;

import cylon.support.ObjectSupport;

public class TableRow extends ObjectSupport implements Node {
	private ArrayList<TableCell> children;

	public TableRow(TableCell...children) {
		this.children = new ArrayList<TableCell>();
		this.children.addAll(Arrays.asList(children));
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		for (TableCell each : children) {
			each.accept(visitor);
		}
		visitor.visitLeave(this);	
	}

	public void addChild(TableCell child) {
		children.add(child);
	}

	public String toString() {
		return DomUtils.toString(this, children.toArray());
	}
}
