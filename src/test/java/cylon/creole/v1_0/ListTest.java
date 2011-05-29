package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Lists
 */
public class ListTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
    }

    @Test public void
    beginWithAAsteriskOrSharpAtTheBeginningOfALine() {
        assertEquals(document(ul(li(texts(t("Item 1")), ul(li(t("Item 1.1")))), li(t("Item 2")))), dut.document("* Item 1\n** Item 1.1\n* Item 2"));
        assertEquals(document(ol(li(texts(t("Item 1")), ol(li(t("Item 1.1")))), li(t("Item 2")))), dut.document("# Item 1\n## Item 1.1\n# Item 2"));
    }

    @Test public void
    whitespaceIsOptionalBeforeAndAfterTheAsteriskOrSharpCharacters() {
        assertEquals(document(ul(li(texts(t("Item 1")), ul(li(t("Item 1.1")))), li(t("Item 2")))), dut.document(" * Item 1\n  ** Item 1.1\n* Item 2"));
        assertEquals(document(ol(li(texts(t("Item 1")), ol(li(t("Item 1.1")))), li(t("Item 2")))), dut.document(" # Item 1\n  ## Item 1.1\n# Item 2"));
    }

    @Test public void
    aListItemEndsAtTheLineWhichBeginsWithBlankLineHeadingTableOrNowikiBlock() {
        assertEquals(document(ul(li(t("Item 1"))), p(t("a paragraph"))), dut.document("* Item 1\n\na paragraph"));
        assertEquals(document(ul(li(t("Item 1"))), h1("a heading")), dut.document("* Item 1\n= a heading"));
        assertEquals(document(ul(li(t("Item 1"))), table(tr(td(t("a table"))))), dut.document("* Item 1\n|a table|"));
        assertEquals(document(ul(li(t("Item 1"))), pre("nowiki\n")), dut.document("* Item 1\n{{{\nnowiki\n}}}"));
    }

    @Ignore("TODO")
    @Test public void
    spanMultipleLinesAndContainLineBreaks() {
        assertEquals(document(ul(li(texts(t("Item 1\n"), t("continued"))))), dut.document("* Item 1\ncontinued"));
        assertEquals(document(ul(li(texts(t("Item 1"), br(), t("continued"))))), dut.document("* Item 1\\\\continued"));
    }
}
