package cylon.combinator;

public class SequnenceCombinator implements Parser {
    private Parser[] parsers;

    public SequnenceCombinator(Parser... parsers) {
        this.parsers = parsers;
    }

    public Result parse(CharSequence input) {
        int pos = 0;
        for (Parser each : parsers) {
            CharSequence remaining = input.subSequence(pos, input.length());
            Result result = each.parse(remaining);
            if (result.isSuccess()) {
                pos += result.consumed().length();
            } else {
                return Result.failure(input);
            }
        }
        return Result.success(input.subSequence(0, pos), input.subSequence(pos, input.length()));
    }
}
