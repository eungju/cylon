package cylon.creole;

import cylon.dom.Link;
import cylon.dom.TextComposite;

class FreeStandingUrlRule extends InlineRule {
    static final String OFFICIAL_SCHEMES = "aaa|aaas|acap|cap|cid|crid|data|dav|dict|dns|fax|file|ftp|go|gopher|h323|http|https|im|imap|ldap|mailto|mid|news|nfs|nntp|pop|pres|rtsp|sip|sips|snmp|tel|telnet|urn|wais|xmpp";
    static final String UNOFFICIAL_SCHEMES = "about|aim|callto|cvs|ed2k|feed|fish|git|gizmoproject|iax2|irc|ircs|lastfm|ldaps|magnet|mms|msnim|nsfw|psyc|rsync|secondlife|skype|ssh|svn|sftp|smb|sms|soldat|steam|unreal|ut2004|webcal|xfire|ymsgr";

    static final String REGEX = "(" + OFFICIAL_SCHEMES + "|" + UNOFFICIAL_SCHEMES + "):\\S*[^ \t,.?!:;\\\\\"']";

    public FreeStandingUrlRule() {
        super(REGEX);
    }
    public void matched(String[] group, AbstractCreoleParser parser) {
        TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
        Link node = new Link(group[0]);
        parent.addChild(node);
    }
}
