package cylon.combinator;

public class AnyParser extends ActionParser {
    public Result parse(CharSequence input) {
        if (input.length() > 0) {
            return Result.success(action.invoke(input.subSequence(0, 1)), input.subSequence(1, input.length()));
        }
        return Result.failure(input);
    }
}
