package cylon.combinator;

public class EofParser extends ActionParser {
    public Result parse(CharSequence input) {
        if (input.length() == 0) {
            return Result.success(action.invoke(input.subSequence(0, 0)), input);
        }
        return Result.failure(input);
    }
}
