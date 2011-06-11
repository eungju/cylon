package cylon.combinator;

public class ChoiceCombinator extends ActionParser {
    private final Parser[] expressions;

    public ChoiceCombinator(Parser... expressions) {
        this.expressions = expressions;
    }

    public Result parse(CharSequence input) {
        for (Parser each : expressions) {
            Result result = each.parse(input);
            if (result.isSuccess()) {
                return Result.success(action.invoke(result.derivative()), result.input());
            }
        }
        return Result.failure(input);
    }
}
