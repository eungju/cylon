package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-BoldAndItalics
 */
public class BoldAndItalicTest extends DomBuilder {
    private AdhocCreoleParser parser;

    @Before
    public void beforeEach() {
        parser = new AdhocCreoleParser();
    }

    @Test public void
    canBeUsedInsideParagraphsListItemsAndTableCells() {
        assertEquals(document(p(b("bold"))), parser.document("**bold**"));
        assertEquals(document(p(i("italic"))), parser.document("//italic//"));

        assertEquals(document(ul(li(b("bold")))), parser.document("* **bold**"));
        assertEquals(document(ul(li(i("italic")))), parser.document("* //italic//"));

        assertEquals(document(table(tr(td(b("bold"))))), parser.document("| **bold** |"));
        assertEquals(document(table(tr(td(i("italic"))))), parser.document("| //italic// |"));
    }

    @Test public void
    linksAppearingInsideBoldAndOrItalicTextShouldAlsoBecomeBoldAndOrItalic() {
        assertEquals(document(p(b(link("link"), t(" inside bold")))), parser.document("**[[link]] inside bold**"));
        assertEquals(document(p(i(link("link"), t(" inside italic")))), parser.document("//[[link]] inside italic//"));
    }

    @Test public void
    willEndAtTheEndOfParagraphsListItemsAndTableCells() {
        assertEquals(document(p(b("bold"))), parser.document("**bold"));
        assertEquals(document(p(i("italic"))), parser.document("//italic"));

        assertEquals(document(ul(li(b("bold")))), parser.document("* **bold"));
        assertEquals(document(ul(li(i("italic")))), parser.document("* //italic"));

        assertEquals(document(table(tr(td(b("bold"))))), parser.document("| **bold |"));
        assertEquals(document(table(tr(td(i("italic"))))), parser.document("| //italic |"));
    }

    @Test public void
    shouldBeAbleToCrossLines() {
        assertEquals(document(p(t("Bold and italics should "), i(t("be"), t(" "), t("able")), t(" to cross lines."))),
                parser.document("Bold and italics should //be\nable// to cross lines."));
    }

    @Test public void
    shouldNotBeAbleToCrossParagraphs() {
        assertEquals(document(p(t("But, should "), i("not be...")), p(t("...able"), i(" to cross paragraphs."))),
                parser.document("But, should //not be...\n\n...able// to cross paragraphs."));
    }

    @Test public void
    boldItalic() {
        assertEquals(document(p(b(i("bold italics")))), parser.document("**//bold italics//**"));
        assertEquals(document(p(i(b("bold italics")))), parser.document("//**bold italics**//"));
        assertEquals(document(p(i(t("This is "), b("also"), t(" good.")))), parser.document("//This is **also** good.//"));
    }
}