package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-HorizontalRule
 */
public class HorizontalRuleTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
    }

    @Test public void
    theFourHyphensMustBeTheOnlyCharactersOnThatLine() {
        assertEquals(document(hr()), dut.document("----"));
    }

    @Test public void
    whitespaceIsOptionalBeforeAndAfterTheHyphens() {
        assertEquals(document(hr()), dut.document(" ----"));
        assertEquals(document(hr()), dut.document("---- "));
    }
}
