package cylon.combinator;

public class ZeroOrMoreCombinator implements Parser {
    private final Parser parser;

    public ZeroOrMoreCombinator(Parser parser) {
        this.parser = parser;
    }

    public Result parse(CharSequence input) {
        int pos = 0;
        while (true) {
            Result result = parser.parse(input.subSequence(pos, input.length()));
            if (result.isFailure()) {
                break;
            }
            pos += result.consumed().length();
        }
        return Result.success(input.subSequence(0, pos), input.subSequence(pos, input.length()));
    }
}
