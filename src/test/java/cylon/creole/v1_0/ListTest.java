package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Lists
 */
public abstract class ListTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends ListTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }

        @Ignore
        @Test public void
        spanMultipleLinesAndContainLineBreaks() {
        }
    }

    public static class LineParserTest extends ListTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
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

    @Test public void
    spanMultipleLinesAndContainLineBreaks() {
        assertEquals(document(ul(li(texts(t("Item 1"), t(" "), t("continued"))))), dut.document("* Item 1\ncontinued"));
        assertEquals(document(ul(li(texts(t("Item 1"), br(), t("continued"))))), dut.document("* Item 1\\\\continued"));
    }
}
