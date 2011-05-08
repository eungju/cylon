package cylon.html;

import cylon.dom.Bold;
import cylon.dom.Citation;
import cylon.dom.Code;
import cylon.dom.Document;
import cylon.dom.DomVisitor;
import cylon.dom.ForcedLinebreak;
import cylon.dom.Heading;
import cylon.dom.HorizontalLine;
import cylon.dom.Image;
import cylon.dom.Italic;
import cylon.dom.Link;
import cylon.dom.LinkTarget;
import cylon.dom.ListItem;
import cylon.dom.Note;
import cylon.dom.OrderedList;
import cylon.dom.Paragraph;
import cylon.dom.Preformatted;
import cylon.dom.Quotation;
import cylon.dom.Strike;
import cylon.dom.Subscript;
import cylon.dom.Superscript;
import cylon.dom.Table;
import cylon.dom.TableCell;
import cylon.dom.TableRow;
import cylon.dom.Tip;
import cylon.dom.Underline;
import cylon.dom.Unformatted;
import cylon.dom.UnorderedList;
import cylon.dom.WikiAdaptor;

public class HtmlRenderer implements DomVisitor {
	protected WikiAdaptor adaptor;
	protected StringBuilder buffer = new StringBuilder();

	public HtmlRenderer(WikiAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	
	public String asString() {
		return buffer.toString();
	}

	public void visitEnter(Document node) {
	}

	public void visitLeave(Document node) {
	}

    public void visit(Heading node) {
        buffer.append("<h").append(node.getLevel()).append('>');
        buffer.append(escapeHtml(node.getTitle()));
        buffer.append("</h").append(node.getLevel()).append('>');
    }

    public void visit(HorizontalLine node) {
		buffer.append("<hr />");
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
	}

	public void visitLeave(Table node) {
		buffer.append("</table>");
	}

	public void visitEnter(TableRow node) {
		buffer.append("<tr>");		
	}

	public void visitLeave(TableRow node) {
		buffer.append("</tr>");
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
	}

	public void visitLeave(UnorderedList node) {
		buffer.append("</ul>");
	}

	public void visitEnter(OrderedList node) {
		buffer.append("<ol>");
	}

	public void visitLeave(OrderedList node) {
		buffer.append("</ol>");
	}

	public void visitEnter(ListItem node) {
		buffer.append("<li>");
	}

	public void visitLeave(ListItem node) {
		buffer.append("</li>");
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
	}

	public void visitEnter(Quotation node) {
		buffer.append("<blockquote><p>");
	}

	public void visitLeave(Quotation node) {
		buffer.append("</p></blockquote>");
	}

	public void visitEnter(Note node) {
		buffer.append("<div class=\"importance\"><p>");
	}

	public void visitLeave(Note node) {
		buffer.append("</p></div>");
	}

	public void visitEnter(Tip node) {
		buffer.append("<div class=\"tip\"><p>");
	}

	public void visitLeave(Tip node) {
		buffer.append("</p></div>");
	}

	public void visit(Code node) {
		buffer.append("<code>");
		buffer.append(escapeHtml(node.getText()));
		buffer.append("</code>");
	}

	public void visit(ForcedLinebreak node) {
		buffer.append("<br />");
	}

	public void visitEnter(Link node) {
		String href = null;
		boolean doNotLink = false;
		String cssClass = "";
		LinkTarget target = node.getTarget();
		if (target.isUrl()) {
			cssClass += " url";
			href = target.getUrl();
			doNotLink = !target.isSafeUrl();
		} else if (target.isPage()) {
			cssClass += " page";
			if (target.isNeighborWiki()) {
				href = adaptor.pageUrl(target.getNeighborWikiId(), target.getPageName());
			} else {
				if (adaptor.isPageExist(target.getPageName())) {
					href = adaptor.pageUrl(target.getPageName());
				} else {
					href = adaptor.pageEditUrl(target.getPageName());
					cssClass += " broken";
				}
			}
		} else if (target.isAttachment()) {
			cssClass += " attachment";
			if (adaptor.hasAttachmentOfName(target.getAttachmentName())) {
				href = adaptor.attachmentUrl(target.getAttachmentName());
			} else {
				href = target.getTarget();
				doNotLink = true;
				cssClass += " broken";
			}
		}
		
		buffer.append("<a");
		if (doNotLink) {
			buffer.append(" title=\"").append(escapeHtml(href)).append("\"");
		} else {
			buffer.append(" href=\"").append(escapeHtml(href)).append("\"");
		}
		buffer.append(" class=\"").append(cssClass.trim()).append("\"");
		if (target.isUrl()) {
			buffer.append(" target=\"_blank\"");
		}
		buffer.append(">");
		if (!node.hasDescription()) {
			buffer.append(escapeHtml(target.getTarget()));
		}
	}

	public void visitLeave(Link node) {
		buffer.append("</a>");
	}

	public void visit(Image node) {
		LinkTarget target = node.getTarget();
		String src = null;
		boolean doNotLink = false;
		String cssClass = "";
		if (target.isAttachment()) {
			cssClass += " attachment";
			if (adaptor.hasAttachmentOfName(target.getAttachmentName())) {
				src = adaptor.attachmentUrl(target.getAttachmentName());
			} else {
				src = target.getTarget();
				doNotLink = true;
				cssClass += " broken";
			}
		} else if (target.isUrl()) {
			src = target.getUrl();
		}
		
		if (node.hasAlign()) {
			cssClass += "f_" + node.getAlign();
		}
		
		buffer.append("<img");
		if (doNotLink) {
			buffer.append(" title=\"").append(escapeHtml(src)).append("\"");
		} else {
			buffer.append(" src=\"").append(escapeHtml(src)).append("\"");
		}
		if (cssClass.trim().length() > 0) {
			buffer.append(" class=\"").append(cssClass.trim()).append("\"");
		}
		if (node.hasAlternative()) {
			 buffer.append(" alt=\"").append(escapeHtml(node.getAlternative())).append("\"");
		}
		buffer.append(" />");
	}

    public void visitEnter(Citation node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void visitLeave(Citation node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public static String escapeHtml(String str) {
        return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    }
}