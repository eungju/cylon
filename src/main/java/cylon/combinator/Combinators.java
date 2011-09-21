package cylon.combinator;

public class Combinators {
    public static ActionParser empty() {
        return new EmptyParser();
    }

    public static ActionParser any() {
        return new AnyParser();
    }

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
        return choice(expression, empty());
    }

    public static ActionParser and(final Parser expression) {
        return new ActionParser() {
            public Result parse(CharSequence input) {
                Result result = expression.parse(input);
                if (result.isSuccess()) {
                    return Result.success(action.invoke(EmptyParser.EMPTY), input);
                }
                return Result.failure(input);
            }
        };
    }

    public static ActionParser not(final Parser expression) {
        return new ActionParser() {
            public Result parse(CharSequence input) {
                Result result = expression.parse(input);
                if (result.isFailure()) {
                    return Result.success(action.invoke(EmptyParser.EMPTY), input);
                }
                return Result.failure(input);
            }
        };
    }

}
