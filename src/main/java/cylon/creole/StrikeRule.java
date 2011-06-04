package cylon.creole;

import cylon.dom.Strike;

class StrikeRule extends InlineRule {
    public StrikeRule() {
        super("--");
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        inlineOpenClose(Strike.class, new Strike(), parser);
    }
}
