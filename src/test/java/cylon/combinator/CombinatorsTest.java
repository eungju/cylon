package cylon.combinator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static cylon.combinator.CharRecognizers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CombinatorsTest {
    @Test public void
    sequenceOperatorSuccess() {
        assertThat(Combinators.sequence(term("a"), term("b")).parse("abc"), is(Result.success(Arrays.asList("a", "b"), "c")));
    }

    @Test public void
    sequenceOperatorFailure() {
        assertThat(Combinators.sequence(term("a"), term("c")).parse("abc"), is(Result.failure("abc")));
    }

    @Test public void
    choiceOperatorSuccess() {
        Parser dut = Combinators.choice(term("a"), term("1"));
        assertThat(dut.parse("abc"), is(Result.success("a", "bc")));
        assertThat(dut.parse("123"), is(Result.success("1", "23")));
    }

    @Test public void
    choiceOperatorFailure() {
        Parser dut = Combinators.choice(term("a"), term("1"));
        assertThat(dut.parse("!@#"), is(Result.failure("!@#")));
    }

    @Test public void
    zeroOrMoreOperator() {
        Parser dut = Combinators.zeroOrMore(term("a"));
        assertThat(dut.parse("bbcc"), is(Result.success(new ArrayList<CharSequence>(), "bbcc")));
        assertThat(dut.parse("aabbcc"), is(Result.success(Arrays.asList("a", "a"), "bbcc")));
    }

    @Test public void
    oneOrMoreOperator() {
        Parser dut = Combinators.oneOrMore(term("a"));
        assertThat(dut.parse("abbcc"), is(Result.success(Arrays.asList("a"), "bbcc")));
        assertThat(dut.parse("aabbcc"), is(Result.success(Arrays.asList("a", "a"), "bbcc")));
        assertThat(dut.parse("bbcc"), is(Result.failure("bbcc")));
    }

    @Test public void
    optionalOperator() {
        Parser dut = Combinators.optional(term("a"));
        assertThat(dut.parse("bc"), is(Result.success("", "bc")));
        assertThat(dut.parse("abc"), is(Result.success("a", "bc")));
    }

    @Test public void
    andPredicate() {
        Parser dut = Combinators.and(term("a"));
        assertThat(dut.parse("abc"), is(Result.success("", "abc")));
        assertThat(dut.parse("bc"), is(Result.failure("bc")));
    }

    @Test public void
    notPredicate() {
        Parser dut = Combinators.not(term("a"));
        assertThat(dut.parse("bc"), is(Result.success("", "bc")));
        assertThat(dut.parse("abc"), is(Result.failure("abc")));
    }
}
