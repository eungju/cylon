package cylon.combinator;

public class Combinators {
    public static Parser choice(Parser... parsers) {
        return new ChoiceCombinator(parsers);
    }
    
    public static Parser sequence(Parser... parsers) {
        return new SequenceCombinator(parsers);
    }

    public static Parser zeroOrMore(Parser parser) {
        return new ZeroOrMoreCombinator(parser);
    }

    public static Parser oneOrMore(Parser parser) {
        return new OneOrMoreCombinator(parser);
    }

    public static Parser optional(Parser parser) {
        return new ChoiceCombinator(parser, new EmptyParser());
    }
}
