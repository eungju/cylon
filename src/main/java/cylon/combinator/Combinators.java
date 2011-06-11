package cylon.combinator;

public class Combinators {
    public static ActionParser choice(Parser... parsers) {
        return new ChoiceCombinator(parsers);
    }
    
    public static ActionParser sequence(Parser... parsers) {
        return new SequenceCombinator(parsers);
    }

    public static ActionParser zeroOrMore(Parser parser) {
        return new ZeroOrMoreCombinator(parser);
    }

    public static ActionParser oneOrMore(Parser parser) {
        return new OneOrMoreCombinator(parser);
    }

    public static ActionParser optional(Parser parser) {
        return new ChoiceCombinator(parser, new EmptyParser());
    }
}
