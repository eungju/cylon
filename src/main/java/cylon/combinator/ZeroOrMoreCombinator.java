package cylon.combinator;

import java.util.ArrayList;
import java.util.List;

public class ZeroOrMoreCombinator extends ActionParser {
    private final Parser expression;

    public ZeroOrMoreCombinator(Parser expression) {
        this.expression = expression;
    }

    public Result parse(CharSequence input) {
        List<Object> derivative = new ArrayList<Object>();
        CharSequence remaining = input;
        while (true) {
            Result result = expression.parse(remaining);
            if (result.isFailure()) {
                break;
            }
            derivative.add(result.derivative());
            remaining = result.input();
        }
        return Result.success(action.invoke(derivative), remaining);
    }
}
