package cylon.combinator;

public class EofParser implements Parser {
    public Result parse(CharSequence input) {
        if (input.length() == 0) {
            return Result.success("", input);
        }
        return Result.failure(input);
    }
}
