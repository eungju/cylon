package cylon.combinator;

public class EmptyParser extends ActionParser {
    public Result parse(CharSequence input) {
        return Result.success(action.invoke(input.subSequence(0, 0)), input);
    }
}
