package cylon.combinator;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CombinatorTest {
    @Test public void
    sequenceSuccess() {
        assertThat(new SequenceCombinator(new RegexParser("a"), new RegexParser("b")).parse("abc"), is(Result.success("ab", "c")));
    }

    @Test public void
    sequenceFailure() {
        assertThat(new SequenceCombinator(new RegexParser("a"), new RegexParser("c")).parse("abc"), is(Result.failure("abc")));
    }

    @Test public void
    choiceSuccess() {
        Parser dut = new ChoiceCombinator(new RegexParser("a"), new RegexParser("1"));
        assertThat(dut.parse("abc"), is(Result.success("a", "bc")));
        assertThat(dut.parse("123"), is(Result.success("1", "23")));
    }

    @Test public void
    choiceFailure() {
        Parser dut = new ChoiceCombinator(new RegexParser("a"), new RegexParser("1"));
        assertThat(dut.parse("!@#"), is(Result.failure("!@#")));
    }

    @Test public void
    zeroOrMore() {
        Parser dut = new ZeroOrMoreCombinator(new RegexParser("a"));
        assertThat(dut.parse("bbcc"), is(Result.success("", "bbcc")));
        assertThat(dut.parse("aabbcc"), is(Result.success("aa", "bbcc")));
    }

    @Test public void
    oneOrMore() {
        Parser dut = Combinators.oneOrMore(new RegexParser("a"));
        assertThat(dut.parse("abbcc"), is(Result.success("a", "bbcc")));
        assertThat(dut.parse("aabbcc"), is(Result.success("aa", "bbcc")));
        assertThat(dut.parse("bbcc"), is(Result.failure("bbcc")));
    }

    @Test public void
    optional() {
        Parser dut = Combinators.optional(new RegexParser("a"));
        assertThat(dut.parse("bc"), is(Result.success("", "bc")));
        assertThat(dut.parse("abc"), is(Result.success("a", "bc")));
    }
}
