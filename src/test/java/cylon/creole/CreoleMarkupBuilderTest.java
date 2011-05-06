package cylon.creole;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CreoleMarkupBuilderTest {
	private CreoleMarkupBuilder builder;

	@Before public void beforeEach() {
		builder = new CreoleMarkupBuilder();
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
