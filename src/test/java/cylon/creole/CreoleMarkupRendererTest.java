package cylon.creole;

import cylon.dom.DomBuilder;
import cylon.dom.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CreoleMarkupRendererTest extends DomBuilder {
	private CreoleMarkupRenderer visitor;

	String markup(Node root) {
		CreoleMarkupRenderer v = new CreoleMarkupRenderer();
		root.accept(v);
		return v.asString();
	}

	@Before public void beforeEach() {
		visitor = new CreoleMarkupRenderer();
	}

	@Test public void document() {
		document(p()).accept(visitor);
		assertEquals("\n", visitor.asString());
	}

	@Test public void headings() {
		document(h1("a"), h2("b"), h3("c"), h4("d"), h5("e")).accept(visitor);
		assertEquals("=a=\n==b==\n===c===\n====d====\n=====e=====\n", visitor.asString());
	}

	@Test public void horizontalLine() {
		hr().accept(visitor);
		assertEquals("----\n", visitor.asString());
	}
	
	@Test public void paragraph() {
		p(t("a"), i("b"), b("c")).accept(visitor);
		assertEquals("a//b//**c**\n\n", visitor.asString());
	}
	
	@Test public void paragraphCanHasIndentOne() {
		p(1, t("a"), i("b"), b("c")).accept(visitor);
		assertEquals(">a//b//**c**\n\n", visitor.asString());
	}

	@Test public void unformatted() {
		t("abc").accept(visitor);
		assertEquals("abc", visitor.asString());
	}
	
	@Test public void escapeUnformatted() {
		assertEquals("a*~*", markup(t("a**")));
		assertEquals("a/~/", markup(t("a//")));
	}
	
	@Test public void escapeUnformattedTilt() {
		assertEquals("a~", markup(t("a~")));
		assertEquals("a~~*", markup(t("a~*")));
	}
	
	@Test public void escapeUnformattedAfterUnformatted() {
		//앞에 마크업이 아닌 이상은 뒤에 오는 문자가 이스케이핑 되어야 한다.
		assertEquals("a*~*\n\n", markup(p(t("a*"), t("*"))));
		assertEquals("a*~**\n\n", markup(p(t("a*"), t("**"))));
		assertEquals("a*~**~*\n\n", markup(p(t("a*"), t("***"))));
	}

	@Test public void escapeUnformattedAfterBlockMarkup() {
		assertEquals("~*a*~*\n\n", markup(p(t("*a**"))));
		assertEquals("~[[[~]]\n\n", markup(p(t("["), link("~"))));
		assertEquals(" ~#\n\n", markup(p(t(" #"))));
		assertEquals(" ~*\n\n", markup(p(t(" *"))));
	}
	
	@Test public void escapeUnformattedBeforeInlineMarkup() {
		assertEquals("a~***b**\n\n", markup(p(t("a*"), b("b"))));
		assertEquals("a~///b//\n\n", markup(p(t("a/"), i("b"))));
		assertEquals("a~^^^b^^\n\n", markup(p(t("a^"), sup("b"))));
		assertEquals("a~,,,b,,\n\n", markup(p(t("a,"), sub("b"))));
		assertEquals("a~---b--\n\n", markup(p(t("a-"), s("b"))));
		assertEquals("a~___b__\n\n", markup(p(t("a_"), u("b"))));
		assertEquals("a~\\\\\\\n\n", markup(p(t("a\\"), br())));
		assertEquals("a~[[[b]]\n\n", markup(p(t("a["), link("b"))));
		assertEquals("a~{{{http://b}}\n\n", markup(p(t("a{"), image("http://b"))));
		assertEquals("a~{{{{b}}}\n\n", markup(p(t("a{"), code("b"))));
	}
	
	@Test public void escapeUnformattedAfterInlineMarkup() {
		//마크업 직후의 문자는 이스케이핑 하지 않아도 되지만 직적이 마크업이었는지 판단하기 어려우므로 그냥 이스케이핑 한다. 
		assertEquals("**a**~*\n\n", markup(p(b("a"), t("*"))));
		assertEquals("**a**~**\n\n", markup(p(b("a"), t("**"))));
	}

	@Test public void italic() {
		i("a").accept(visitor);
		assertEquals("//a//", visitor.asString());
	}
	
	@Test public void bold() {
		b("a").accept(visitor);
		assertEquals("**a**", visitor.asString());
	}

	@Test public void superscript() {
		sup("a").accept(visitor);
		assertEquals("^^a^^", visitor.asString());
	}

	@Test public void subscript() {
		sub("a").accept(visitor);
		assertEquals(",,a,,", visitor.asString());
	}
	
	@Test public void strike() {
		s("a").accept(visitor);
		assertEquals("--a--", visitor.asString());
	}
	
	@Test public void underline() {
		u("a").accept(visitor);
		assertEquals("__a__", visitor.asString());
	}

	@Test public void tableHeader() {
		table(tr(th(t("a")))).accept(visitor);
		assertEquals("|=a|\n", visitor.asString());
	}

	@Test public void tableCell() {
		table(tr(td(t("a")))).accept(visitor);
		assertEquals("|a|\n", visitor.asString());
	}

	@Test public void tableWithMultipleRowsAndCells() {
		table(tr(td(t("a")),td(t("a")),td(t("a"))), tr(td(t("a")),td(t("a")),td(t("a")))).accept(visitor);
		assertEquals("|a|a|a|\n|a|a|a|\n", visitor.asString());
	}

	@Test public void preformattedContainsText() {
		pre("a\n").accept(visitor);
		assertEquals("{{{\na\n}}}\n", visitor.asString());
	}
	
	@Test public void preformattedCloseSyntaxMustBeFollowedByLineFeed() {
		pre("a").accept(visitor);
		assertEquals("{{{\na\n}}}\n", visitor.asString());
	}

	@Test public void preformattedCanBeEmpty() {
		pre("").accept(visitor);
		assertEquals("{{{\n}}}\n", visitor.asString());
	}

	@Test public void preformattedEscape() {
		pre("}}}").accept(visitor);
		assertEquals("{{{\n~}}}\n}}}\n", visitor.asString());
	}

	@Test public void preformattedCanHaveInterpreter() {
		// FIXME: is it supported by smart editor?
		pre("syntax java", "a").accept(visitor);
		assertEquals("{{{#!syntax java\na\n}}}\n", visitor.asString());
	}

	@Test public void imageWithoutAlternative() {
		image("uri").accept(visitor);
		assertEquals("{{uri}}", visitor.asString());
	}

	@Test public void imageWithAlternative() {
		image("uri", "alt").accept(visitor);
		assertEquals("{{uri|alt}}", visitor.asString());
	}

	@Test public void whenImageIsAttachment() {
		image("attachment:filename.ext", "alt").accept(visitor);
		assertEquals("{{attachment:filename.ext|alt}}", visitor.asString());
	}

	@Test public void forcedLineBreakMarkupIsTwoBackslashes() {
		p(t("a"), br(), t("b")).accept(visitor);
		assertEquals("a\\\\b\n\n", visitor.asString());
	}
	
	@Test public void codeMarkupIsThreeBraces() {
		code("**a**").accept(visitor);
		assertEquals("{{{**a**}}}", visitor.asString());
	}

	@Test public void unorderedListStartsWithStars() {
		ul(li(t("a"))).accept(visitor);
		assertEquals("*a\n", visitor.asString());
	}

	@Test public void orderedListStartsWithShaps() {
		ol(li(t("a"))).accept(visitor);
		assertEquals("#a\n", visitor.asString());
	}

	@Test public void listContainsOneOrMoreItems() {
		ul(li(t("a")), li(t("b"))).accept(visitor);
		assertEquals("*a\n*b\n", visitor.asString());
	}

	@Test public void listCanBeMixed() {
		ul(
			li(texts(t("a")), ol(
					li(t("b"))))
		).accept(visitor);
		assertEquals("*a\n*#b\n", visitor.asString());
	}

	@Test public void listCanBeNested() {
		ul(
				li(texts(t("a")), ul(li(t("b"))))).accept(visitor);
		assertEquals("*a\n**b\n", visitor.asString());
	}

	@Test public void listAllowsDeepNesting() {
		ul(
				li(texts(t("a")), ul(
						li(texts(), ul(li(t("b")))))),
				li(t("c"))).accept(visitor);
		assertEquals("*a\n***b\n*c\n", visitor.asString());
	}

	@Test public void listItemContainsFormattedText() {
		ul(li(i("a"))).accept(visitor);
		assertEquals("*//a//\n", visitor.asString());
	}
	
	@Test public void linkWithoutDescription() {
		link("page name").accept(visitor);
		assertEquals("[[page name]]", visitor.asString());
	}

	@Test public void linkWithDescription() {
		link("page name", t("desc")).accept(visitor);
		assertEquals("[[page name|desc]]", visitor.asString());
	}

	@Test public void linkDescriptionCanBeFormatted() {
		link("page name", b("desc")).accept(visitor);
		assertEquals("[[page name|**desc**]]", visitor.asString());
	}
	
	@Test public void urlLink() {
		link("http://a").accept(visitor);
		assertEquals("[[http://a]]", visitor.asString());
	}

	@Test public void urlLinkWithDescription() {
		link("http://a", b("desc")).accept(visitor);
		assertEquals("[[http://a|**desc**]]", visitor.asString());
	}
	
	@Test public void characterEntity() {
		assertEquals("&quot;안녕하세요.&quot;\n\n", markup(p(t("&quot;안녕하세요.&quot;"))));
	}
}
