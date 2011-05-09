package cylon.creole;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import cylon.html.HtmlRenderer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class AdhocCreoleParserPracticalTest {
	AdhocCreoleParser parser;

	@Before public void beforeEach() {
		parser = new AdhocCreoleParser();
	}

	String loadContent(String fileName) {
		try {
            return Resources.toString(Resources.getResource(getClass().getPackage().getName().replaceAll("\\.", "/") + "/" + fileName), Charsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot load " + fileName, e);
		}
	}

    @Test public void standardTestCase() throws IOException {
        HtmlRenderer r = new HtmlRenderer(true);
        parser.document(loadContent("creole1.0test.txt")).accept(r);
        System.out.println(r.asString());
    }

	@Test(timeout=1000) public void shouldBeFast() throws IOException {
        for (int i = 0; i < 10; i++) {
		    parser.document(loadContent("creole1.0test.txt"));
        }
	}

	@Test public void preStackoverflow() {
		parser.document(loadContent("error-pre-stackoverflow.txt"));
	}
}
