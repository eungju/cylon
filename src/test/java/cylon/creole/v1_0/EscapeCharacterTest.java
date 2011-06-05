package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-EscapeCharacter
 */
public abstract class EscapeCharacterTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends EscapeCharacterTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends EscapeCharacterTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test public void
    theFollowingCharacterIsRenderedAsIsAndCannotBeAPartOfAnyCreoleMarkup() {
        assertEquals(document(p(t("#"), t("1"))), dut.document("~#1"));
    }

    @Test public void
    outsideNowikiPreformattedAndURLTheEscapeCharacterOnlyEscapesTheCharacterImmediatelyFollowingItProvidedThatItIsNotABlank() {
        assertEquals(document(p(link("http://www.foo.com/~bar/"))), dut.document("http://www.foo.com/~bar/"));
        assertEquals(document(p(t("~ "))), dut.document("~ "));
    }
}
