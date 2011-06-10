package cylon.combinator;

import org.junit.Test;

import static cylon.combinator.CharRecognizers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CharRecognizersTest {
    @Test public void
    empty() {
        assertThat(new EmptyParser().parse(""), is(Result.success("", "")));
        assertThat(new EmptyParser().parse("abc"), is(Result.success("", "abc")));
    }

    @Test public void
    eofSuccess() {
        assertThat(new EofParser().parse(""), is(Result.success("", "")));
    }

    @Test public void
    eofFailure() {
        assertThat(new EofParser().parse(" "), is(Result.failure(" ")));
    }

    @Test public void
    eolSuccess() {
        Parser dut = eol();
        assertThat(dut.parse(""), is(Result.success("", "")));
        assertThat(dut.parse("\r\n"), is(Result.success("", "\r\n")));
        assertThat(dut.parse("\n"), is(Result.success("", "\n")));
    }

    @Test public void
    eolFailure() {
        assertThat(eol().parse(" "), is(Result.failure(" ")));
    }

    @Test public void
    regexSuccess() {
        assertThat(regex("a").parse("abc"), is(Result.success("a", "bc")));
    }

    @Test public void
    regexFailure() {
        Parser dut = regex("a");
        assertThat(dut.parse(""), is(Result.failure("")));
        assertThat(dut.parse("b"), is(Result.failure("b")));
        assertThat(dut.parse("123abc"), is(Result.failure("123abc")));
    }
}
