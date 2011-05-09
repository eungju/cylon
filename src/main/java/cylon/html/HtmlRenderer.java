package cylon.html;

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

public class HtmlRenderer implements DomVisitor {
	protected StringBuilder buffer = new StringBuilder();
    private final boolean indent;

    public HtmlRenderer() {
        this(false);
    }

    public HtmlRenderer(boolean indent) {
        this.indent = indent;
    }

	public String asString() {
		return buffer.toString();
	}

    private void newline() {
        if (indent) {
            buffer.append("\n");
        }
    }

	public void visitEnter(Document node) {
	}

	public void visitLeave(Document node) {
	}

    public void visit(Heading node) {
        buffer.append("<h").append(node.getLevel()).append('>');
        buffer.append(escapeHtml(node.getTitle()));
        buffer.append("</h").append(node.getLevel()).append('>');
        newline();
    }

    public void visit(HorizontalLine node) {
		buffer.append("<hr />");
        newline();
	}

	public void visitEnter(Paragraph node) {
		if(node.getIndent() > 0) {
			buffer.append("<p class=\"indent_").append(node.getIndent()).append("\">");
		} else {
			buffer.append("<p>");
		}
	}

	public void visitLeave(Paragraph node) {
		buffer.append("</p>");
        newline();
	}

	public void visit(Unformatted node) {
		buffer.append(escapeHtml(node.getText()));
	}

	public void visitEnter(Italic node) {
		buffer.append("<em>");
	}

	public void visitLeave(Italic node) {
		buffer.append("</em>");
	}

	public void visitEnter(Bold node) {
		buffer.append("<strong>");
	}

	public void visitLeave(Bold node) {
		buffer.append("</strong>");
	}

	public void visitEnter(Table node) {
		buffer.append("<table class=\"table\" border=\"1\" cellspacing=\"0\">");
        newline();
	}

	public void visitLeave(Table node) {
		buffer.append("</table>");
        newline();
	}

	public void visitEnter(TableRow node) {
		buffer.append("<tr>");		
        newline();
	}

	public void visitLeave(TableRow node) {
		buffer.append("</tr>");
        newline();
	}

	public void visitEnter(TableCell node) {
		if(node.isHead()) buffer.append("<th scope=\"row\">");
		else buffer.append("<td>");
	}

	public void visitLeave(TableCell node) {
		if(node.isHead()) buffer.append("</th>");
		else buffer.append("</td>");
	}

	public void visitEnter(UnorderedList node) {
		buffer.append("<ul>");
        newline();
	}

	public void visitLeave(UnorderedList node) {
		buffer.append("</ul>");
        newline();
	}

	public void visitEnter(OrderedList node) {
		buffer.append("<ol>");
        newline();
	}

	public void visitLeave(OrderedList node) {
		buffer.append("</ol>");
        newline();
	}

	public void visitEnter(ListItem node) {
		buffer.append("<li>");
	}

	public void visitLeave(ListItem node) {
		buffer.append("</li>");
        newline();
	}

	public void visitEnter(Superscript node) {
		buffer.append("<sup>");
	}

	public void visitLeave(Superscript node) {
		buffer.append("</sup>");
	}

	public void visitEnter(Subscript node) {
		buffer.append("<sub>");
	}

	public void visitLeave(Subscript node) {
		buffer.append("</sub>");
	}

	public void visitEnter(Strike node) {
		buffer.append("<del>");
	}

	public void visitLeave(Strike node) {
		buffer.append("</del>");
	}

	public void visitEnter(Underline node) {
		buffer.append("<u>");
	}

	public void visitLeave(Underline node) {
		buffer.append("</u>");
	}

	public void visit(Preformatted node) {
		buffer.append("<pre");
		if (node.hasInterpreter()) {
			String[] args = node.getInterpreter().split("\\s+");
			if (args[0].equals("syntax")) {
				buffer.append(" name=\"code\" class=\"").append(args[1]).append('"');
			}
		}
		buffer.append('>');
		buffer.append(escapeHtml(node.getText()));
		buffer.append("</pre>");
        newline();
	}

	public void visit(Code node) {
		buffer.append("<code>");
		buffer.append(escapeHtml(node.getText()));
		buffer.append("</code>");
	}

	public void visit(ForcedLinebreak node) {
		buffer.append("<br />");
        newline();
	}

	public void visitEnter(Link node) {
		buffer.append("<a");
		buffer.append(" href=\"").append(escapeHtml(node.getUri())).append("\"");
		buffer.append(">");
		if (!node.hasDescription()) {
			buffer.append(escapeHtml(node.getUri()));
		}
	}

	public void visitLeave(Link node) {
		buffer.append("</a>");
	}

	public void visit(Image node) {
		buffer.append("<img");
		buffer.append(" src=\"").append(escapeHtml(node.getUri())).append("\"");
		if (node.hasAlternative()) {
			 buffer.append(" alt=\"").append(escapeHtml(node.getAlternative())).append("\"");
		}
		buffer.append(" />");
	}

    public static String escapeHtml(String str) {
        return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    }
}