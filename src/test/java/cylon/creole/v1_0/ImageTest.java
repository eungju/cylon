package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-ImageInline
 */
public class ImageTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
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
