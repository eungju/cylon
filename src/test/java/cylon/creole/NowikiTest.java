package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-NowikiPreformatted
 */
public class NowikiTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
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
