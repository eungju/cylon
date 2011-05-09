package cylon.creole;

import cylon.dom.Bold;
import cylon.dom.Code;
import cylon.dom.Document;
import cylon.dom.DomVisitor;
import cylon.dom.ForcedLinebreak;
import cylon.dom.Heading;
import cylon.dom.HorizontalLine;
import cylon.dom.Image;
import cylon.dom.Italic;
import cylon.dom.Link;
import cylon.dom.ListItem;
import cylon.dom.OrderedList;
import cylon.dom.Paragraph;
import cylon.dom.Preformatted;
import cylon.dom.Strike;
import cylon.dom.Subscript;
import cylon.dom.Superscript;
import cylon.dom.Table;
import cylon.dom.TableCell;
import cylon.dom.TableRow;
import cylon.dom.Underline;
import cylon.dom.Unformatted;
import cylon.dom.UnorderedList;

public class CreoleMarkupRenderer extends CreoleMarkupBuilder implements DomVisitor {
	private StringBuilder listBullets = new StringBuilder();
	
	public String asString() {
		return toString();
	}

	public void visitEnter(Document node) {
	}
	
	public void visitLeave(Document node) {
	}	

	public void visit(Heading dom) {
		newline();
		emit('=', dom.getLevel()).emit(dom.getTitle()).emit('=', dom.getLevel());
		newline();
	}

	public void visit(HorizontalLine node) {
		newline().emit("----").newline();
	}
	
	public void visitEnter(Paragraph node) {
		newline().emit('>', node.getIndent());
	}

	public void visitLeave(Paragraph node) {
		newline().newline(true);
	}

	public void visitEnter(Table node) {
		newline();
	}

	public void visitLeave(Table node) {
		newline();
	}

	public void visitEnter(TableRow node) {
		newline();
	}

	public void visitLeave(TableRow node) {
		emit("|").newline();
	}

	public void visitEnter(TableCell node) {
		if (node.isHead()) {
			emit("|=");
		} else {
			emit("|");
		}
	}

	public void visitLeave(TableCell node) {
	}

	public void visit(Preformatted node) {
		newline().emit("{{{");
		if (node.hasInterpreter()) {
			String[] args = node.getInterpreter().split("\\s+");
			if (args[0].equals("syntax")) {
				emit("#!syntax ").emit(args[1]);
			}
		}
		newline();
		preformatted(node.getText());
		newline().emit("}}}").newline();
	}

	public void visitEnter(UnorderedList node) {
		listBullets.append('*');
	}

	public void visitLeave(UnorderedList node) {
		listBullets.deleteCharAt(listBullets.length() - 1);
	}

	public void visitEnter(OrderedList node) {
		listBullets.append('#');
	}

	public void visitLeave(OrderedList node) {
		listBullets.deleteCharAt(listBullets.length() - 1);
	}

	public void visitEnter(ListItem node) {
		if (node.hasText()) {
			newline().emit(listBullets.toString());
		}
	}

	public void visitLeave(ListItem node) {
		newline();
	}

	public void visitEnter(Italic node) {
		italic();
	}

	public void visitLeave(Italic node) {
		italic();
	}

	public void visitEnter(Bold node) {
		bold();
	}
	
	public void visitLeave(Bold node) {
		bold();
	}
	
	public void visitEnter(Superscript node) {
		superscript();
	}

	public void visitLeave(Superscript node) {
		superscript();
	}
	
	public void visitEnter(Subscript node) {
		subscript();
	}

	public void visitLeave(Subscript node) {
		subscript();
	}

	public void visitEnter(Strike node) {
		strike();
	}

	public void visitLeave(Strike node) {
		strike();
	}

	public void visitEnter(Underline node) {
		underline();
	}

	public void visitLeave(Underline node) {
		underline();
	}

	public void visit(Unformatted node) {
		unformatted(node.getText());
	}

	public void visit(ForcedLinebreak node) {
		forcedLinebreak();
	}

	public void visit(Code node) {
		markup("{{{").emit(node.getText()).markup("}}}");
	}
	
	public void visitEnter(Link node) {
		markup("[[");
		emit(node.getUri());
		if (node.hasDescription()) {
			emit("|");
		}
	}
	
	public void visitLeave(Link node) {
		markup("]]");
	}	

	public void visit(Image node) {
		markup("{{");
		emit(node.getUri());
		if(node.hasAlternative()) {
			emit("|alt=").emit(node.getAlternative());
		}
		if(node.hasAlign()) {
			emit("|align=").emit(node.getAlign());
		}
		markup("}}");
	}
}
