package cylon.html;

import cylon.dom.DomBuilder;
import cylon.dom.Text;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.apache.commons.lang.StringEscapeUtils.*;
import static org.junit.Assert.*;

@RunWith(JMock.class)
public class HtmlRendererTest extends DomBuilder {
	private Mockery mockery = new JUnit4Mockery();
	private HtmlRenderer visitor;

	@Before public void beforeEach() {
		visitor = new HtmlRenderer();
	}

	@Test public void document() {
		document(p(), hr()).accept(visitor);
		assertEquals("<p></p><hr />", visitor.asString());
	}

    @Test public void heading1() {
        h1("a").accept(visitor);
        assertEquals("<h1>a</h1>", visitor.asString());
    }


	@Test public void horizontalLine() {
		hr().accept(visitor);
		assertEquals("<hr />", visitor.asString());
	}	

	@Test public void paragraph() {
		p(t("a"), i("b"), b("c")).accept(visitor);
		assertEquals("<p>a<em>b</em><strong>c</strong></p>", visitor.asString());
	}

	@Test public void paragraphCanHasIndentOne() {
		p(1, t("a"), i("b"), b("c")).accept(visitor);
		assertEquals("<div style=\"margin-left: 2em;\"><p>a<em>b</em><strong>c</strong></p></div>", visitor.asString());
	}

	@Test public void unformatted() {
		t("<a>").accept(visitor);
		assertEquals("&lt;a&gt;", visitor.asString());
	}

	@Test public void italic() {
		i("a").accept(visitor);
		assertEquals("<em>a</em>", visitor.asString());
	}

	@Test public void bold() {
		b("a").accept(visitor);
		assertEquals("<strong>a</strong>", visitor.asString());
	}

	@Test public void superscript() {
		sup("a").accept(visitor);
		assertEquals("<sup>a</sup>", visitor.asString());
	}

	@Test public void subscript() {
		sub("a").accept(visitor);
		assertEquals("<sub>a</sub>", visitor.asString());
	}

	@Test public void strike() {
		s("a").accept(visitor);
		assertEquals("<del>a</del>", visitor.asString());
	}

	@Test public void underline() {
		u("a").accept(visitor);
		assertEquals("<u>a</u>", visitor.asString());
	}	

	@Test public void tableHeader() {
		table(tr(th(t("a")))).accept(visitor);
		assertEquals("<table class=\"table\" border=\"1\" cellspacing=\"0\"><tr><th scope=\"row\">a</th></tr></table>", visitor.asString());
	}	

	@Test public void tableCell() {
		table(tr(td(t("a")))).accept(visitor);
		assertEquals("<table class=\"table\" border=\"1\" cellspacing=\"0\"><tr><td>a</td></tr></table>", visitor.asString());
	}

	@Test public void urlLink() {
		final String url = "http://naver.com/w?x=y&z";
		link(url).accept(visitor);
		assertEquals("<a href=\"" + escapeHtml(url) + "\">" + escapeHtml(url) +  "</a>", visitor.asString());
	}

	@Test public void urlLinkWithDescription() {
		final String url = "http://naver.com/w?x=y&z";
		link(url, t("D")).accept(visitor);
		assertEquals("<a href=\"" + escapeHtml(url) + "\">D</a>", visitor.asString());
	}
		
	@Test public void imageUrlTarget() {
		image("http://a").accept(visitor);
		assertEquals("<img src=\"http://a\" />", visitor.asString());
	}

	@Test public void imageWithAlt() {
		image("http://a", "b").accept(visitor);
		assertEquals("<img src=\"http://a\" alt=\"b\" />", visitor.asString());
	}

	@Test public void unorderedList() {
		ul(li()).accept(visitor);
		assertEquals("<ul><li></li></ul>", visitor.asString());
	}

	@Test public void orderedList() {
		ol(li()).accept(visitor);
		assertEquals("<ol><li></li></ol>", visitor.asString());
	}

	@Test public void textListItem() {
		li(t("a")).accept(visitor);
		assertEquals("<li>a</li>", visitor.asString());
	}

	@Test public void listListItem() {
		li(new Text[]{}, ol()).accept(visitor);
		assertEquals("<li><ol></ol></li>", visitor.asString());
	}

	//*a
	//**b
	@Test public void listCanBeNested() {
		ul(
				li(texts(t("a")),
						ul(li(t("b"))))
		).accept(visitor);
		StringBuilder actual = new StringBuilder();
		actual.append("<ul>");
		actual.append(	"<li>a");
		actual.append(		"<ul>");
		actual.append(			"<li>b</li>");
		actual.append(		"</ul>");
		actual.append(	"</li>");
		actual.append("</ul>");
		assertEquals(actual.toString(), visitor.asString());
	}

	@Test public void preformatted() {
		pre("<a>").accept(visitor);
		assertEquals("<pre>&lt;a&gt;</pre>", visitor.asString());
	}

	@Test public void preformattedWithSyntaxHighlighter() {
		pre("syntax java", "<a>").accept(visitor);
		assertEquals("<pre name=\"code\" class=\"java\">&lt;a&gt;</pre>", visitor.asString());
	}

	@Test public void preformattedWithUnknown() {
		pre("unknown", "<a>").accept(visitor);
		assertEquals("<pre>&lt;a&gt;</pre>", visitor.asString());
	}

	@Test public void code() {
		code("<a>").accept(visitor);
		assertEquals("<code>&lt;a&gt;</code>", visitor.asString());
	}

	@Test public void forcedLinebreak() {
		br().accept(visitor);
		assertEquals("<br />", visitor.asString());		
	}
	
	@Test public void characterEntity() {
		p(t("&quot;안녕하세요.&quot;")).accept(visitor);
		assertEquals("<p>&amp;quot;안녕하세요.&amp;quot;</p>", visitor.asString());
	}
}
