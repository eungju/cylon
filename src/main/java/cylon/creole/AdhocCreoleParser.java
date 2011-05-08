package cylon.creole;

import cylon.dom.Bold;
import cylon.dom.Code;
import cylon.dom.Document;
import cylon.dom.ForcedLinebreak;
import cylon.dom.Heading;
import cylon.dom.HorizontalLine;
import cylon.dom.Image;
import cylon.dom.Italic;
import cylon.dom.ItemList;
import cylon.dom.Link;
import cylon.dom.ListItem;
import cylon.dom.OrderedList;
import cylon.dom.Paragraph;
import cylon.dom.Preformatted;
import cylon.dom.Strike;
import cylon.dom.Subscript;
import cylon.dom.Superscript;
import cylon.dom.Table;
import cylon.dom.TableCell;
import cylon.dom.TableRow;
import cylon.dom.Text;
import cylon.dom.TextComposite;
import cylon.dom.Underline;
import cylon.dom.Unformatted;
import cylon.dom.UnorderedList;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdhocCreoleParser {
	static abstract class TokenRule extends Rule {
		public TokenRule(String expression) {
			super(expression);
		}
		public abstract void matched(String[] group, AdhocCreoleParser parser);
	}
	
	static abstract class BlockRule extends TokenRule {
		public static final int PATTERN_FLAGS = Pattern.MULTILINE | Pattern.UNICODE_CASE;
		static final String NEWLINE = "(?:\\r\\n|\\r|\\n)";
		
		public BlockRule(String expression) {
			super(expression);
		}
	}

	static class BlockSeparatorRule extends BlockRule {
		public BlockSeparatorRule() {
			super("(?:^\\p{Blank}*" + NEWLINE + ")+");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			parser.cursor.ascendUntil(Document.class);
		}
	}

	static class HeadingRule extends BlockRule {
		public HeadingRule() {
			super("^(={1,5})(.*?)(=*)\\p{Blank}*$");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			Document parent = parser.cursor.ascendUntil(Document.class);
			Heading node = new Heading(group[1].length(), group[2]);
			parent.addChild(node);
		}
	}

	static class HorizontalLineRule extends BlockRule {
		public HorizontalLineRule() {
			super("^----\\p{Blank}*$");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			Document parent = parser.cursor.ascendUntil(Document.class);
			HorizontalLine node = new HorizontalLine();
			parent.addChild(node);
		}
	}

	static class PreformattedRule extends BlockRule {
		public PreformattedRule() {
			super("^\\{\\{\\{(?:#!(.+))?\\p{Blank}*" + NEWLINE + "((?s:.)*?)^\\}\\}\\}\\p{Blank}*$");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			Document parent = parser.cursor.ascendUntil(Document.class);
			Preformatted node = new Preformatted(group[1], group[2].replaceAll("^~(}}})", "$1"));
			parent.addChild(node);
		}
	}

	static class ListRule extends BlockRule {
		final Pattern listItemPattern = Pattern.compile("^\\p{Blank}*([*#]+)(.*)$", BlockRule.PATTERN_FLAGS);
		public ListRule() {
			super("(?:^\\p{Blank}*(?:\\*[^*#]|#[^*#]).*)(?:" + NEWLINE + "^\\p{Blank}*[*#]+.*)*$");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			int depth = 0;
			Matcher matcher = listItemPattern.matcher(group[0]);
			while (matcher.find()) {
				String bullet = matcher.group(1);
				ItemList list = null;
				int indentDiff = bullet.length() - depth;
				if (indentDiff > 0) {
					while (indentDiff-- != 0) {
						list = bullet.charAt(depth) == '*' ? new UnorderedList() : new OrderedList();
						if (depth > 0) {
							parser.cursor.ascendUntil(ListItem.class).addList(list);
						} else {
							Document parent = parser.cursor.ascendUntil(Document.class);
							parent.addChild(list);
						}
						parser.cursor.descend(list);
						depth++;
						//여러 단계를 한 번에 들어갈 경우 빈 ListItem을 넣어준다.
						if (indentDiff != 0) {
							ListItem listItem = new ListItem();
							list.addChild(listItem);
							parser.cursor.descend(listItem);
						}
					}
				} else if (indentDiff < 0) {
					while (indentDiff++ != 0) {
						parser.cursor.ascendTo(ItemList.class);
						depth--;
					}
					list = parser.cursor.ascendUntil(ItemList.class);
				} else {
					list = parser.cursor.ascendUntil(ItemList.class);
				}
				ListItem listItem = new ListItem();
				list.addChild(listItem);
				parser.cursor.descend(listItem);
				parser.parseInline(matcher.group(2));
			}
		}
	}

	static class TableRule extends BlockRule {
		/** "|" 문자가 들어가는 마크업은 추가 되어야 한다 */
		final Pattern tableCellPattern = Pattern.compile("\\|(=)?((?:"
				+ LinkRule.REGEX + "|"
				+ ImageRule.REGEX + "|"
				+ CodeRule.REGEX + "|"
				+ EscapeRule.REGEX + "|"
				+ "[^|])+)", BlockRule.PATTERN_FLAGS);
		
		public TableRule() {
			super("^\\|.+\\|\\p{Blank}*$");
		}
		
		public void matched(String[] group, AdhocCreoleParser parser) {
			if (!parser.cursor.is(Table.class)) {
				Document parent = parser.cursor.ascendUntil(Document.class);
				Table newBlock = new Table();
				parent.addChild(newBlock);
				parser.cursor.descend(newBlock);
			}
			Table table = parser.cursor.ascendUntil(Table.class);

			TableRow row = new TableRow();
			table.addChild(row);
			parser.cursor.descend(row);
			Matcher matcher = tableCellPattern.matcher(group[0]);
			while (matcher.find()) {
				tableCell_(new String[] {matcher.group(), matcher.group(1), matcher.group(2)}, parser);
			}
			parser.cursor.ascendAndAssert(row);
		}

		public TableCell tableCell_(String[] group, AdhocCreoleParser parser) {
			TableRow parent = parser.cursor.ascendUntil(TableRow.class);
			boolean head = group[1] != null;
			TableCell node = new TableCell(head);
			parent.addChild(node);
			parser.cursor.descend(node);
			if (head) {
				node.addChild(new Unformatted(group[2]));
			} else {
				parser.parseInline(group[2]);
			}
			return parser.cursor.ascendTo(node);
		}
	}
	
	static class ParagraphRule extends BlockRule {
		public ParagraphRule() {
			super("^(>+)?(.+)");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			int groupIndex = 0;
			if (!parser.cursor.isDescendedFrom(Paragraph.class) || group[1] != null) {
				Document parent = parser.cursor.ascendUntil(Document.class);
				Paragraph newBlock = new Paragraph(group[1] != null? group[1].length() : 0);
				parent.addChild(newBlock);
				parser.cursor.descend(newBlock);
				groupIndex = 2;
			}
			parser.cursor.ascendUntil(TextComposite.class);
			parser.parseInline(group[groupIndex]);
		}
	}

	static abstract class InlineRule extends TokenRule {
		public static final int PATTERN_FLAGS = Pattern.UNICODE_CASE;
		
		public InlineRule(String expression) {
			super(expression);
		}

		<T extends Text> void inlineOpenClose(Class<T> type, T newNode, AdhocCreoleParser parser) {
			if (parser.cursor.is(type)) {
				T node = parser.cursor.ascendUntil(type);
				parser.cursor.ascendAndAssert(node);
			} else {
				TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
				parent.addChild(newNode);
				parser.cursor.descend(newNode);
			}
		}

		static final String OFFICIAL_SCHEMES = "aaa|aaas|acap|cap|cid|crid|data|dav|dict|dns|fax|file|ftp|go|gopher|h323|http|https|im|imap|ldap|mailto|mid|news|nfs|nntp|pop|pres|rtsp|sip|sips|snmp|tel|telnet|urn|wais|xmpp";
		static final String UNOFFICIAL_SCHEMES = "about|aim|callto|cvs|ed2k|feed|fish|git|gizmoproject|iax2|irc|ircs|lastfm|ldaps|magnet|mms|msnim|nsfw|psyc|rsync|secondlife|skype|ssh|svn|sftp|smb|sms|soldat|steam|unreal|ut2004|webcal|xfire|ymsgr";
		/**
		 * See http://en.wikipedia.org/wiki/URI_scheme
		 */
		boolean isUrl(String uri) {
			final Pattern PATTERN = Pattern.compile("^(" + OFFICIAL_SCHEMES + "|" + UNOFFICIAL_SCHEMES + "):[^:]*");
			return PATTERN.matcher(uri).matches();
		}
	}

	static class LinkRule extends InlineRule {
		static final String REGEX = "\\[\\[(.+?)\\s*(?:\\|\\s*(.+?))?\\]\\]";
		
		public LinkRule() {
			super(REGEX);
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
			Link node = new Link(group[1]);
			parent.addChild(node);
			parser.cursor.descend(node);
			if (group[2] != null) {
				parser.parseInline(group[2]);
			}
			parser.cursor.ascendTo(node);
		}
	}	

	static class CodeRule extends InlineRule {
		static final String REGEX = "\\{\\{\\{(.*?)\\}\\}\\}";
		
		public CodeRule() {
			super(REGEX);
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
			Code node = new Code(group[1]);
			parent.addChild(node);
		}
	}

	static class ImageRule extends InlineRule {
		static final String REGEX = "\\{\\{(.+?)((\\|\\w+=[^|]*)*)\\}\\}";
		
		public ImageRule() {
			super(REGEX);
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
			Map<String, String> options = options(group[2]);
			Image node = new Image(group[1], options.get("alt"), options.get("align"));
			parent.addChild(node);
		}
		Map<String,String> options(String options) {
			Map<String,String> result = new HashMap<String, String>();
			Pattern pattern = Pattern.compile("\\|(\\w+)=([^|]*)");
			Matcher matcher = pattern.matcher(options);
			while (matcher.find()) {
				result.put(matcher.group(1), matcher.group(2));
			}
			return result;
		}
	}

	static class BoldRule extends InlineRule {
		public BoldRule() {
			super("\\*\\*");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			inlineOpenClose(Bold.class, new Bold(), parser);			
		}
	}

	static class ItalicRule extends InlineRule {
		public ItalicRule() {
			super("//");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			inlineOpenClose(Italic.class, new Italic(), parser);			
		}
	}

	static class UnderlineRule extends InlineRule {
		public UnderlineRule() {
			super("__");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			inlineOpenClose(Underline.class, new Underline(), parser);			
		}
	}

	static class StrikeRule extends InlineRule {
		public StrikeRule() {
			super("--");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			inlineOpenClose(Strike.class, new Strike(), parser);			
		}
	}

	static class SuperscriptRule extends InlineRule {
		public SuperscriptRule() {
			super("\\^\\^");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			inlineOpenClose(Superscript.class, new Superscript(), parser);			
		}
	}

	static class SubscriptRule extends InlineRule {
		public SubscriptRule() {
			super(",,");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			inlineOpenClose(Subscript.class, new Subscript(), parser);			
		}
	}

	static class ForcedLinebreakRule extends InlineRule {
		public ForcedLinebreakRule() {
			super("\\\\\\\\");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
			ForcedLinebreak node = new ForcedLinebreak();
			parent.addChild(node);
		}
	}

	static class EscapeRule extends InlineRule {
		static final String REGEX = "~(\\p{Punct})";
		
		public EscapeRule() {
			super(REGEX);
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
			Unformatted node = new Unformatted(group[1]);
			parent.addChild(node);
		}
	}

	//ORDER IS IMPORTANT TO PARSE CORRECTLY
	private static final RuleSet blockRules = new RuleSet(
			new Rule[] {
					new BlockSeparatorRule()
					, new HeadingRule()
					, new HorizontalLineRule()
					, new PreformattedRule()
					, new ListRule()
					, new TableRule()
					, new ParagraphRule()
			}
			, BlockRule.PATTERN_FLAGS
	);
	//ORDER IS IMPORTANT TO PARSE CORRECTLY
	private static final RuleSet inlineRules = new RuleSet(
			new Rule[] {
					new LinkRule()
					, new CodeRule()
					, new ImageRule()
					, new BoldRule()
					, new ItalicRule()
					, new UnderlineRule()
					, new StrikeRule()
					, new SuperscriptRule()
					, new SubscriptRule()
					, new ForcedLinebreakRule()
					, new EscapeRule()
			}
			, InlineRule.PATTERN_FLAGS
	);
	private final DomTrunk cursor = new DomTrunk();

	public Document document(String input) {
		Document node = new Document();
		cursor.descend(node);
		Matcher matcher = blockRules.pattern().matcher(input);
		while (matcher.find()) {
			TokenRule matchedRule = (TokenRule) blockRules.rule(matcher);
			matchedRule.matched(blockRules.group(matcher, matchedRule), this);
		}
		return cursor.ascendTo(node);
	}

	TextComposite formattedText(String input) {
		Paragraph node = new Paragraph(0);
		cursor.descend(node);
		parseInline(input);
		return cursor.ascendTo(node);
	}

	void parseInline(String input) {
		Matcher matcher = inlineRules.pattern().matcher(input);
		int position = 0;
		while (matcher.find()) {
			String plain = input.substring(position, matcher.start());
			if (plain.length() > 0) {
				TextComposite parent = cursor.ascendUntil(TextComposite.class);
				parent.addChild(new Unformatted(plain));
			}
			TokenRule matchedRule = (TokenRule) inlineRules.rule(matcher);
			matchedRule.matched(inlineRules.group(matcher, matchedRule), this);
			position = matcher.end();
		}
		String plain = input.substring(position);
		if (plain.length() > 0) {
			TextComposite parent = cursor.ascendUntil(TextComposite.class);
			parent.addChild(new Unformatted(plain));
		}
	}
}
