package cylon.creole;

import cylon.dom.Subscript;

class SubscriptRule extends InlineRule {
    public SubscriptRule() {
        super(",,");
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        inlineOpenClose(Subscript.class, new Subscript(), parser);
    }
}
