package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubscriptTest extends DomBuilder {
    private AdhocCreoleParser parser;

    @Before
    public void beforeEach() {
        parser = new AdhocCreoleParser();
    }

    @Test
    public void surroundedByTwoCommas() {
        assertEquals(document(p(t("This is "), sub("subscripted"), t(" text."))),
                parser.document("This is ,,subscripted,, text."));
    }
}
