package cylon.examples;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.Document;
import cylon.html.HtmlRenderer;

public class CreoleExample {
    public static void main(String[] args) {
        AdhocCreoleParser parser = new AdhocCreoleParser();
        Document document = parser.document("Hello World");
        HtmlRenderer renderer = new HtmlRenderer(true);
        document.accept(renderer);
        System.out.println(renderer.asString());
    }
}
