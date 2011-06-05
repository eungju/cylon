package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-ImageInline
 */
public abstract class ImageTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends ImageTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends ImageTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test public void
    enclosedByDoubleCurlyBrackets() {
        assertEquals(document(p(image("myimage.png"))), dut.document("{{myimage.png}}"));
    }

    @Test public void
    hasAlt() {
        assertEquals(document(p(image("myimage.png", "this is my image"))), dut.document("{{myimage.png|this is my image}}"));
    }
}
