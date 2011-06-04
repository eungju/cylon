package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.creole.CreoleParser;
import cylon.creole.LineCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Tables
 */
public abstract class TableTest extends DomBuilder {
    protected CreoleParser dut;

    public static class AdhocParserTest extends TableTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends TableTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
    }

    @Test public void
    allCellsAreSeparatedBySinglePipes() {
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(t("Cell 1.2"))),
                tr(td(t("Cell 2.1")), td(t("Cell 2.2"))))), dut.document("|Cell 1.1|Cell 1.2|\n|Cell 2.1|Cell 2.2|"));
    }

    @Test public void
    leadingSpacesArePermittedBeforeTheFirstCellOfARowAndTrailingSpacesArePermittedAtTheEndOfALine() {
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(t("Cell 1.2"))),
                tr(td(t("Cell 2.1")), td(t("Cell 2.2"))))), dut.document(" | Cell 1.1 | Cell 1.2 |  \n\t| Cell 2.1 | Cell 2.2 | "));
    }

    @Test public void
    theEndingPipeIsOptional() {
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(t("Cell 1.2"))),
                tr(td(t("Cell 2.1")), td(t("Cell 2.2"))))), dut.document("|Cell 1.1|Cell 1.2\n|Cell 2.1|Cell 2.2"));
    }

    @Test public void
    canEmbedLinksBoldItalicsLineBreaksAndNowiki() {
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(link("http://www.wikicreole.org", t("Link")), t(" in Cell 1.2"))))), dut.document("|Cell 1.1|[[http://www.wikicreole.org|Link]] in Cell 1.2|"));
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(b("Bold"), t(" in Cell 1.2"))))), dut.document("|Cell 1.1|**Bold** in Cell 1.2|"));
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(i("Italic"), t(" in Cell 1.2"))))), dut.document("|Cell 1.1|//Italic// in Cell 1.2|"));
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(t("Two lines"), br(), t("in Cell 1.2"))))), dut.document("|Cell 1.1|Two lines\\\\in Cell 1.2|"));
        assertEquals(document(table(
                tr(td(t("Cell 1.1")), td(code("Nowiki"), t(" in Cell 1.2"))))), dut.document("|Cell 1.1|{{{Nowiki}}} in Cell 1.2|"));
    }

    @Test public void
    equalSignDirectlyFollowingPipeDefinesAHeader() {
        assertEquals(document(table(
                tr(th(t("Heading Col 1")), th(t("Heading Col 2"))),
                tr(td(t("Cell 1.1")), td(t("Cell 1.2"))))), dut.document("|=Heading Col 1 |=Heading Col 2         |\n|Cell 1.1|Cell 1.2|"));

    }
}
