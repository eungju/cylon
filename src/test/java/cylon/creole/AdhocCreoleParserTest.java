package cylon.creole;

import cylon.dom.DomBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdhocCreoleParserTest extends DomBuilder {
	private AdhocCreoleParser dut;

	@Before public void beforeEach() {
		dut = new AdhocCreoleParser();
	}

	@Test public void documentCanBeEmpty() {
		assertEquals(document(), dut.document(""));
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
				dut.document("----\n" + "=a\n" + "{{{\nb\n}}}\n" + "*c\n\n" + "#d\n" + "|e|\n" + "f"));
	}

	@Test public void listTypeIsDeterminedByTheTypeOfTheFirstListItem() {
		assertEquals(document(ul(
				li(t("a")),
				li(t("b")))),
				dut.document("*a\n#b\n"));
	}

	@Test public void listShouldStartWithFirstLevel() {
		assertEquals(document(p(t("##a"))), dut.document("##a\n"));
	}

	@Test public void listsCanStartWithLeadingSpaces() {
		assertEquals(document(ol(
				li(t("a")),
				li(t("b")))),
				dut.document(" #a\n\t#b"));
	}

	@Test public void headingStartsWithEquals() {
		assertEquals(document(h1("A")), dut.document("=A"));
	}

	@Test public void headingCanEndWithEquals() {
		assertEquals(document(h2("A")), dut.document("==A=="));
	}

	@Test public void unorderedListStartsWithStars() {
		assertEquals(document(ul(li(t("a")))), dut.document("*a"));
	}

	@Test public void orderedListStartsWithShaps() {
		assertEquals(document(ol(li(t("a")))), dut.document("#a"));
	}

	@Test public void listContainsOneOrMoreItems() {
		assertEquals(document(ul(
				li(t("a")),
				li(t("b")))),
				dut.document("*a\n*b"));
	}

	@Test public void listCanBeNested() {
		assertEquals(document(ul(
				li(texts(t("a")), ul(
						li(t("b")))))),
				dut.document("*a\n**b"));
	}

	@Test public void listCanBeMixed() {
		assertEquals(document(ul(
				li(texts(t("a")), ol(
						li(t("b")))))),
				dut.document("*a\n*#b"));
	}

	@Test public void listAllowsMultiDepthIndentAndDedent() {
		assertEquals(document(ul(
				li(texts(t("a")), ul(
						li(texts(), ul(li(t("b")))))),
				li(t("c")))),
				dut.document("*a\n***b\n*c"));
	}

	@Test public void listAllowsRepeatedMultiDepthIndentAndDedent() {
		assertEquals(document(ul(
				li(texts(t("a")), ul(
						li(texts(), ul(
								li(t("b")))))),
				li(texts(t("c")), ul(
						li(texts(), ul(
								li(t("d")))))))),
			dut.document("*a\n***b\n*c\n***d"));
	}

	@Test public void listItemContainsFormattedText() {
		assertEquals(document(ul(li(i("a")))), dut.document("*//a//"));
	}

	@Test public void tablesHaveAtLeastOneRow() {
		assertEquals(document(table(tr(td(t("a"))))),
				dut.document("|a|\n"));
	}

	@Test public void tablesCanContainTableHeader() {
		assertEquals(document(table(tr(th(t("a"))))),
				dut.document("|=a|\n"));
	}

	@Test public void tablesCanHaveMultipleRows() {
		assertEquals(document(table(
				tr(td(t("a"))),
				tr(td(t("a"))))),
				dut.document("|a|\n|a|\n"));
	}

	@Test public void tablesCanHaveMultipleCells() {
		assertEquals(document(table(tr(
				td(t("a")),
				td(t("b"))))),
				dut.document("|a|b|\n"));
	}

	@Test public void tablesAreSeparatedByEmptyLine() {
		assertEquals(document(
				table(tr(td(t("a")))),
				table(tr(td(t("b"))), tr(td(t("c"))))),
				dut.document("|a|\n\n|b|\n|c|\n"));
	}

	@Test public void tableCellCanHaveLink() {
		assertEquals(
				document(table(tr(td(t("a")), td(link("Page Name", t("desc")))))),
				dut.document("|a|[[Page Name|desc]]|\n"));
	}

	@Test public void tableCellCanHaveImage() {
		assertEquals(
				document(table(tr(td(t("a")), td(image("uri", "alt"))))),
				dut.document("|a|{{uri|alt}}|\n"));
	}

	@Test public void tableCellCanHaveCode() {
		assertEquals(
				document(table(tr(td(t("a")), td(code("ab | cd"))))), 
				dut.document("|a|{{{ab | cd}}}|\n"));
	}

	@Test public void tableCellCanHaveEscape() {
		assertEquals(
				document(table(tr(td(t("a")), td(t("a"), t("|"))))), 
				dut.document("|a|a~||\n"));
	}

	@Test public void paragraphsHaveAtLeastOneLine() {
		assertEquals(document(p(t("a"))), dut.document("a\n"));
	}

	@Test public void paragraphCanHaveMultipleLines() {
		assertEquals(document(p(t("a"), t(" "), t("b"))),
				dut.document("a\nb"));
	}

	@Test public void paragraphCanHaveIndentOne() {
		assertEquals(document(p(1, t("This is an indnted.")))
				, dut.document(">This is an indnted."));
	}

	@Test public void paragraphCanHaveIndentTwo() {
		assertEquals(document(p(2, t("This is an indnted.")))
				, dut.document(">>This is an indnted."));
	}	

	@Test public void paragraphCanHaveMultiIndent() {
		assertEquals(document(p(1, t("one indented")), p(2, t("two indented")))
				, dut.document(">one indented\n>>two indented"));
	}

	@Test public void paragraphCanHaveMultiIndentContainsText() {
		assertEquals(document(p(1, t("one indented"), t(" "), t("text")), p(2, t("two indented")))
				, dut.document(">one indented\ntext\n>>two indented"));
	}	

	@Test public void paragraphsAreSeparatedByEmptyLine() {
		assertEquals(document(
				p(t("a")),
				p(t("b"))),
				dut.document("a\n\nb"));
	}

	@Test public void preformattedContainsText() {
		assertEquals(document(pre("a\n")), dut.document("{{{\na\n}}}"));
	}

	@Test public void preformattedCanBeEmpty() {
		assertEquals(document(pre("")), dut.document("{{{\n}}}"));
	}

	@Test public void preformattedShouldNotEatMarkup() {
		assertEquals(document(pre("----\n")), dut.document("{{{\n----\n}}}"));
		assertEquals(document(pre("|a|\n")), dut.document("{{{\n|a|\n}}}"));
	}

	@Test public void preformattedCanHaveInterpreter() {
		assertEquals(document(pre("syntax java", "a\n")), dut.document("{{{#!syntax java\na\n}}}"));
	}
	
	/** inline element test */
	@Test public void boldMarkupIsTwoStars() {
		assertEquals(p(b("a")), dut.formattedText("**a**"));
	}

	@Test public void italicMarkupIsTwoSlashes() {
		assertEquals(p(i("a")), dut.formattedText("//a//"));
	}

	@Test public void underlineMarkupIsTwoUnderbars() {
		assertEquals(p(u("a")), dut.formattedText("__a__"));
	}

	@Test public void strikeMarkupIsTwoDashes() {
		assertEquals(p(s("a")), dut.formattedText("--a--"));
	}

	@Test public void superscriptMarkupIsTwoCarets() {
		assertEquals(p(sup(t("a"))), dut.formattedText("^^a^^"));
	}

	@Test public void subscriptMarkupIsTwoComas() {
		assertEquals(p(sub(t("a"))), dut.formattedText(",,a,,"));
	}

	@Test public void codeMarkupIsThreeBraces() {
		assertEquals(p(code("**a**")), dut.formattedText("{{{**a**}}}"));
	}

	@Test public void forcedLineBreakMarkupIsTwoBackslashes() {
		assertEquals(p(t("a"), br(), t("b")), dut.formattedText("a\\\\b"));
	}

	@Test public void escapeMarkupIsTilde() {
		assertEquals(p(t("*"), t("*not bold"), t("*"), t("*")), dut.formattedText("~**not bold~**"));
	}

	@Test public void escapeMatchesWithOnlyOneCharacter() {
		assertEquals(p(t("*"), b("a")), dut.formattedText("~***a**"));
	}

	@Test public void formattedTextCanBeMixed() {
		assertEquals(p(t("a"), b("b"), t("c")), dut.formattedText("a**b**c"));
	}

	@Test public void boldShouldBeNonGreedy() {
		assertEquals(p(b("a"), t("b"), b("c")), dut.formattedText("**a**b**c**"));
	}

	@Test public void boldExtendsAcrossLineBreak() {
		assertEquals(p(b("a\nb\nc")), dut.formattedText("**a\nb\nc**"));
	}

	@Test public void italicShouldBeNonGreedy() {
		assertEquals(p(i("a"), t("b"), i("c")), dut.formattedText("//a//b//c//"));
	}

	@Test public void boldCanNestItalic() {
		assertEquals(p(b(t("a"), i("b"), t("c"))), dut.formattedText("**a//b//c**"));
	}

	@Test public void italicCanNestBold() {
		assertEquals(p(i(t("a"), b("b"), t("c"))), dut.formattedText("//a**b**c//"));
	}

	@Test public void linkWithoutDescription() {
		assertEquals(p(link("Page Name")), dut.formattedText("[[Page Name]]"));
	}

	@Test public void linkWithDescription() {
		assertEquals(p(link("Page Name", t("desc"))), dut.formattedText("[[Page Name|desc]]"));
	}
	
	@Test public void linkWithDescriptionWithWhiteSpace() {
		assertEquals(p(link("Page Name", t("desc"))), dut.formattedText("[[Page Name | desc]]"));
	}
	

	@Test public void linkDescriptionCanBeFormatted() {
		assertEquals(p(link("Page Name", b("desc"))), dut.formattedText("[[Page Name|**desc**]]"));
	}

	@Test public void urlLink() {
		assertEquals(p(link("http://a")), dut.formattedText("[[http://a]]"));
	}

	@Test public void imageWithoutAlternative() {
		assertEquals(p(image("uri")), dut.formattedText("{{uri}}"));
	}

	@Test public void imageWithAlternative() {
		assertEquals(p(image("uri", "alt")), dut.formattedText("{{uri|alt}}"));
	}
	
	@Test public void paragraphsContainsFormattedText() {
		assertEquals(document(p(t("a"), b("b"), i("c"))), dut.document("a**b**//c//"));
	}

	@Test public void tableFollowedByHeading() {
		assertEquals(document(table(tr(td(t("a")))), h1("h1")), dut.document("|a|\n=h1"));
	}

	@Test public void paragraphFollowedByHeading1() {
		assertEquals(document(p(t("a")), h1("h1")), dut.document("a\n=h1"));
	}
	
	@Test public void paragraphFollowedByHeading2() {
		assertEquals(document(p(t("a")), h1("h1"), p(t("b")), h2("h2"), p(t("c"))), dut.document("a\n=h1=\nb\n\n==h2==\nc"));
	}
	
	@Test public void paragraphHaveHeadings() {
		String html = "=h1=\na\n"
					+ "==h2==\nb\n"
					+ "===h3===\nc\n"
					+ "====h4====\nd\n"
					+ "=====h5=====\ne";
		assertEquals(document(h1("h1"), p(t("a")), h2("h2"), p(t("b")), h3("h3"), p(t("c"))
				, h4("h4"), p(t("d")), h5("h5"), p(t("e"))
		), dut.document(html));
	}

	@Test public void paragraphFollowedByHorizontalRule() {
		assertEquals(document(p(t("a")), hr()), dut.document("a\n----"));
	}

	@Test public void tablesContainFormattedText() {
		assertEquals(document(table(tr(td(t("a"), b("b"), i("c"))))), dut.document("|a**b**//c//|"));
	}

	@Test public void listsFollowedByParagraph() {
		assertEquals(document(ul(li(t("a"))), p(t("b"))), dut.document("*a\nb"));
	}

	@Test public void listsFollowedByTable() {
		assertEquals(document(ul(li(t("a"))), table(tr(td(t("b"))))), dut.document("*a\n|b|"));
	}

	@Test public void inlineFormattingsCanBeClosedByEndOfParagraph() {
		assertEquals(
				document(p(i(t("a"), u()))),
				dut.document("//a__"));
	}

	@Test public void inlineFormattingsExtendsAcrossLineBreak() {
		assertEquals(
				document(p(i(t("a"), u(t(" "), t("b"))))),
				dut.document("//a__\nb"));
	}

	@Test public void inlineFormattingsCanBeClosedByEndOfTableCell() {
		assertEquals(
				document(table(tr(td(i(t("a"), u()))))),
				dut.document("|//a__|"));
	}

	@Test public void inlineFormattingsCanBeClosedByEndOfLink() {
		assertEquals(
				document(p(link("uri", i("a")))),
				dut.document("[[uri|//a]]"));
	}
	
	@Test public void characterEntity() {
		assertEquals(
				document(p(t("&quot;안녕하세요.&quot;"))), 
				dut.document("&quot;안녕하세요.&quot;"));
	}
}
