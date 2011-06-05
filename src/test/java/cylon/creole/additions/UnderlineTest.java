package cylon.creole.additions;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/CreoleAdditions#section-CreoleAdditions-Underline
 */
public abstract class UnderlineTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends UnderlineTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends UnderlineTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test
    public void surroundedByTwoCarets() {
        assertEquals(document(p(t("This is "), u("underlined"), t(" text."))),
                dut.document("This is __underlined__ text."));
    }
}
