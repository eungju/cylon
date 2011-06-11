package cylon.combinator;

public class Combinators {
    public static ActionParser choice(Parser... expressions) {
        return new ChoiceCombinator(expressions);
    }
    
    public static ActionParser sequence(Parser... expressions) {
        return new SequenceCombinator(expressions);
    }

    public static ActionParser zeroOrMore(Parser expressions) {
        return new ZeroOrMoreCombinator(expressions);
    }

    public static ActionParser oneOrMore(Parser expressions) {
        return new OneOrMoreCombinator(expressions);
    }

    public static ActionParser optional(Parser expression) {
        return new ChoiceCombinator(expression, new EmptyParser());
    }
}
