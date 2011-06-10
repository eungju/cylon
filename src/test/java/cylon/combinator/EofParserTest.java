package cylon.combinator;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EofParserTest {
    private Parser dut;

    @Before public void beforeEach() {
        dut = new EofParser();
    }

    @Test
    public void
    success() {
        assertThat(dut.parse(""), is(Result.success("", "")));
    }

    @Test
    public void
    failure() {
        assertThat(dut.parse(" "), is(Result.failure(" ")));
    }
}
