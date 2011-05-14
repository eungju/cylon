package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-LineBreaks
 */
public class LinkBreakTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
    }

    @Test public void
    wikiStyle() {
        assertEquals(document(p(t("This is the first line,"), br(), t("and this is the second."))),
                dut.document("This is the first line,\\\\and this is the second."));
    }
}
