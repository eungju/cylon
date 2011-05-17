package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * http://www.wikicreole.org/wiki/Creole1.0#section-Creole1.0-LinksInternalExternalAndInterwiki
 */
public class LinkTest extends DomBuilder {
    private AdhocCreoleParser dut;

    @Before
    public void beforeEach() {
        dut = new AdhocCreoleParser();
    }

    @Test public void
    internalLink() {
        assertEquals(document(p(link("link"))), dut.document("[[link]]"));
        assertEquals(document(p(link("MyBigPage", t("Go to my page")))), dut.document("[[MyBigPage|Go to my page]]"));
    }

    @Test public void
    externalLink() {
        assertEquals(document(p(link("http://www.wikicreole.org/"))), dut.document("[[http://www.wikicreole.org/]]"));
        assertEquals(document(p(link("http://www.wikicreole.org/", t("Visit the WikiCreole website")))), dut.document("[[http://www.wikicreole.org/|Visit the WikiCreole website]]"));
    }

    @Test public void
    imagesInsideLinksMustBeSupported() {
        assertEquals(document(p(link("link", image("image"), t(" inside link")))), dut.document("[[link|{{image}} inside link]]"));
    }

    @Test public void
    freeStandingURLsShouldBeDetectedAndTurnedIntoLinks() {
        assertEquals(document(p(link("http://www.rawlink.org/"), t(", "), link("http://www.another.rawlink.org"))), dut.document("http://www.rawlink.org/, http://www.another.rawlink.org"));
    }

    @Test public void
    interwikiLink() {
        assertEquals(document(p(link("Ohana:WikiFamily"))), dut.document("[[Ohana:WikiFamily]]"));
    }
}
