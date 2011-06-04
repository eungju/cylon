package cylon.creole;

import cylon.dom.Underline;

class UnderlineRule extends InlineRule {
    public UnderlineRule() {
        super("__");
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        inlineOpenClose(Underline.class, new Underline(), parser);
    }
}
