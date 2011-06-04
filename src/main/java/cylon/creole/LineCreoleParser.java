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

public class LineCreoleParser extends AbstractCreoleParser {
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");
    private StringBuilder nowikiBuffer = null;
    
    public Document document(String input) {
        Document root = new Document();
        cursor.descend(root);

        Matcher lm = NEWLINE_PATTERN.matcher(input);
        int s = 0;
        while (lm.find()) {
            String line = input.substring(s, lm.end());
            s = lm.end();
            recognizeBlockLine(line);
        }
        String line = input.substring(s);
        if (!line.isEmpty()) {
            recognizeBlockLine(line);
        }

        return cursor.ascendTo(root);
    }

    void recognizeBlockLine(String line) {
        if (recognizeNowiki(line)) {
        } else if (recognizeBlockSeparator(line)) {
        } else if (recognizeHeading(line)) {
        } else if (recognizeHorizontalRule(line)) {
        } else if (recognizeTable(line)) {
        } else if (recognizeList(line)) {
        } else if (recognizeParagraph(line)) {
        } else {
            throw new IllegalArgumentException("Not recognized line: " + line);
        }
    }

    boolean recognizeNowiki(String line) {
        Pattern NOWIKI_OPEN_PATTERN = Pattern.compile("^\\{{3}\\s*");
        Pattern NOWIKI_CLOSE_PATTERN = Pattern.compile("^\\}{3}\\s*");
        if (nowikiBuffer == null) {
            Matcher matcher = NOWIKI_OPEN_PATTERN.matcher(line);
            if (matcher.matches()) {
                nowikiBuffer = new StringBuilder();
                return true;
            }
            return false;
        } else {
            Matcher matcher = NOWIKI_CLOSE_PATTERN.matcher(line);
            if (matcher.matches()) {
                Document parent = cursor.ascendUntil(Document.class);
                parent.addChild(new Preformatted(null, nowikiBuffer.toString()));
                nowikiBuffer = null;
            } else {
                nowikiBuffer.append(line.matches("\\s+}}}\\s*") ? line.substring(1) : line);
            }
            return true;
        }
    }

    boolean recognizeBlockSeparator(String line) {
        Pattern BLOCK_SEPARATOR_PATTERN = Pattern.compile("^\\s*");
        Matcher matcher = BLOCK_SEPARATOR_PATTERN.matcher(line);
        if (matcher.matches()) {
            cursor.ascendUntil(Document.class);
            return true;
        }
        return false;
    }

    boolean recognizeHeading(String line) {
        Pattern HEADING_PATTERN = Pattern.compile("^\\s*(={1,6})\\s*(.+?)\\s*(=+)?\\s*");
        Matcher matcher = HEADING_PATTERN.matcher(line);
        if (matcher.matches()) {
            Document parent = cursor.ascendUntil(Document.class);
            parent.addChild(new Heading(matcher.group(1).length(), matcher.group(2)));
            return true;
        }
        return false;
    }

    boolean recognizeHorizontalRule(String line) {
        Pattern HORIZONTAL_RULE_PATTERN = Pattern.compile("^\\s*----\\s*");
        Matcher matcher = HORIZONTAL_RULE_PATTERN.matcher(line);
        if (matcher.matches()) {
            Document parent = cursor.ascendUntil(Document.class);
            parent.addChild(new HorizontalLine());
            return true;
        }
        return false;
    }

    boolean recognizeList(String line) {
        Pattern LIST_PATTERN = Pattern.compile("^\\s*([*#]+)\\s*(.*?)\\s*");
        Matcher matcher = LIST_PATTERN.matcher(line);
        if (matcher.matches()) {
            String bullets = matcher.group(1);
            //indent
            while (cursor.count(ItemList.class) < bullets.length()) {
                ItemList list = bullets.charAt(cursor.count(ItemList.class)) == '*' ? new UnorderedList() : new OrderedList();
                if (cursor.count(ItemList.class) == 0) {
                    Document parent = cursor.ascendUntil(Document.class);
                    parent.addChild(list);
                } else {
                    cursor.ascendUntil(ListItem.class).addList(list);
                }
                cursor.descend(list);
                //여러 단계를 한 번에 들어갈 경우 빈 ListItem을 넣어준다.
                if (bullets.length() - cursor.count(ItemList.class) > 0) {
                    ListItem listItem = new ListItem();
                    list.addChild(listItem);
                    cursor.descend(listItem);
                }
            }
            //dedent
            while (cursor.count(ItemList.class) > bullets.length()) {
                cursor.ascendTo(ItemList.class);
            }
            
            ItemList list = cursor.ascendUntil(ItemList.class);
            ListItem listItem = new ListItem();
            list.addChild(listItem);
            cursor.descend(listItem);
            recognizeInlineElements(matcher.group(2));
            return true;
        } else if (cursor.count(ItemList.class) > 0) {
            //list items span multiple lines
            TextComposite parent = cursor.ascendUntil(TextComposite.class);
            parent.addChild(new Unformatted(" "));
            recognizeInlineElements(line.trim());
            return true;
        }
        return false;
    }

    boolean recognizeTable(String line) {
        Pattern TABLE_PATTERN = Pattern.compile("^\\s*(\\|.*?)\\|?\\s*");
        Matcher matcher = TABLE_PATTERN.matcher(line);
        if (matcher.matches()) {
            if (!cursor.is(Table.class)) {
                Document parent = cursor.ascendUntil(Document.class);
                Table newBlock = new Table();
                parent.addChild(newBlock);
                cursor.descend(newBlock);
            }
            Table table = cursor.ascendUntil(Table.class);
            TableRow row = new TableRow();
            table.addChild(row);
            cursor.descend(row);
            recognizeCells(matcher.group(1));
            cursor.ascendAndAssert(row);
            return true;
        }
        return false;
    }

    void recognizeCells(String line) {
        Pattern TABLE_CELL_PATTERN = Pattern.compile("\\|(=)?((?:"
				+ LinkRule.REGEX + "|"
				+ ImageRule.REGEX + "|"
				+ CodeRule.REGEX + "|"
				+ EscapeRule.REGEX + "|"
				+ "[^|])*)");
        Matcher matcher = TABLE_CELL_PATTERN.matcher(line);
        while (matcher.find()) {
            TableRow parent = cursor.ascendUntil(TableRow.class);
            boolean head = matcher.group(1) != null;
            TableCell node = new TableCell(head);
            parent.addChild(node);
            cursor.descend(node);
            recognizeInlineElements(matcher.group(2).trim());
            cursor.ascendTo(node);
        }
    }
    
    boolean recognizeParagraph(String line) {
        Pattern PARAGRAPH_PATTERN = Pattern.compile("^(?:(\\:+|>+)\\s*)?(.*)\\s*");
        Matcher matcher = PARAGRAPH_PATTERN.matcher(line);
        if (matcher.matches()) {
            int groupIndex = 0;
            String indent = matcher.group(1);
            if (!(cursor.isDescendedFrom(Paragraph.class) && indent == null)) {
                Document parent = cursor.ascendUntil(Document.class);
                Paragraph newBlock = new Paragraph(indent != null? indent.length() : 0);
                parent.addChild(newBlock);
                cursor.descend(newBlock);
                groupIndex = 2;
            } else {
                TextComposite parent = cursor.ascendUntil(TextComposite.class);
                parent.addChild(new Unformatted(" "));
            }
            recognizeInlineElements(matcher.group(groupIndex));
            return true;
        }
        return false;
    }

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

    void recognizeInlineElements(String line) {
        Matcher matcher = inlineRules.pattern().matcher(line);
        int pos = 0;
        while (matcher.find()) {
            if (pos < matcher.start()) {
                String unformatted = line.substring(pos, matcher.start());
                TextComposite parent = cursor.ascendUntil(TextComposite.class);
                parent.addChild(new Unformatted(unformatted));
            }
            TokenRule matchedRule = (TokenRule) inlineRules.rule(matcher);
            matchedRule.matched(inlineRules.group(matcher, matchedRule), this);
            pos = matcher.end();
        }
        if (pos < line.length()) {
            String unformatted = line.substring(pos);
            TextComposite parent = cursor.ascendUntil(TextComposite.class);
            parent.addChild(new Unformatted(unformatted));
        }
    }

    protected void parseInline(String input) {
        recognizeInlineElements(input);
    }
}
