package cylon.creole;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class PracticalPlainWikiParserTest {
	PlainWikiParser parser;

	@Before public void beforeEach() {
		parser = new PlainWikiParser();
	}

	String loadContent(String fileName) {
		try {
			return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/" + fileName));
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot load ", e);
		}
	}

	@Test(timeout=1000) public void shouldBeFast() throws IOException {
        for (int i = 0; i < 10; i++) {
		    parser.document(loadContent("Creole10.txt"));
        }
	}

	@Test public void preStackoverflow() {
		parser.document(loadContent("error-pre-stackoverflow.txt"));
	}
}
