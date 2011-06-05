package cylon.creole.additions;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/CreoleAdditions#section-CreoleAdditions-IndentedParagraphs
 */
public abstract class IndentedParagraphTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends IndentedParagraphTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends IndentedParagraphTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test
    public void beginWithColons() {
        assertEquals(
                document(
                        p(t("This is a normal paragraph.")),
                        p(1, t("This is an indented"), t(" "), t("paragraph in two lines.")),
                        p(2, t("This is more indented."))),
                dut.document(
                        "This is a normal paragraph.\n" +
                        ":This is an indented\n" +
                        "paragraph in two lines.\n" +
                        "::This is more indented.\n"));
    }

    @Test
    public void beginWithRightAngleBracketsAndOptionalSpaces() {
        assertEquals(
                document(
                        p(t("This is a normal paragraph.")),
                        p(1, t("This is an indented"), t(" "), t("paragraph in two lines.")),
                        p(2, t("This is more indented."))),
                dut.document(
                        "This is a normal paragraph.\n" +
                        "> This is an indented\n" +
                        "paragraph in two lines.\n" +
                        ">> This is more indented.\n"));
    }
}
