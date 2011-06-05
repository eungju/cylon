package cylon.creole;

import cylon.dom.Document;

public abstract class AbstractCreoleParser implements CreoleParser {
    protected final DomTrunk cursor = new DomTrunk();

    public abstract Document document(String input);
}
