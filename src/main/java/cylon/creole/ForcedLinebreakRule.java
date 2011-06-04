package cylon.creole;

import cylon.dom.ForcedLinebreak;
import cylon.dom.TextComposite;

class ForcedLinebreakRule extends InlineRule {
    public ForcedLinebreakRule() {
        super("\\\\\\\\");
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
        ForcedLinebreak node = new ForcedLinebreak();
        parent.addChild(node);
    }
}
