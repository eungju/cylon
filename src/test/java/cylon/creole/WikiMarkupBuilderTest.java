package cylon.creole;

import static org.junit.Assert.*;

import cylon.creole.WikiMarkupBuilder;
import org.junit.Before;
import org.junit.Test;


public class WikiMarkupBuilderTest {
	private WikiMarkupBuilder builder;

	@Before public void beforeEach() {
		builder = new WikiMarkupBuilder();
	}

	@Test public void isOnEmptyLine() {
		assertTrue(builder.isOnEmptyLine());
		builder.newline(true);
		assertTrue(builder.isOnEmptyLine());
		builder.emit(" ");
		assertTrue(builder.isOnEmptyLine());
		builder.emit("a");
		assertFalse(builder.isOnEmptyLine());
		builder.newline();
		assertTrue(builder.isOnEmptyLine());
	}
}
