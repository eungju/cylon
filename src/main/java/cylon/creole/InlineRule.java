package cylon.creole;

import cylon.dom.Text;
import cylon.dom.TextComposite;

import java.util.regex.Pattern;

abstract class InlineRule extends TokenRule {
    public static final int PATTERN_FLAGS = Pattern.UNICODE_CASE;

    public InlineRule(String expression) {
        super(expression);
    }

    <T extends Text> void inlineOpenClose(Class<T> type, T newNode, AbstractCreoleParser parser) {
        if (parser.cursor.is(type)) {
            T node = parser.cursor.ascendUntil(type);
            parser.cursor.ascendAndAssert(node);
        } else {
            TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
            parent.addChild(newNode);
            parser.cursor.descend(newNode);
        }
    }
}
