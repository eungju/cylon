package cylon.combinator;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RecognizerTest {
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
        Parser dut = new EolParser();
        assertThat(dut.parse(""), is(Result.success("", "")));
        assertThat(dut.parse("\r\n"), is(Result.success("", "\r\n")));
        assertThat(dut.parse("\n"), is(Result.success("", "\n")));
    }

    @Test public void
    eolFailure() {
        assertThat(new EolParser().parse(" "), is(Result.failure(" ")));
    }

    @Test public void
    regexSuccess() {
        assertThat(new RegexParser("a").parse("abc"), is(Result.success("a", "bc")));
    }

    @Test public void
    regexFailure() {
        assertThat(new RegexParser("a").parse(""), is(Result.failure("")));
        assertThat(new RegexParser("a").parse("b"), is(Result.failure("b")));
        assertThat(new RegexParser("a").parse("123abc"), is(Result.failure("123abc")));
    }
}
