package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParagraphTest extends DomBuilder {
    private AdhocCreoleParser parser;

    @Before
    public void beforeEach() {
        parser = new AdhocCreoleParser();
    }

    @Test
    public void indentedParagraphsWhichBeginWithColons() {
        assertEquals(
                document(
                        p(t("This is a normal paragraph.")),
                        p(1, t("This is an indented"), t("paragraph in two lines.")),
                        p(2, t("This is more indented."))),
                parser.document(
                        "This is a normal paragraph.\n" +
                        ":This is an indented\n" +
                        "paragraph in two lines.\n" +
                        "::This is more indented.\n"));
    }

    @Test
    public void indentedParagraphsWhichBeginWithRightAngleBracketsAndOptionalSpaces() {
        assertEquals(
                document(
                        p(t("This is a normal paragraph.")),
                        p(1, t("This is an indented"), t("paragraph in two lines.")),
                        p(2, t("This is more indented."))),
                parser.document(
                        "This is a normal paragraph.\n" +
                        "> This is an indented\n" +
                        "paragraph in two lines.\n" +
                        ">> This is more indented.\n"));
    }
}
