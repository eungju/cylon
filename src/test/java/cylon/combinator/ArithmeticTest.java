package cylon.combinator;

import org.junit.Test;

import static cylon.combinator.CharRecognizers.*;
import static cylon.combinator.Combinators.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * http://en.wikipedia.org/wiki/Parsing_expression_grammar#Examples
 */
public class ArithmeticTest {
    ParserReference exprRef = new ParserReference();

    Parser value() {
        return choice(regex("[0-9]+"), sequence(term("("), exprRef, term(")")));
    }

    Parser product() {
        return sequence(value(), zeroOrMore(sequence(choice(term("*"), term("/")), value())));
    }

    Parser sum() {
        return sequence(product(), zeroOrMore(sequence(choice(term("+"), term("-")), product())));
    }

    Parser expr() {
        return exprRef.set(sum());
    }

    @Test
    public void
    arithmetic() {
        Parser parser = new ArithmeticTest().expr();
        assertThat(parser.parse("1+2*3").consumed(), is((CharSequence) "1+2*3"));
    }
}
