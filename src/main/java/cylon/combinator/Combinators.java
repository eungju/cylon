package cylon.combinator;

public class Combinators {
    public static Parser zeroOrMore(Parser parser) {
        return new ZeroOrMoreCombinator(parser);
    }

    public static Parser oneOrMore(Parser parser) {
        return new SequenceCombinator(parser, zeroOrMore(parser));
    }

    public static Parser optional(Parser parser) {
        return new ChoiceCombinator(parser, new EmptyParser());
    }
}
