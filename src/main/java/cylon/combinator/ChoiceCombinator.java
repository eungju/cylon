package cylon.combinator;

public class ChoiceCombinator extends Parser {
    private final Parser[] parsers;

    public ChoiceCombinator(Parser... parsers) {
        this.parsers = parsers;
    }

    public Result parse(CharSequence input) {
        for (Parser each : parsers) {
            Result result = each.parse(input);
            if (result.isSuccess()) {
                return Result.success(action.apply(result.consumed()), result.input());
            }
        }
        return Result.failure(input);
    }
}
