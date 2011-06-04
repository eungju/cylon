package cylon.creole;

import cylon.dom.TextComposite;
import cylon.dom.Unformatted;

class EscapeRule extends InlineRule {
    static final String REGEX = "~(\\S)";

    public EscapeRule() {
        super(REGEX);
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
        Unformatted node = new Unformatted(group[1]);
        parent.addChild(node);
    }
}
