package cylon.creole;

import cylon.dom.Image;
import cylon.dom.TextComposite;

class ImageRule extends InlineRule {
    static final String REGEX = "\\{\\{([^|]*?)(?:\\|(.*?))?\\}\\}";

    public ImageRule() {
        super(REGEX);
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
        Image node = new Image(group[1], group[2]);
        parent.addChild(node);
    }
}
