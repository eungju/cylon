package cylon.dom;


public class DomBuilder {
	protected Document document(Block... children) {
		return new Document(children);
	}
	
	protected HorizontalRule hr() {
		return new HorizontalRule();
	}
	
	protected Heading h(int level, String title) {
		return new Heading(level, title);
	}

	
	protected Heading h1(String title) { return h(1, title); }
	protected Heading h2(String title) { return h(2, title); }
	protected Heading h3(String title) { return h(3, title); }
	protected Heading h4(String title) { return h(4, title); }
	protected Heading h5(String title) { return h(5, title); }
    protected Heading h6(String title) { return h(6, title); }

	protected UnorderedList ul(ListItem... children) {
		return new UnorderedList(children);
	}
	
	protected OrderedList ol(ListItem... children) {
		return new OrderedList(children);
	}
	
	protected ListItem li(Text[] texts, ItemList list) {
		return new ListItem(texts, list);
	}
	
	protected ListItem li(Text... children) {
		return new ListItem(children, null);
	}
	
	protected Table table(TableRow... children) {
		return new Table(children);
	}
	
	protected TableRow tr(TableCell... children) {
		return new TableRow(children);
	}
	
	protected TableCell th(Text...children) {
		return new TableCell(true, children);
	}
	
	protected TableCell td(Text...children) {
		return new TableCell(false, children);
	}
	
	protected Preformatted pre(String text) {
		return new Preformatted(text);
	}

	protected Paragraph p(Text...children) {
		return new Paragraph(0, children);
	}
	
	protected Paragraph p(int indent, Text...children) {
		return new Paragraph(indent, children);
	}
	
	protected Text[] texts(Text... texts) {
		return texts;
	}

	protected Link link(String uri, Text... children) {
		return new Link(uri, children);
	}

	protected Image image(String uri, String alt) {
		return new Image(uri, alt);
	}

	protected Image image(String uri) {
		return new Image(uri, null);
	}
	
	protected Code code(String text) {
		return new Code(text);
	}
	
	protected LineBreak br() {
		return new LineBreak();
	}
	
	protected Unformatted t(String text) {
		return new Unformatted(text);
	}

	protected Bold b(String text) {
		return new Bold(new Unformatted(text));
	}
	
	protected Bold b(Text... children) {
		return new Bold(children);
	}
	
	protected Italic i(String text) {
		return new Italic(new Unformatted(text));
	}
	
	protected Italic i(Text... children) {
		return new Italic(children);
	}
	
	protected Underline u(String text) {
		return new Underline(new Unformatted(text));
	}
	
	protected Underline u(Text... children) {
		return new Underline(children);
	}
	
	protected Strike s(String text) {
		return new Strike(new Unformatted(text));
	}
	
	protected Strike s(Text... children) {
		return new Strike(children);
	}
	
	protected Superscript sup(String text) {
		return new Superscript(new Unformatted(text));
	}
	
	protected Superscript sup(Text... children) {
		return new Superscript(children);
	}

	protected Subscript sub(String text) {
		return new Subscript(new Unformatted(text));
	}

	protected Subscript sub(Text... children) {
		return new Subscript(children);
	}
}
