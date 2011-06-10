package cylon.combinator;

public class EmptyParser implements Parser {
    public Result parse(CharSequence input) {
        return Result.success("", input);
    }
}
