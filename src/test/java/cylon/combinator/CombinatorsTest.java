package cylon.combinator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static cylon.combinator.CharRecognizers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CombinatorsTest {
    @Test public void
    sequenceSuccess() {
        assertThat(Combinators.sequence(regex("a"), regex("b")).parse("abc"), is(Result.success(Arrays.asList("a", "b"), "c")));
    }

    @Test public void
    sequenceFailure() {
        assertThat(Combinators.sequence(regex("a"), regex("c")).parse("abc"), is(Result.failure("abc")));
    }

    @Test public void
    choiceSuccess() {
        Parser dut = Combinators.choice(regex("a"), regex("."));
        assertThat(dut.parse("abc"), is(Result.success("a", "bc")));
        assertThat(dut.parse("123"), is(Result.success("1", "23")));
    }

    @Test public void
    choiceFailure() {
        Parser dut = Combinators.choice(regex("a"), regex("1"));
        assertThat(dut.parse("!@#"), is(Result.failure("!@#")));
    }

    @Test public void
    zeroOrMore() {
        Parser dut = Combinators.zeroOrMore(regex("a"));
        assertThat(dut.parse("bbcc"), is(Result.success(new ArrayList<CharSequence>(), "bbcc")));
        assertThat(dut.parse("aabbcc"), is(Result.success(Arrays.asList("a", "a"), "bbcc")));
    }

    @Test public void
    oneOrMore() {
        Parser dut = Combinators.oneOrMore(regex("a"));
        assertThat(dut.parse("abbcc"), is(Result.success(Arrays.asList("a"), "bbcc")));
        assertThat(dut.parse("aabbcc"), is(Result.success(Arrays.asList("a", "a"), "bbcc")));
        assertThat(dut.parse("bbcc"), is(Result.failure("bbcc")));
    }

    @Test public void
    optional() {
        Parser dut = Combinators.optional(regex("a"));
        assertThat(dut.parse("bc"), is(Result.success("", "bc")));
        assertThat(dut.parse("abc"), is(Result.success("a", "bc")));
    }
}
