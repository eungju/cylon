package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SuperscriptTest extends DomBuilder {
    private AdhocCreoleParser parser;

    @Before
    public void beforeEach() {
        parser = new AdhocCreoleParser();
    }

    @Test
    public void surroundedByTwoCarets() {
        assertEquals(document(p(t("This is "), sup("superscripted"), t(" text."))),
                parser.document("This is ^^superscripted^^ text."));
    }
}
