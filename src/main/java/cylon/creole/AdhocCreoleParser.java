package cylon.creole;

import cylon.dom.Document;
import cylon.dom.Heading;
import cylon.dom.HorizontalRule;
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

public class AdhocCreoleParser implements CreoleParser {
    //ORDER IS IMPORTANT TO PARSE CORRECTLY
	private static final RuleSet BLOCK_RULES = new RuleSet(
			new Rule[] {
					new BlockSeparatorRule()
					, new HeadingRule()
					, new HorizontalRuleRule()
					, new PreformattedRule()
					, new ListRule()
					, new TableRule()
					, new ParagraphRule()
			}
			, BlockRule.PATTERN_FLAGS
	);
    private final DomTrunk trunk;
    private final InlineParser inlineParser;

    public AdhocCreoleParser() {
        trunk = new DomTrunk();
        inlineParser = new InlineParser(trunk);
    }

	public Document document(String input) {
		Document node = new Document();
		trunk.descend(node);
		Matcher matcher = BLOCK_RULES.pattern().matcher(input);
        int position = 0;
		while (matcher.find()) {
            String gap = input.substring(position, matcher.start());
            if (gap.trim().length() > 0) {
                throw new RuntimeException("Unrecognized input: " + gap);
            }
			BlockRule matchedRule = (BlockRule) BLOCK_RULES.rule(matcher);
			matchedRule.matched(BLOCK_RULES.group(matcher, matchedRule), this);
            position = matcher.end();
		}
        String gap = input.substring(position);
        if (gap.trim().length() > 0) {
            throw new RuntimeException("Unrecognized input: " + gap);
        }
		return trunk.ascendTo(node);
	}
    
    static abstract class BlockRule extends Rule {
		public static final int PATTERN_FLAGS = Pattern.MULTILINE | Pattern.UNICODE_CASE;
		static final String NEWLINE = "(?:\\r\\n|\\r|\\n)";

		public BlockRule(String expression) {
			super(expression);
		}

        public abstract void matched(String[] group, AdhocCreoleParser parser);
	}

	static class BlockSeparatorRule extends BlockRule {
		public BlockSeparatorRule() {
			super("(?:^\\p{Blank}*" + NEWLINE + ")+");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			parser.trunk.ascendUntil(Document.class);
		}
	}

	static class HeadingRule extends BlockRule {
		public HeadingRule() {
			super("^\\p{Blank}*(={1,6})(.*?)(=+\\p{Blank}*)?$");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			Document parent = parser.trunk.ascendUntil(Document.class);
			Heading node = new Heading(group[1].length(), group[2].trim());
			parent.addChild(node);
		}
	}

	static class HorizontalRuleRule extends BlockRule {
		public HorizontalRuleRule() {
			super("^\\p{Blank}*----\\p{Blank}*$");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			Document parent = parser.trunk.ascendUntil(Document.class);
			HorizontalRule node = new HorizontalRule();
			parent.addChild(node);
		}
	}

	static class PreformattedRule extends BlockRule {
		public PreformattedRule() {
			super("^\\{\\{\\{\\p{Blank}*" + NEWLINE + "((?s:.)*?)^\\}\\}\\}\\p{Blank}*$");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			Document parent = parser.trunk.ascendUntil(Document.class);
			Preformatted node = new Preformatted(group[1].replaceAll("(?m:^\\p{Blank}(\\p{Blank}*\\}\\}\\}\\p{Blank}*)$)", "$1"));
			parent.addChild(node);
		}
	}

	static class ListRule extends BlockRule {
		final Pattern listItemPattern = Pattern.compile("^\\p{Blank}*(\\*+|#+)(.*)$", BlockRule.PATTERN_FLAGS);
		public ListRule() {
			super("(?:^\\p{Blank}*(?:\\*[^*]|#[^#]).*)(?:" + NEWLINE + "^\\p{Blank}*(\\*+|#+)+.*)*$");
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
							parser.trunk.ascendUntil(ListItem.class).addList(list);
						} else {
							Document parent = parser.trunk.ascendUntil(Document.class);
							parent.addChild(list);
						}
						parser.trunk.descend(list);
						depth++;
						//여러 단계를 한 번에 들어갈 경우 빈 ListItem을 넣어준다.
						if (indentDiff != 0) {
							ListItem listItem = new ListItem();
							list.addChild(listItem);
							parser.trunk.descend(listItem);
						}
					}
				} else if (indentDiff < 0) {
					while (indentDiff++ != 0) {
						parser.trunk.ascendTo(ItemList.class);
						depth--;
					}
					list = parser.trunk.ascendUntil(ItemList.class);
				} else {
					list = parser.trunk.ascendUntil(ItemList.class);
				}
				ListItem listItem = new ListItem();
				list.addChild(listItem);
				parser.trunk.descend(listItem);
				parser.inlineParser.recognize(matcher.group(2).trim());
			}
		}
	}

	static class TableRule extends BlockRule {
		/** "|" 문자가 들어가는 마크업은 추가 되어야 한다 */
		final Pattern tableCellPattern = Pattern.compile("\\|(=)?((?:"
				+ InlineParser.LinkRule.REGEX + "|"
				+ InlineParser.ImageRule.REGEX + "|"
				+ InlineParser.CodeRule.REGEX + "|"
				+ InlineParser.EscapeRule.REGEX + "|"
				+ "[^|])+)", BlockRule.PATTERN_FLAGS);
		
		public TableRule() {
			super("^\\p{Blank}*\\|.+$");
		}
		
		public void matched(String[] group, AdhocCreoleParser parser) {
			if (!parser.trunk.is(Table.class)) {
				Document parent = parser.trunk.ascendUntil(Document.class);
				Table newBlock = new Table();
				parent.addChild(newBlock);
				parser.trunk.descend(newBlock);
			}
			Table table = parser.trunk.ascendUntil(Table.class);

			TableRow row = new TableRow();
			table.addChild(row);
			parser.trunk.descend(row);
			Matcher matcher = tableCellPattern.matcher(group[0].trim());
			while (matcher.find()) {
				tableCell_(new String[] {matcher.group(), matcher.group(1), matcher.group(2)}, parser);
			}
			parser.trunk.ascendAndAssert(row);
		}

		public TableCell tableCell_(String[] group, AdhocCreoleParser parser) {
			TableRow parent = parser.trunk.ascendUntil(TableRow.class);
			boolean head = group[1] != null;
			TableCell node = new TableCell(head);
			parent.addChild(node);
			parser.trunk.descend(node);
			parser.inlineParser.recognize(group[2].trim());
			return parser.trunk.ascendTo(node);
		}
	}
	
	static class ParagraphRule extends BlockRule {
		public ParagraphRule() {
			super("^(?:(\\:+|>+)\\p{Blank}*)?(.+)");
		}
		public void matched(String[] group, AdhocCreoleParser parser) {
			if (!(parser.trunk.isDescendedFrom(Paragraph.class) && group[1] == null)) {
				Document parent = parser.trunk.ascendUntil(Document.class);
				Paragraph newBlock = new Paragraph(group[1] != null? group[1].length() : 0);
				parent.addChild(newBlock);
				parser.trunk.descend(newBlock);
			} else {
                TextComposite parent = parser.trunk.ascendUntil(TextComposite.class);
                parent.addChild(new Unformatted(" "));
            }
			parser.inlineParser.recognize(group[2]);
		}
	}
}
