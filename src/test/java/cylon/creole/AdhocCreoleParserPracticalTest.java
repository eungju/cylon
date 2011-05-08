package cylon.creole;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AdhocCreoleParserPracticalTest {
	AdhocCreoleParser parser;

	@Before public void beforeEach() {
		parser = new AdhocCreoleParser();
	}

	String loadContent(String fileName) {
		try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(getClass().getPackage().getName().replaceAll("\\.", "/") + "/" + fileName);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n;
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
            return new String(output.toByteArray());
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
