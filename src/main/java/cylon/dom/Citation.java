package cylon.dom;

import org.apache.commons.lang.StringUtils;

import cylon.support.ObjectSupport;

public class Citation extends ObjectSupport implements Text {
	private final Text explanation;
	private final String label;

	public Citation(Text explanation) {
		this(explanation, null);
	}

	public Citation(Text explanation, String label) {
		this.explanation = explanation;
		this.label = label;
	}

	public void accept(DomVisitor visitor) {
		visitor.visitEnter(this);
		explanation.accept(visitor);
		visitor.visitLeave(this);
	}

	public Text getExplanation() {
		return explanation;
	}

	public Unformatted getPlainExplanation() {
		return (Unformatted)explanation;
	}
	
	public Link getLinkExplanation() {
		return (Link)explanation;
	}
	
	public boolean hasLabel() {
		return StringUtils.isNotBlank(label);
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isPlain() {
		return explanation instanceof Unformatted;
	}
	
	public boolean isLink() {
		return explanation instanceof Link;
	}
}
