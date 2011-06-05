package cylon.creole.additions;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/CreoleAdditions#section-CreoleAdditions-Subscript
 */
public abstract class SubscriptTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends SubscriptTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends SubscriptTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test
    public void surroundedByTwoCommas() {
        assertEquals(document(p(t("This is "), sub("subscripted"), t(" text."))),
                dut.document("This is ,,subscripted,, text."));
    }
}
