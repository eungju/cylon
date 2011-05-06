package cylon.dom;

import cylon.support.ObjectSupport;

public class Image extends ObjectSupport implements Text {
	private LinkTarget uri;
	private String alternative;
	private String align;

	public Image(String uri, String alternative) {
		this(uri, alternative, null);
	}
	
	public Image(String uri, String alternative, String align) {
		this.uri = new LinkTarget(uri);
		this.alternative = alternative;
		this.align = align;
	}
	
	public LinkTarget getTarget() {
		return uri;
	}
	
	public void accept(DomVisitor visitor) {
		visitor.visit(this);
	}

	public boolean hasAlternative() {
		return alternative != null;
	}
	
	public String getAlternative() {
		return alternative;
	}

	public boolean hasAlign() {
		return align != null;
	}
	
	public String getAlign() {
		return align;
	}
}
