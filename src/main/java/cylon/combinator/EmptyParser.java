package cylon.combinator;

public class EmptyParser extends Parser {
    public Result parse(CharSequence input) {
        return Result.success(action.apply(""), input);
    }
}
