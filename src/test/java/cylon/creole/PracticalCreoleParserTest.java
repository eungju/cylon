package cylon.creole;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import cylon.html.HtmlRenderer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public abstract class PracticalCreoleParserTest {
    protected CreoleParser dut;

    @Ignore
    public static class AdhocParserTest extends PracticalCreoleParserTest {
        @Before
        public void beforeEach() {
            dut = new AdhocCreoleParser();
        }
    }

    public static class LineParserTest extends PracticalCreoleParserTest {
        @Before
        public void beforeEach() {
            dut = new LineCreoleParser();
        }
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
