package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdhocCreoleParserDivisionTest extends DomBuilder {
	AdhocCreoleParser parser;

	@Before public void beforeEach() {
		parser = new AdhocCreoleParser();
	}
	
	@Test public void noteHasTexts() {
		assertEquals(document(note(t("text\n"), b("bold"), t("\n"))), parser.document("[[[note\ntext\n**bold**\n]]]"));
	}
	
	@Test public void tipHasTexts() {
		assertEquals(document(tip(t("text\n"), b("bold"), t("\n"))), parser.document("[[[tip\ntext\n**bold**\n]]]"));
	}
}
