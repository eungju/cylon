package cylon.combinator;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static cylon.combinator.CharRecognizers.*;
import static cylon.combinator.Combinators.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * http://en.wikipedia.org/wiki/Parsing_expression_grammar#Examples
 */
public class ArithmeticTest {
    static class Calculator {
        ParserReference expressionRef = new ParserReference();
        ParserReference productRef = new ParserReference();
        Parser parser = expression();

        Parser factor() {
            return choice(
                    regex("[0-9]+").with(new Action() {
                        public Double apply(Object from) {
                            return Double.parseDouble(from.toString());
                        }
                    }),
                    sequence(term("("), expressionRef, term(")")).with(new Action() {
                        public Double apply(Object from) {
                            return (Double) ((List) from).get(1);
                        }
                    }));
        }

        Parser product() {
            return productRef.set(choice(
                    sequence(factor(), choice(term("*"), term("/")), productRef).with(new Action() {
                        public Object apply(Object from) {
                            Double a = (Double) ((List) from).get(0);
                            Object operator = ((List) from).get(1);
                            Double b = (Double) ((List) from).get(2);
                            return (operator.equals("*")) ? a * b : a / b;
                        }
                    }),
                    factor()));
        }

        Parser expression() {
            return expressionRef.set(choice(
                    sequence(product(), choice(term("+"), term("-")), expressionRef).with(new Action() {
                        public Object apply(Object from) {
                            Double a = (Double) ((List) from).get(0);
                            Object operator = ((List) from).get(1);
                            Double b = (Double) ((List) from).get(2);
                            return (operator.equals("+")) ? a + b : a - b;
                        }
                    }),
                    product()));
        }

        public double calculate(String expression) {
            return (Double) parser.parse(expression).consumed();
        }
    }

    Calculator dut;

    @Before
    public void beforeEach() {
        dut = new Calculator();
    }

    @Test public void
    precedence() {
        assertThat(dut.calculate("1+2*3"), is(7D));
        assertThat(dut.calculate("1*2+3"), is(5D));
        assertThat(dut.calculate("(1+2)*3"), is(9D));
    }

    @Test public void
    rightAssociative() {
        assertThat(dut.calculate("48/2*(9+3)"), is(2D));
    }
}
