package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnderlineTest extends DomBuilder {
    private AdhocCreoleParser parser;

    @Before
    public void beforeEach() {
        parser = new AdhocCreoleParser();
    }

    @Test
    public void surroundedByTwoCarets() {
        assertEquals(document(p(t("This is "), u("underlined"), t(" text."))),
                parser.document("This is __underlined__ text."));
    }
}
