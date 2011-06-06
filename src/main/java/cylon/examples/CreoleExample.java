package cylon.examples;

import cylon.creole.CreoleParser;
import cylon.creole.DefaultCreoleParser;
import cylon.dom.Document;
import cylon.html.HtmlRenderer;

public class CreoleExample {
    public static void main(String[] args) {
        CreoleParser parser = new DefaultCreoleParser();
        Document document = parser.document("Hello World");
        HtmlRenderer renderer = new HtmlRenderer(true);
        document.accept(renderer);
        System.out.println(renderer.getResult());
    }
}
