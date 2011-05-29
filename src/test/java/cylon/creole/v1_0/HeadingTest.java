package cylon.creole.v1_0;

import cylon.creole.AdhocCreoleParser;
import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-Headings
 */
public class HeadingTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
    }

    @Test public void
    onlyThreeDifferentSizedLevelsOfHeadingsAreRequired() {
        assertEquals(document(h1("Level 1 (largest)"), h2("Level 2"), h3("Level 3"), h4("Level 4"), h5("Level 5"), h6("Level 6")),
                dut.document("= Level 1 (largest) =\n" +
                        "== Level 2 ==\n" +
                        "=== Level 3 ===\n" +
                        "==== Level 4 ====\n" +
                        "===== Level 5 =====\n" +
                        "====== Level 6 ======"));
    }

    @Test public void
    closingEqualSignsAreOptional() {
        assertEquals(document(h3("Also level 3"), h3("Also level 3"), h3("Also level 3")),
                dut.document("=== Also level 3\n" +
                        "=== Also level 3 =\n" +
                        "=== Also level 3 =="));
    }

    @Test public void
    whitespaceIsAllowedBeforeTheLeftsideEqualSigns() {
        assertEquals(document(h3("Also level 3")),
                dut.document(" === Also level 3 ===\n"));
    }

    @Test public void
    onlyWhitespaceCharactersArePermittedAfterTheClosingEqualSigns() {
        assertEquals(document(h3("Also level 3")),
                dut.document(" === Also level 3 === \n"));
        assertEquals(document(h3("Also level 3 ===!")),
                dut.document(" === Also level 3 ===!\n"));
    }
}
