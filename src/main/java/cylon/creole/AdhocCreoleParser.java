package cylon.creole;

import cylon.dom.Document;
import cylon.dom.Heading;
import cylon.dom.HorizontalLine;
import cylon.dom.ItemList;
import cylon.dom.ListItem;
import cylon.dom.OrderedList;
import cylon.dom.Paragraph;
import cylon.dom.Preformatted;
import cylon.dom.Table;
import cylon.dom.TableCell;
import cylon.dom.TableRow;
import cylon.dom.TextComposite;
import cylon.dom.Unformatted;
import cylon.dom.UnorderedList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdhocCreoleParser extends AbstractCreoleParser {

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
		public void matched(String[] group, AbstractCreoleParser parser) {
			parser.cursor.ascendUntil(Document.class);
		}
	}

	static class HeadingRule extends BlockRule {
		public HeadingRule() {
			super("^\\p{Blank}*(={1,6})(.*?)(=+\\p{Blank}*)?$");
		}
		public void matched(String[] group, AbstractCreoleParser parser) {
			Document parent = parser.cursor.ascendUntil(Document.class);
			Heading node = new Heading(group[1].length(), group[2].trim());
			parent.addChild(node);
		}
	}

	static class HorizontalLineRule extends BlockRule {
		public HorizontalLineRule() {
			super("^\\p{Blank}*----\\p{Blank}*$");
		}
		public void matched(String[] group, AbstractCreoleParser parser) {
			Document parent = parser.cursor.ascendUntil(Document.class);
			HorizontalLine node = new HorizontalLine();
			parent.addChild(node);
		}
	}

	static class PreformattedRule extends BlockRule {
        final Pattern closingBracesPattern = Pattern.compile("^\\p{Blank}(\\p{Blank}*\\}\\}\\}\\p{Blank}*)$", PATTERN_FLAGS);
		public PreformattedRule() {
			super("^\\{\\{\\{(?:#!(.+))?\\p{Blank}*" + NEWLINE + "((?s:.)*?)^\\}\\}\\}\\p{Blank}*$");
		}
		public void matched(String[] group, AbstractCreoleParser parser) {
			Document parent = parser.cursor.ascendUntil(Document.class);
			Preformatted node = new Preformatted(group[1], closingBracesPattern.matcher(group[2]).replaceAll("$1"));
			parent.addChild(node);
		}
	}

	static class ListRule extends BlockRule {
		final Pattern listItemPattern = Pattern.compile("^\\p{Blank}*([*#]+)(.*)$", BlockRule.PATTERN_FLAGS);
		public ListRule() {
			super("(?:^\\p{Blank}*(?:\\*[^*#]|#[^*#]).*)(?:" + NEWLINE + "^\\p{Blank}*[*#]+.*)*$");
		}
		public void matched(String[] group, AbstractCreoleParser parser) {
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
				parser.parseInline(matcher.group(2).trim());
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
			super("^\\p{Blank}*\\|.+$");
		}
		
		public void matched(String[] group, AbstractCreoleParser parser) {
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
			Matcher matcher = tableCellPattern.matcher(group[0].trim());
			while (matcher.find()) {
				tableCell_(new String[] {matcher.group(), matcher.group(1), matcher.group(2)}, parser);
			}
			parser.cursor.ascendAndAssert(row);
		}

		public TableCell tableCell_(String[] group, AbstractCreoleParser parser) {
			TableRow parent = parser.cursor.ascendUntil(TableRow.class);
			boolean head = group[1] != null;
			TableCell node = new TableCell(head);
			parent.addChild(node);
			parser.cursor.descend(node);
			parser.parseInline(group[2].trim());
			return parser.cursor.ascendTo(node);
		}
	}
	
	static class ParagraphRule extends BlockRule {
		public ParagraphRule() {
			super("^(?:(\\:+|>+)\\p{Blank}*)?(.+)");
		}
		public void matched(String[] group, AbstractCreoleParser parser) {
			int groupIndex = 0;
			if (!(parser.cursor.isDescendedFrom(Paragraph.class) && group[1] == null)) {
				Document parent = parser.cursor.ascendUntil(Document.class);
				Paragraph newBlock = new Paragraph(group[1] != null? group[1].length() : 0);
				parent.addChild(newBlock);
				parser.cursor.descend(newBlock);
				groupIndex = 2;
			} else {
                TextComposite parent = parser.cursor.ascendUntil(TextComposite.class);
                parent.addChild(new Unformatted(" "));
            }
			parser.parseInline(group[groupIndex]);
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
                    , new FreeStandingUrlRule()
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

	public Document document(String input) {
		Document node = new Document();
		cursor.descend(node);
		Matcher matcher = blockRules.pattern().matcher(input);
        int position = 0;
		while (matcher.find()) {
            String gap = input.substring(position, matcher.start());
            if (gap.trim().length() > 0) {
                throw new RuntimeException("Unrecognized input: " + gap);
            }
			TokenRule matchedRule = (TokenRule) blockRules.rule(matcher);
			matchedRule.matched(blockRules.group(matcher, matchedRule), this);
            position = matcher.end();
		}
        String gap = input.substring(position);
        if (gap.trim().length() > 0) {
            throw new RuntimeException("Unrecognized input: " + gap);
        }
		return cursor.ascendTo(node);
	}

	TextComposite formattedText(String input) {
		Paragraph node = new Paragraph(0);
		cursor.descend(node);
		parseInline(input);
		return cursor.ascendTo(node);
	}

	protected void parseInline(String input) {
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
