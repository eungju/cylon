package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-LineBreaks
 */
public abstract class LineBreakTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends LineBreakTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends LineBreakTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test public void
    wikiStyle() {
        assertEquals(document(p(t("This is the first line,"), br(), t("and this is the second."))),
                dut.document("This is the first line,\\\\and this is the second."));
    }
}
