package cylon.creole;

import cylon.dom.Document;
import cylon.dom.Heading;
import cylon.dom.HorizontalLine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineCreoleParser implements CreoleParser {
    private final DomTrunk cursor = new DomTrunk();

    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");
    
    public Document document(String input) {
        Document root = new Document();
        cursor.descend(root);

        Matcher lm = NEWLINE_PATTERN.matcher(input);
        int s = 0;
        while (lm.find()) {
            String line = input.substring(s, lm.end());
            s = lm.end();
            recognizeLine(line);
        }
        recognizeLine(input.substring(s));

        return cursor.ascendTo(root);
    }

    void recognizeLine(String line) {
        if (recognizeHeading(line)) {
        } else if (recognizeHorizontalRule(line)) {
        }
    }

    boolean recognizeHeading(String line) {
        Pattern HEADING_PATTERN = Pattern.compile("^\\s*(={1,6})(.+?)(=+\\s*)?$");
        Matcher matcher = HEADING_PATTERN.matcher(line.trim());
        if (matcher.matches()) {
            Document parent = cursor.ascendUntil(Document.class);
            parent.addChild(new Heading(matcher.group(1).length(), matcher.group(2).trim()));
            return true;
        }
        return false;
    }

    boolean recognizeHorizontalRule(String line) {
        Pattern HORIZONTAL_RULE_PATTERN = Pattern.compile("^\\s*----\\s*$");
        Matcher matcher = HORIZONTAL_RULE_PATTERN.matcher(line.trim());
        if (matcher.matches()) {
            Document parent = cursor.ascendUntil(Document.class);
            parent.addChild(new HorizontalLine());
            return true;
        }
        return false;
    }
}
