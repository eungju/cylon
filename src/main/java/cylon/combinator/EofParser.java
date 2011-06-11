package cylon.combinator;

public class EofParser extends Parser {
    public Result parse(CharSequence input) {
        if (input.length() == 0) {
            return Result.success(action.apply(""), input);
        }
        return Result.failure(input);
    }
}
