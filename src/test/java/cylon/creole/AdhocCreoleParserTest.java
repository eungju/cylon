package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdhocCreoleParserTest extends DomBuilder implements WikiParserSpec {
	AdhocCreoleParser parser;

	@Before public void beforeEach() {
		parser = new AdhocCreoleParser();
	}

	@Test public void documentCanBeEmpty() {
		assertEquals(document(), parser.document(""));
	}

	@Test public void documentShouldContainBlocks() {
		assertEquals(document(
				hr(),
				h1("a"),
				pre("b\n"),
				ul(li(t("c"))),
				ol(li(t("d"))),
				table(tr(td(t("e")))),
				p(t("f"))),
				parser.document("----\n" + "=a\n" + "{{{\nb\n}}}\n" + "*c\n\n" + "#d\n" + "|e|\n" + "f"));
	}

	@Test public void listTypeIsDeterminedByTheTypeOfTheFirstListItem() {
		assertEquals(document(ul(
				li(t("a")),
				li(t("b")))),
				parser.document("*a\n#b\n"));
	}

	@Test public void listShouldStartWithFirstLevel() {
		assertEquals(document(p(t("##a"))), parser.document("##a\n"));
	}

	@Test public void listsCanStartWithLeadingSpaces() {
		assertEquals(document(ol(
				li(t("a")),
				li(t("b")))),
				parser.document(" #a\n\t#b"));
	}

	@Test public void headingStartsWithEquals() {
		assertEquals(document(h1("A")), parser.document("=A"));
	}

	@Test public void headingCanEndWithEquals() {
		assertEquals(document(h2("A")), parser.document("==A=="));
	}

	@Test public void unorderedListStartsWithStars() {
		assertEquals(document(ul(li(t("a")))), parser.document("*a"));
	}

	@Test public void orderedListStartsWithShaps() {
		assertEquals(document(ol(li(t("a")))), parser.document("#a"));
	}

	@Test public void listContainsOneOrMoreItems() {
		assertEquals(document(ul(
				li(t("a")),
				li(t("b")))),
				parser.document("*a\n*b"));
	}

	@Test public void listCanBeNested() {
		assertEquals(document(ul(
				li(texts(t("a")), ul(
						li(t("b")))))),
				parser.document("*a\n**b"));
	}

	@Test public void listCanBeMixed() {
		assertEquals(document(ul(
				li(texts(t("a")), ol(
						li(t("b")))))),
				parser.document("*a\n*#b"));
	}

	@Test public void listAllowsMultiDepthIndentAndDedent() {
		assertEquals(document(ul(
				li(texts(t("a")), ul(
						li(texts(), ul(li(t("b")))))),
				li(t("c")))),
				parser.document("*a\n***b\n*c"));
	}

	@Test public void listAllowsRepeatedMultiDepthIndentAndDedent() {
		assertEquals(document(ul(
				li(texts(t("a")), ul(
						li(texts(), ul(
								li(t("b")))))),
				li(texts(t("c")), ul(
						li(texts(), ul(
								li(t("d")))))))),
			parser.document("*a\n***b\n*c\n***d"));
	}

	@Test public void listItemContainsFormattedText() {
		assertEquals(document(ul(li(i("a")))), parser.document("*//a//"));
	}

	@Test public void tablesHaveAtLeastOneRow() {
		assertEquals(document(table(tr(td(t("a"))))),
				parser.document("|a|\n"));
	}

	@Test public void tablesCanContainTableHeader() {
		assertEquals(document(table(tr(th(t("a"))))),
				parser.document("|=a|\n"));
	}

	@Test public void tablesCanHaveMultipleRows() {
		assertEquals(document(table(
				tr(td(t("a"))),
				tr(td(t("a"))))),
				parser.document("|a|\n|a|\n"));
	}

	@Test public void tablesCanHaveMultipleCells() {
		assertEquals(document(table(tr(
				td(t("a")),
				td(t("b"))))),
				parser.document("|a|b|\n"));
	}

	@Test public void tablesAreSeparatedByEmptyLine() {
		assertEquals(document(
				table(tr(td(t("a")))),
				table(tr(td(t("b"))), tr(td(t("c"))))),
				parser.document("|a|\n\n|b|\n|c|\n"));
	}

	@Test public void tableCellCanHaveLink() {
		assertEquals(
				document(table(tr(td(t("a")), td(link("Page Name", t("desc")))))),
				parser.document("|a|[[Page Name|desc]]|\n"));
	}

	@Test public void tableCellCanHaveImage() {
		assertEquals(
				document(table(tr(td(t("a")), td(image("uri", "alt"))))),
				parser.document("|a|{{uri|alt=alt}}|\n"));
	}

	@Test public void tableCellCanHaveCode() {
		assertEquals(
				document(table(tr(td(t("a")), td(code("ab | cd"))))), 
				parser.document("|a|{{{ab | cd}}}|\n"));
	}

	@Test public void tableCellCanHaveEscape() {
		assertEquals(
				document(table(tr(td(t("a")), td(t("a"), t("|"))))), 
				parser.document("|a|a~||\n"));
	}

	@Test public void paragraphsHaveAtLeastOneLine() {
		assertEquals(document(p(t("a"))), parser.document("a\n"));
	}

	@Test public void paragraphCanHaveMultipleLines() {
		assertEquals(document(p(t("a"), t("b"))),
				parser.document("a\nb"));
	}

	@Test public void paragraphCanHaveIndentOne() {
		assertEquals(document(p(1, t("This is an indnted.")))
				, parser.document(">This is an indnted."));
	}

	@Test public void paragraphCanHaveIndentTwo() {
		assertEquals(document(p(2, t("This is an indnted.")))
				, parser.document(">>This is an indnted."));
	}	

	@Test public void paragraphCanHaveMultiIndent() {
		assertEquals(document(p(1, t("one indented")), p(2, t("two indented")))
				, parser.document(">one indented\n>>two indented"));
	}

	@Test public void paragraphCanHaveMultiIndentContainsText() {
		assertEquals(document(p(1, t("one indented"), t("text")), p(2, t("two indented")))
				, parser.document(">one indented\ntext\n>>two indented"));
	}	

	@Test public void paragraphsAreSeparatedByEmptyLine() {
		assertEquals(document(
				p(t("a")),
				p(t("b"))),
				parser.document("a\n\nb"));
	}

	@Test public void preformattedContainsText() {
		assertEquals(document(pre("a\n")), parser.document("{{{\na\n}}}"));
	}

	@Test public void preformattedCanBeEmpty() {
		assertEquals(document(pre("")), parser.document("{{{\n}}}"));
	}

	@Test public void preformattedShouldNotEatMarkup() {
		assertEquals(document(pre("----\n")), parser.document("{{{\n----\n}}}"));
		assertEquals(document(pre("|a|\n")), parser.document("{{{\n|a|\n}}}"));
	}

	@Test public void preformattedCanHaveInterpreter() {
		assertEquals(document(pre("syntax java", "a\n")), parser.document("{{{#!syntax java\na\n}}}"));
	}
	
	/** inline element test */
	@Test public void boldMarkupIsTwoStars() {
		assertEquals(p(b("a")), parser.formattedText("**a**"));
	}

	@Test public void italicMarkupIsTwoSlashes() {
		assertEquals(p(i("a")), parser.formattedText("//a//"));
	}

	@Test public void underlineMarkupIsTwoUnderbars() {
		assertEquals(p(u("a")), parser.formattedText("__a__"));
	}

	@Test public void strikeMarkupIsTwoDashes() {
		assertEquals(p(s("a")), parser.formattedText("--a--"));
	}

	@Test public void superscriptMarkupIsTwoCarets() {
		assertEquals(p(sup(t("a"))), parser.formattedText("^^a^^"));
	}

	@Test public void subscriptMarkupIsTwoComas() {
		assertEquals(p(sub(t("a"))), parser.formattedText(",,a,,"));
	}

	@Test public void codeMarkupIsThreeBraces() {
		assertEquals(p(code("**a**")), parser.formattedText("{{{**a**}}}")); 
	}

	@Test public void forcedLineBreakMarkupIsTwoBackslashes() {
		assertEquals(p(t("a"), br(), t("b")), parser.formattedText("a\\\\b")); 
	}

	@Test public void escapeMarkupIsTilde() {
		assertEquals(p(t("*"), t("*not bold"), t("*"), t("*")), parser.formattedText("~**not bold~**"));
	}

	@Test public void escapeMatchesWithOnlyOneCharacter() {
		assertEquals(p(t("*"), b("a")), parser.formattedText("~***a**"));
	}

	@Test public void formattedTextCanBeMixed() {
		assertEquals(p(t("a"), b("b"), t("c")), parser.formattedText("a**b**c"));
	}

	@Test public void boldShouldBeNonGreedy() {
		assertEquals(p(b("a"), t("b"), b("c")), parser.formattedText("**a**b**c**"));
	}

	@Test public void boldExtendsAcrossLineBreak() {
		assertEquals(p(b("a\nb\nc")), parser.formattedText("**a\nb\nc**"));
	}

	@Test public void italicShouldBeNonGreedy() {
		assertEquals(p(i("a"), t("b"), i("c")), parser.formattedText("//a//b//c//"));
	}

	@Test public void boldCanNestItalic() {
		assertEquals(p(b(t("a"), i("b"), t("c"))), parser.formattedText("**a//b//c**"));
	}

	@Test public void italicCanNestBold() {
		assertEquals(p(i(t("a"), b("b"), t("c"))), parser.formattedText("//a**b**c//"));
	}

	@Test public void linkWithoutDescription() {
		assertEquals(p(link("Page Name")), parser.formattedText("[[Page Name]]"));
	}

	@Test public void linkWithDescription() {
		assertEquals(p(link("Page Name", t("desc"))), parser.formattedText("[[Page Name|desc]]"));
	}
	
	@Test public void linkWithDescriptionWithWhiteSpace() {
		assertEquals(p(link("Page Name", t("desc"))), parser.formattedText("[[Page Name | desc]]"));
	}
	

	@Test public void linkDescriptionCanBeFormatted() {
		assertEquals(p(link("Page Name", b("desc"))), parser.formattedText("[[Page Name|**desc**]]"));
	}

	@Test public void urlLink() {
		assertEquals(p(link("http://a")), parser.formattedText("[[http://a]]"));
	}

	@Test public void imageWithoutAlternative() {
		assertEquals(p(image("uri")), parser.formattedText("{{uri}}"));
	}

	@Test public void imageWithAlternative() {
		assertEquals(p(image("uri", "alt")), parser.formattedText("{{uri|alt=alt}}"));
	}
	
	@Test public void paragraphsContainsFormattedText() {
		assertEquals(document(p(t("a"), b("b"), i("c"))), parser.document("a**b**//c//"));
	}

	@Test public void tableFollowedByHeading() {
		assertEquals(document(table(tr(td(t("a")))), h1("h1")), parser.document("|a|\n=h1"));
	}

	@Test public void paragraphFollowedByHeading1() {
		assertEquals(document(p(t("a")), h1("h1")), parser.document("a\n=h1"));
	}
	
	@Test public void paragraphFollowedByHeading2() {
		assertEquals(document(p(t("a")), h1("h1"), p(t("b")), h2("h2"), p(t("c"))), parser.document("a\n=h1=\nb\n\n==h2==\nc"));
	}
	
	@Test public void paragraphHaveHeadings() {
		String html = "=h1=\na\n"
					+ "==h2==\nb\n"
					+ "===h3===\nc\n"
					+ "====h4====\nd\n"
					+ "=====h5=====\ne";
		assertEquals(document(h1("h1"), p(t("a")), h2("h2"), p(t("b")), h3("h3"), p(t("c"))
				, h4("h4"), p(t("d")), h5("h5"), p(t("e"))
		), parser.document(html));
	}

	@Test public void paragraphFollowedByHorizontalRule() {
		assertEquals(document(p(t("a")), hr()), parser.document("a\n----"));
	}

	@Test public void tablesContainFormattedText() {
		assertEquals(document(table(tr(td(t("a"), b("b"), i("c"))))), parser.document("|a**b**//c//|"));
	}

	@Test public void listsFollowedByParagraph() {
		assertEquals(document(ul(li(t("a"))), p(t("b"))), parser.document("*a\nb"));
	}

	@Test public void listsFollowedByTable() {
		assertEquals(document(ul(li(t("a"))), table(tr(td(t("b"))))), parser.document("*a\n|b|"));
	}

	@Test public void inlineFormattingsCanBeClosedByEndOfParagraph() {
		assertEquals(
				document(p(i(t("a"), u()))),
				parser.document("//a__"));
	}

	@Test public void inlineFormattingsExtendsAcrossLineBreak() {
		assertEquals(
				document(p(i(t("a"), u("b")))),
				parser.document("//a__\nb"));
	}

	@Test public void inlineFormattingsCanBeClosedByEndOfTableCell() {
		assertEquals(
				document(table(tr(td(i(t("a"), u()))))),
				parser.document("|//a__|"));
	}

	@Test public void inlineFormattingsCanBeClosedByEndOfLink() {
		assertEquals(
				document(p(link("uri", i("a")))),
				parser.document("[[uri|//a]]"));
	}
	
	@Test public void characterEntity() {
		assertEquals(
				document(p(t("&quot;안녕하세요.&quot;"))), 
				parser.document("&quot;안녕하세요.&quot;"));
	}
}
