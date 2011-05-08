package cylon.dom;

/**
 * Hierarchical Visitor Pattern.
 */
public interface DomVisitor {
	void visitEnter(Document node);
	void visitLeave(Document node);

	void visit(Heading node);
	
	void visit(HorizontalLine node);	

	void visitEnter(UnorderedList node);
	void visitLeave(UnorderedList node);
	
	void visitEnter(OrderedList node);
	void visitLeave(OrderedList node);
	
	void visitEnter(ListItem node);
	void visitLeave(ListItem node);
	
	void visit(Preformatted node);
	
	void visitEnter(Table node);
	void visitLeave(Table node);

	void visitEnter(TableRow node);	
	void visitLeave(TableRow node);

	void visitEnter(TableCell node);	
	void visitLeave(TableCell node);

	void visitEnter(Paragraph node);
	void visitLeave(Paragraph node);
	
	void visit(Unformatted node);

	void visitEnter(Link node);
	void visitLeave(Link node);

	void visit(Image node);
	
	void visit(ForcedLinebreak node);

	void visit(Code node);

	void visitEnter(Italic node);
	void visitLeave(Italic node);
	
	void visitEnter(Bold node);
	void visitLeave(Bold node);
	
	void visitEnter(Superscript node);
	void visitLeave(Superscript node);

	void visitEnter(Subscript node);
	void visitLeave(Subscript node);

	void visitEnter(Strike node);
	void visitLeave(Strike node);
	
	void visitEnter(Underline node);	
	void visitLeave(Underline node);
}
