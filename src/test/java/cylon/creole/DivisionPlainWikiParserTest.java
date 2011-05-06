package cylon.creole;

import static org.junit.Assert.*;

import cylon.creole.PlainWikiParser;
import org.junit.Before;
import org.junit.Test;

import cylon.dom.DomBuilder;

public class DivisionPlainWikiParserTest extends DomBuilder {
	PlainWikiParser parser;

	@Before public void beforeEach() {
		parser = new PlainWikiParser();
	}
	
	@Test public void noteHasTexts() {
		assertEquals(document(note(t("text\n"), b("bold"), t("\n"))), parser.document("[[[note\ntext\n**bold**\n]]]"));
	}
	
	@Test public void tipHasTexts() {
		assertEquals(document(tip(t("text\n"), b("bold"), t("\n"))), parser.document("[[[tip\ntext\n**bold**\n]]]"));
	}
}
