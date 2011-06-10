package cylon.combinator;

public class ChoiceCombinator implements Parser {
    private Parser[] parsers;

    public ChoiceCombinator(Parser... parsers) {
        this.parsers = parsers;
    }

    public Result parse(CharSequence input) {
        for (Parser each : parsers) {
            Result result = each.parse(input);
            if (result.isSuccess()) {
                return result;
            }
        }
        return Result.failure(input);
    }
}
