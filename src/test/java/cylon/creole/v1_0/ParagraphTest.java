package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Paragraphs
 */
public class ParagraphTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
    }

    @Test public void
    oneOrMoreBlankLinesEndParagraphs() {
        assertEquals(document(p(t("This is my text.")), p(t("This is more text."))),
                dut.document("This is my text.\n\nThis is more text."));
    }

    @Test public void
    aListTableOrNowikiBlockEndParagraphsToo() {
        assertEquals(document(p(t("This is my text.")), ul(li(t("item")))),
                dut.document("This is my text.\n* item"));

        assertEquals(document(p(t("This is my text.")), table(tr(td(t("cell"))))),
                dut.document("This is my text.\n|cell|"));

        assertEquals(document(p(t("This is my text.")), pre("nowiki\n")),
                dut.document("This is my text.\n{{{\nnowiki\n}}}"));
    }
}
