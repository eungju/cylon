package cylon.dom;

import org.junit.Test;

import static org.junit.Assert.*;

public class CitationTest {
	@Test public void canHaveLabel() {
		Citation dut = new Citation(new Unformatted("a"), "label");
		assertTrue(dut.hasLabel());
		assertEquals("label", dut.getLabel());
	}
	
	@Test public void plainExplanation() {
		Unformatted explanation = new Unformatted("blah");
		Citation dut = new Citation(explanation);
		assertTrue(dut.isPlain());
		assertFalse(dut.isLink());
		assertEquals(explanation, dut.getPlainExplanation());
	}
	
	@Test public void linkExplanation() {
		Link explanation = new Link("http://localhost");
		Citation dut = new Citation(explanation);
		assertFalse(dut.isPlain());
		assertTrue(dut.isLink());
		assertEquals(explanation, dut.getLinkExplanation());
	}
}
