package cylon.creole;

import cylon.dom.Code;
import cylon.dom.TextComposite;

class CodeRule extends InlineRule {
    static final String REGEX = "\\{\\{\\{(.*?)\\}\\}\\}";

    public CodeRule() {
        super(REGEX);
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
        Code node = new Code(group[1]);
        parent.addChild(node);
    }
}
