package cylon.creole;

import cylon.dom.Code;
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

public class LineCreoleParser implements CreoleParser {
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");
    private final DomTrunk cursor = new DomTrunk();
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
        } else if (recognizeList(line)) {
        } else if (recognizeTable(line)) {
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

    //TODO
    boolean recognizeList(String line) {
        Pattern LIST_PATTERN = Pattern.compile("^\\s*([*#]+)\\s*(.*)$\\s*");
        Matcher matcher = LIST_PATTERN.matcher(line);
        if (matcher.matches()) {
            ItemList list;
            if (matcher.group(1).charAt(0) == '*') {
                list = new UnorderedList();
            } else {
                list = new OrderedList();
            }
            Document parent = cursor.ascendUntil(Document.class);
            parent.addChild(list);
            cursor.descend(list);

            ListItem item = new ListItem();
            list.addChild(item);
            cursor.descend(item);

            recognizeInlineElements(matcher.group(2));
            
            return true;
        }
        return false;
    }

    //TODO
    boolean recognizeTable(String line) {
        Pattern TABLE_PATTERN = Pattern.compile("^\\s*\\|.*");
        Matcher matcher = TABLE_PATTERN.matcher(line);
        if (matcher.matches()) {
            Document parent = cursor.ascendUntil(Document.class);

            Table table = new Table();
            parent.addChild(table);
            cursor.descend(table);

            TableRow row = new TableRow();
            table.addChild(row);
            cursor.descend(row);

            TableCell cell = new TableCell(false);
            row.addChild(cell);
            cursor.descend(cell);

            recognizeInlineElements("cell");

            return true;
        }
        return false;
    }

    boolean recognizeParagraph(String line) {
        Pattern PARAGRAPH_PATTERN = Pattern.compile("^(?:(\\:+|>+)\\s*)?(.*)$\\s*");
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

    void recognizeInlineElements(String line) {
        //FIXME:
        Pattern pattern = Pattern.compile("\\{{3}(.*?)\\}{3}");
        Matcher matcher = pattern.matcher(line);
        int pos = 0;
        while (matcher.find()) {
            if (pos < matcher.start()) {
                String unformatted = line.substring(pos, matcher.start());
                TextComposite parent = cursor.ascendUntil(TextComposite.class);
                parent.addChild(new Unformatted(unformatted));
            }
            pos = matcher.end();

            TextComposite parent = cursor.ascendUntil(TextComposite.class);
            Code node = new Code(matcher.group(1));
            parent.addChild(node);
        }
        if (pos < line.length()) {
            String unformatted = line.substring(pos);
            TextComposite parent = cursor.ascendUntil(TextComposite.class);
            parent.addChild(new Unformatted(unformatted));
        }
    }
}
