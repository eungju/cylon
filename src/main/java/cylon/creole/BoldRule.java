package cylon.creole;

import cylon.dom.Bold;

class BoldRule extends InlineRule {
    public BoldRule() {
        super("\\*\\*");
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        inlineOpenClose(Bold.class, new Bold(), parser);
    }
}
