package cylon.dom;

import cylon.support.ObjectSupport;

import java.util.ArrayList;
import java.util.List;

public class Image extends ObjectSupport implements Text {
	private String uri;
	private String alternative;

	public Image(String uri, String alternative) {
		this.uri = uri;
		this.alternative = alternative;
	}
	
	public String getUri() {
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

    public String toString() {
        List<Object> temp = new ArrayList<Object>();
        temp.add(uri);
        temp.add(alternative);
        return DomUtils.toString(this, temp.toArray());
    }
}
