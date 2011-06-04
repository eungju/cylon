package cylon.creole;

import cylon.dom.Link;
import cylon.dom.TextComposite;

class LinkRule extends InlineRule {
    static final String REGEX = "\\[\\[([^|]*?)(?:\\|(.*?))?\\]\\]";

    public LinkRule() {
        super(REGEX);
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
        Link node = new Link(group[1].trim());
        parent.addChild(node);
        parser.cursor.descend(node);
        if (group[2] != null) {
            parser.parseInline(group[2].trim());
        }
        parser.cursor.ascendTo(node);
    }
}
