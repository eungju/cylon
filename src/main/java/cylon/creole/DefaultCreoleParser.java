package cylon.creole;

import cylon.dom.Document;

public class DefaultCreoleParser implements CreoleParser {
    private final CreoleParser parser = new LineCreoleParser();
    
    public Document document(String input) {
        return parser.document(input);
    }
}
