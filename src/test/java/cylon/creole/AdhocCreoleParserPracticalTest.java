package cylon.creole;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import cylon.html.HtmlRenderer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AdhocCreoleParserPracticalTest {
	private AdhocCreoleParser dut;

	@Before public void beforeEach() {
		dut = new AdhocCreoleParser();
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
        dut.document(loadContent("creole1.0test.txt")).accept(r);
        assertEquals(loadContent("creole1.0test.html"), r.getResult());
    }

	@Test(timeout=1000) public void shouldBeFast() throws IOException {
        for (int i = 0; i < 10; i++) {
		    dut.document(loadContent("creole1.0test.txt"));
        }
	}

	@Test public void preStackoverflow() {
		dut.document(loadContent("error-pre-stackoverflow.txt"));
	}
}
