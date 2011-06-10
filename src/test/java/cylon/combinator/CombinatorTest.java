package cylon.combinator;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CombinatorTest {
    @Test public void
    sequenceSuccess() {
        assertThat(new SequnenceCombinator(new RegexParser("a"), new RegexParser("b")).parse("abc"), is(Result.success("ab", "c")));
    }

    @Test public void
    sequenceFailure() {
        assertThat(new SequnenceCombinator(new RegexParser("a"), new RegexParser("c")).parse("abc"), is(Result.failure("abc")));
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
}
