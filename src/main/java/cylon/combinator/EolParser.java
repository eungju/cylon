package cylon.combinator;

public class EolParser implements Parser {
    public Result parse(CharSequence input) {
        if (input.length() == 0 || input.charAt(0) == '\r' || input.charAt(0) == '\n') {
            return Result.success("", input);
        }
        return Result.failure(input);
    }
}
