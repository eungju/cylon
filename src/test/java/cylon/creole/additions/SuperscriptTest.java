package cylon.creole.additions;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/CreoleAdditions#section-CreoleAdditions-Superscript
 */
public class SuperscriptTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
    }

    @Test
    public void surroundedByTwoCarets() {
        assertEquals(document(p(t("This is "), sup("superscripted"), t(" text."))),
                dut.document("This is ^^superscripted^^ text."));
    }
}
