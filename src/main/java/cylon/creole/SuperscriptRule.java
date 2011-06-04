package cylon.creole;

import cylon.dom.Superscript;

class SuperscriptRule extends InlineRule {
    public SuperscriptRule() {
        super("\\^\\^");
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        inlineOpenClose(Superscript.class, new Superscript(), parser);
    }
}
