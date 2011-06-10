package cylon.combinator;

import org.junit.Test;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CharBufferTest {
    @Test
    public void asACharSequence() {
        CharBuffer dut = CharBuffer.wrap("Hello, World");
        assertThat(dut.length(), equalTo(12));
        assertThat(Pattern.compile("Hello").matcher(dut).find(), is(true));
        dut.get();
        assertThat(dut.length(), equalTo(11));
        assertThat(Pattern.compile("Hello").matcher(dut).find(), is(false));
    }
}
