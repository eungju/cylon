package cylon.creole;

import cylon.dom.Italic;

class ItalicRule extends InlineRule {
    public ItalicRule() {
        super("//");
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        inlineOpenClose(Italic.class, new Italic(), parser);
    }
}
