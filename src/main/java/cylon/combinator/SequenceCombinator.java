package cylon.combinator;

public class SequenceCombinator implements Parser {
    private Parser[] parsers;

    public SequenceCombinator(Parser... parsers) {
        this.parsers = parsers;
    }

    public Result parse(CharSequence input) {
        int pos = 0;
        for (Parser each : parsers) {
            Result result = each.parse(input.subSequence(pos, input.length()));
            if (result.isFailure()) {
                return Result.failure(input);
            }
            pos += result.consumed().length();
        }
        return Result.success(input.subSequence(0, pos), input.subSequence(pos, input.length()));
    }
}
