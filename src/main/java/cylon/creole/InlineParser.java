package cylon.creole;

import cylon.dom.Bold;
import cylon.dom.Code;
import cylon.dom.ForcedLinebreak;
import cylon.dom.Image;
import cylon.dom.Italic;
import cylon.dom.Link;
import cylon.dom.Strike;
import cylon.dom.Subscript;
import cylon.dom.Superscript;
import cylon.dom.Text;
import cylon.dom.TextComposite;
import cylon.dom.Underline;
import cylon.dom.Unformatted;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InlineParser {
    //ORDER IS IMPORTANT TO PARSE CORRECTLY
    private static final RuleSet INLINE_RULES = new RuleSet(
            new Rule[]{
                    new CodeRule()
                    , new LinkRule()
                    , new FreeStandingUrlRule()
                    , new ImageRule()
                    , new EscapeRule()
                    , new ForcedLinebreakRule()
                    , new BoldRule()
                    , new ItalicRule()
                    , new UnderlineRule()
                    , new StrikeRule()
                    , new SuperscriptRule()
                    , new SubscriptRule()
            }
            , InlineRule.PATTERN_FLAGS
    );
    protected final DomTrunk cursor;

    public InlineParser(DomTrunk cursor) {
        this.cursor = cursor;
    }

    public void recognize(String line) {
        Matcher matcher = INLINE_RULES.pattern().matcher(line);
        int pos = 0;
        while (matcher.find()) {
            if (pos < matcher.start()) {
                String unformatted = line.substring(pos, matcher.start());
                TextComposite parent = cursor.ascendUntil(TextComposite.class);
                parent.addChild(new Unformatted(unformatted));
            }
            InlineRule matchedRule = (InlineRule) INLINE_RULES.rule(matcher);
            matchedRule.matched(INLINE_RULES.group(matcher, matchedRule), this);
            pos = matcher.end();
        }
        if (pos < line.length()) {
            String unformatted = line.substring(pos);
            TextComposite parent = cursor.ascendUntil(TextComposite.class);
            parent.addChild(new Unformatted(unformatted));
        }
    }

    abstract static class InlineRule extends Rule {
        public static final int PATTERN_FLAGS = Pattern.UNICODE_CASE;

        public InlineRule(String expression) {
            super(expression);
        }

        <T extends Text> void inlineOpenClose(Class<T> type, T newNode, InlineParser parser) {
            if (parser.cursor.is(type)) {
                T node = parser.cursor.ascendUntil(type);
                parser.cursor.ascendAndAssert(node);
            } else {
                TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
                parent.addChild(newNode);
                parser.cursor.descend(newNode);
            }
        }

        public abstract void matched(String[] group, InlineParser parser);
    }

    static class CodeRule extends InlineRule {
        static final String REGEX = "\\{\\{\\{(.*?)\\}\\}\\}";

        public CodeRule() {
            super(REGEX);
        }

        public void matched(String[] group, InlineParser parser) {
            TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
            Code node = new Code(group[1]);
            parent.addChild(node);
        }
    }

    static class EscapeRule extends InlineRule {
        static final String REGEX = "~(\\S)";

        public EscapeRule() {
            super(REGEX);
        }

        public void matched(String[] group, InlineParser parser) {
            TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
            Unformatted node = new Unformatted(group[1]);
            parent.addChild(node);
        }
    }

    static class ForcedLinebreakRule extends InlineRule {
        public ForcedLinebreakRule() {
            super("\\\\\\\\");
        }

        public void matched(String[] group, InlineParser parser) {
            TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
            ForcedLinebreak node = new ForcedLinebreak();
            parent.addChild(node);
        }
    }

    static class LinkRule extends InlineRule {
        static final String REGEX = "\\[\\[([^|]*?)(?:\\|(.*?))?\\]\\]";

        public LinkRule() {
            super(REGEX);
        }

        public void matched(String[] group, InlineParser parser) {
            TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
            Link node = new Link(group[1].trim());
            parent.addChild(node);
            parser.cursor.descend(node);
            if (group[2] != null) {
                parser.recognize(group[2].trim());
            }
            parser.cursor.ascendTo(node);
        }
    }

    static class FreeStandingUrlRule extends InlineRule {
        static final String OFFICIAL_SCHEMES = "aaa|aaas|acap|cap|cid|crid|data|dav|dict|dns|fax|file|ftp|go|gopher|h323|http|https|im|imap|ldap|mailto|mid|news|nfs|nntp|pop|pres|rtsp|sip|sips|snmp|tel|telnet|urn|wais|xmpp";
        static final String UNOFFICIAL_SCHEMES = "about|aim|callto|cvs|ed2k|feed|fish|git|gizmoproject|iax2|irc|ircs|lastfm|ldaps|magnet|mms|msnim|nsfw|psyc|rsync|secondlife|skype|ssh|svn|sftp|smb|sms|soldat|steam|unreal|ut2004|webcal|xfire|ymsgr";

        static final String REGEX = "(" + OFFICIAL_SCHEMES + "|" + UNOFFICIAL_SCHEMES + "):\\S*[^ \t,.?!:;\\\\\"']";

        public FreeStandingUrlRule() {
            super(REGEX);
        }

        public void matched(String[] group, InlineParser parser) {
            TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
            Link node = new Link(group[0]);
            parent.addChild(node);
        }
    }

    static class ImageRule extends InlineRule {
        static final String REGEX = "\\{\\{([^|]*?)(?:\\|(.*?))?\\}\\}";

        public ImageRule() {
            super(REGEX);
        }

        public void matched(String[] group, InlineParser parser) {
            TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
            Image node = new Image(group[1], group[2]);
            parent.addChild(node);
        }
    }

    static class BoldRule extends InlineRule {
        public BoldRule() {
            super("\\*\\*");
        }

        public void matched(String[] group, InlineParser parser) {
            inlineOpenClose(Bold.class, new Bold(), parser);
        }
    }

    static class ItalicRule extends InlineRule {
        public ItalicRule() {
            super("//");
        }

        public void matched(String[] group, InlineParser parser) {
            inlineOpenClose(Italic.class, new Italic(), parser);
        }
    }

    static class StrikeRule extends InlineRule {
        public StrikeRule() {
            super("--");
        }

        public void matched(String[] group, InlineParser parser) {
            inlineOpenClose(Strike.class, new Strike(), parser);
        }
    }

    static class SubscriptRule extends InlineRule {
        public SubscriptRule() {
            super(",,");
        }

        public void matched(String[] group, InlineParser parser) {
            inlineOpenClose(Subscript.class, new Subscript(), parser);
        }
    }

    static class SuperscriptRule extends InlineRule {
        public SuperscriptRule() {
            super("\\^\\^");
        }

        public void matched(String[] group, InlineParser parser) {
            inlineOpenClose(Superscript.class, new Superscript(), parser);
        }
    }

    static class UnderlineRule extends InlineRule {
        public UnderlineRule() {
            super("__");
        }

        public void matched(String[] group, InlineParser parser) {
            inlineOpenClose(Underline.class, new Underline(), parser);
        }
    }
}
