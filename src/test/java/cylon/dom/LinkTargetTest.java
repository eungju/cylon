package cylon.dom;

import org.junit.Test;

import static org.junit.Assert.*;


public class LinkTargetTest {
	@Test public void page() {
		LinkTarget uri = new LinkTarget("Page Name");
		assertTrue(uri.isPage());
		assertFalse(uri.isNeighborWiki());
		assertEquals("Page Name", uri.getPageName());
	}

	@Test public void pageOnNeighborWiki() {
		LinkTarget uri = new LinkTarget("wikiid::Page Name");
		assertTrue(uri.isPage());
		assertTrue(uri.isNeighborWiki());
		assertEquals("Page Name", uri.getPageName());
		assertEquals("wikiid", uri.getNeighborWikiId());
	}

	@Test public void url() {
		LinkTarget uri = new LinkTarget("http://naver.com/search?q=dadf&value=%20+");
		assertTrue(uri.isUrl());
		assertTrue(uri.isSafeUrl());
		assertEquals("http://naver.com/search?q=dadf&value=%20+", uri.getUrl());
	}

	@Test public void isAllowedUrl() {
		assertTrue(new LinkTarget("http://").isSafeUrl());
		assertTrue(new LinkTarget("https://").isSafeUrl());
		assertTrue(new LinkTarget("ftp://").isSafeUrl());
		assertTrue(new LinkTarget("mailto:fred@flinstones.org").isSafeUrl());
		assertFalse(new LinkTarget("mms://").isSafeUrl());
	}
	
	@Test public void unsafeUrl() {
		LinkTarget uri = new LinkTarget("javascript:window.close()");
		assertTrue(uri.isUrl());
		assertFalse(uri.isSafeUrl());
	}

	@Test public void attachment() {
		LinkTarget uri = new LinkTarget("attachment:FileName.ext");
		assertTrue(uri.isAttachment());
		assertFalse(uri.isUrl());
		assertEquals("FileName.ext", uri.getAttachmentName());
	}

	@Test public void invalidTarget() {
		LinkTarget uri = new LinkTarget("~");
		assertTrue(uri.isUrl());
		assertFalse(uri.isSafeUrl());
		assertEquals("~", uri.getUrl());
	}
}
