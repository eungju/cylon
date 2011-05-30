package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-NowikiPreformatted
 */
public abstract class NowikiTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends NowikiTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends NowikiTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test public void
    AsABlockTheThreeCurlyBracesShouldBeOnOneLineByItselfToOpenAndAnotherLineOfThreeCurlyBracesShouldBeOnALineByItselfToCloseWithoutLeadingSpaces() {
        assertEquals(document(pre("//This// does **not** get [[formatted]] \n")), dut.document("{{{\n" +
                "//This// does **not** get [[formatted]] \n" +
                "}}}"));
    }

    @Test public void
    inline() {
        assertEquals(document(p(t("Some examples of markup are: "), code("** <i>this</i> ** "))), dut.document("Some examples of markup are: {{{** <i>this</i> ** }}}"));
    }

    @Test public void
    InPreformattedBlocksSinceMarkersMustNotBePrecededByLeadingSpacesLinesWithThreeClosingBracesWhichBelongToThePreformattedBlockMustFollowAtLeastOneSpaceInTheRenderedOutputOneLeadingSpaceIsRemoved() {
        assertEquals(document(pre(
                "if (x != NULL) {\n" +
                "  for (i = 0; i < size; i++) {\n" +
                "    if (x[i] > 0) {\n" +
                "      x[i]--;\n" +
                " }}}\n")), dut.document("{{{\n" +
                "if (x != NULL) {\n" +
                "  for (i = 0; i < size; i++) {\n" +
                "    if (x[i] > 0) {\n" +
                "      x[i]--;\n" +
                "  }}}\n" +
                "}}}"));
    }
}
