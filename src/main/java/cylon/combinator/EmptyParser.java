package cylon.combinator;

public class EmptyParser extends ActionParser {
    public static final CharSequence EMPTY = "";

    public Result parse(CharSequence input) {
        return Result.success(action.invoke(EMPTY), input);
    }
}
