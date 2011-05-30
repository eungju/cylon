package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Paragraphs
 */
public abstract class ParagraphTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends ParagraphTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends ParagraphTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test public void
    oneOrMoreBlankLinesEndParagraphs() {
        assertEquals(document(p(t("This is my text.")), p(t("This is more text."))),
                dut.document("This is my text.\n\nThis is more text."));
    }

    @Test public void
    listsEndParagraphs() {
        assertEquals(document(p(t("This is my text.")), ul(li(t("item")))),
                dut.document("This is my text.\n* item"));
    }

    @Test public void
    tablesEndParagraphs() {
        assertEquals(document(p(t("This is my text.")), table(tr(td(t("cell"))))),
                dut.document("This is my text.\n|cell|"));
    }

    @Test public void
    nowikiBlocksEndParagraphs() {
        assertEquals(document(p(t("This is my text.")), pre("nowiki\n")),
                dut.document("This is my text.\n{{{\nnowiki\n}}}"));
    }
}
