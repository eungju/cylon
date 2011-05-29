package cylon.creole.additions;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/CreoleAdditions#section-CreoleAdditions-IndentedParagraphs
 */
public class IndentedParagraphTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
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
