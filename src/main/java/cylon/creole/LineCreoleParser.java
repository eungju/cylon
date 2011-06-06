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

public class LineCreoleParser implements CreoleParser {
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");
    private final DomTrunk trunk;
    private final InlineParser inlineParser;
    private StringBuilder nowikiBuffer = null;

    public LineCreoleParser() {
        trunk = new DomTrunk();
        inlineParser = new InlineParser(trunk);
    }
    
    public Document document(String input) {
        Document root = new Document();
        trunk.descend(root);

        Matcher matcher = NEWLINE_PATTERN.matcher(input);
        int start = 0;
        while (matcher.find()) {
            String line = input.substring(start, matcher.end());
            start = matcher.end();
            recognizeBlockLine(line);
        }
        String line = input.substring(start);
        if (!line.isEmpty()) {
            recognizeBlockLine(line);
        }

        return trunk.ascendTo(root);
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

    private static final Pattern NOWIKI_OPEN_PATTERN = Pattern.compile("^\\{{3}\\s*");
    private static final Pattern NOWIKI_CLOSE_PATTERN = Pattern.compile("^\\}{3}\\s*");
    boolean recognizeNowiki(String line) {
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
                Document parent = trunk.ascendUntil(Document.class);
                parent.addChild(new Preformatted(nowikiBuffer.toString()));
                nowikiBuffer = null;
            } else {
                nowikiBuffer.append(line.matches("\\s+\\}{3}\\s*") ? line.substring(1) : line);
            }
            return true;
        }
    }

    private static final Pattern BLOCK_SEPARATOR_PATTERN = Pattern.compile("^\\s*");
    boolean recognizeBlockSeparator(String line) {
        Matcher matcher = BLOCK_SEPARATOR_PATTERN.matcher(line);
        if (matcher.matches()) {
            trunk.ascendUntil(Document.class);
            return true;
        }
        return false;
    }

    private static final Pattern HEADING_PATTERN = Pattern.compile("^\\s*(={1,6})\\s*(.+?)\\s*(=+)?\\s*");
    boolean recognizeHeading(String line) {
        Matcher matcher = HEADING_PATTERN.matcher(line);
        if (matcher.matches()) {
            Document parent = trunk.ascendUntil(Document.class);
            parent.addChild(new Heading(matcher.group(1).length(), matcher.group(2)));
            return true;
        }
        return false;
    }

    private static final Pattern HORIZONTAL_RULE_PATTERN = Pattern.compile("^\\s*----\\s*");
    boolean recognizeHorizontalRule(String line) {
        Matcher matcher = HORIZONTAL_RULE_PATTERN.matcher(line);
        if (matcher.matches()) {
            Document parent = trunk.ascendUntil(Document.class);
            parent.addChild(new HorizontalRule());
            return true;
        }
        return false;
    }

    private static final Pattern LIST_PATTERN = Pattern.compile("^\\s*(\\*+|#+)\\s*(.*?)\\s*");
    boolean recognizeList(String line) {
        Matcher matcher = LIST_PATTERN.matcher(line);
        if (matcher.matches() && (trunk.count(ItemList.class) > 0 || matcher.group(1).length() == 1)) {
            String bullets = matcher.group(1);
            //indent
            while (trunk.count(ItemList.class) < bullets.length()) {
                ItemList list = bullets.charAt(trunk.count(ItemList.class)) == '*' ? new UnorderedList() : new OrderedList();
                if (trunk.count(ItemList.class) == 0) {
                    Document parent = trunk.ascendUntil(Document.class);
                    parent.addChild(list);
                } else {
                    trunk.ascendUntil(ListItem.class).addList(list);
                }
                trunk.descend(list);
                //여러 단계를 한 번에 들어갈 경우 빈 ListItem을 넣어준다.
                if (bullets.length() - trunk.count(ItemList.class) > 0) {
                    ListItem listItem = new ListItem();
                    list.addChild(listItem);
                    trunk.descend(listItem);
                }
            }
            //dedent
            while (trunk.count(ItemList.class) > bullets.length()) {
                trunk.ascendTo(ItemList.class);
            }
            
            ItemList list = trunk.ascendUntil(ItemList.class);
            ListItem listItem = new ListItem();
            list.addChild(listItem);
            trunk.descend(listItem);
            inlineParser.recognize(matcher.group(2));
            return true;
        } else if (trunk.count(ItemList.class) > 0) {
            //list items span multiple lines
            TextComposite parent = trunk.ascendUntil(TextComposite.class);
            parent.addChild(new Unformatted(" "));
            inlineParser.recognize(line.trim());
            return true;
        }
        return false;
    }

    private static final Pattern TABLE_PATTERN = Pattern.compile("^\\s*(\\|.*?)\\|?\\s*");
    boolean recognizeTable(String line) {
        Matcher matcher = TABLE_PATTERN.matcher(line);
        if (matcher.matches()) {
            if (!trunk.is(Table.class)) {
                Document parent = trunk.ascendUntil(Document.class);
                Table newBlock = new Table();
                parent.addChild(newBlock);
                trunk.descend(newBlock);
            }
            Table table = trunk.ascendUntil(Table.class);
            TableRow row = new TableRow();
            table.addChild(row);
            trunk.descend(row);
            recognizeCells(matcher.group(1));
            trunk.ascendAndAssert(row);
            return true;
        }
        return false;
    }

    private static final Pattern TABLE_CELL_PATTERN = Pattern.compile("\\|(=)?((?:"
            + InlineParser.LinkRule.REGEX + "|"
            + InlineParser.ImageRule.REGEX + "|"
            + InlineParser.CodeRule.REGEX + "|"
            + InlineParser.EscapeRule.REGEX + "|"
            + "[^|])*)");
    void recognizeCells(String line) {
        Matcher matcher = TABLE_CELL_PATTERN.matcher(line);
        while (matcher.find()) {
            TableRow parent = trunk.ascendUntil(TableRow.class);
            boolean head = matcher.group(1) != null;
            TableCell node = new TableCell(head);
            parent.addChild(node);
            trunk.descend(node);
            inlineParser.recognize(matcher.group(2).trim());
            trunk.ascendTo(node);
        }
    }
    
    private static final Pattern PARAGRAPH_PATTERN = Pattern.compile("^(?:(\\:+|>+)\\s*)?(.*)\\s*");
    boolean recognizeParagraph(String line) {
        Matcher matcher = PARAGRAPH_PATTERN.matcher(line);
        if (matcher.matches()) {
            String indent = matcher.group(1);
            if (!(trunk.isDescendedFrom(Paragraph.class) && indent == null)) {
                Document parent = trunk.ascendUntil(Document.class);
                Paragraph newBlock = new Paragraph(indent != null? indent.length() : 0);
                parent.addChild(newBlock);
                trunk.descend(newBlock);
            } else {
                TextComposite parent = trunk.ascendUntil(TextComposite.class);
                parent.addChild(new Unformatted(" "));
            }
            inlineParser.recognize(matcher.group(2));
            return true;
        }
        return false;
    }
}
