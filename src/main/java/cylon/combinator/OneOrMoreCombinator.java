package cylon.combinator;

import java.util.ArrayList;
import java.util.List;

public class OneOrMoreCombinator extends ActionParser {
    private final Parser expression;

    public OneOrMoreCombinator(Parser expression) {
        this.expression = expression;
    }

    public Result parse(CharSequence input) {
        List<Object> derivative = new ArrayList<Object>();
        CharSequence remaining = input;
        Result result = expression.parse(remaining);
        if (result.isFailure()) {
            return Result.failure(input);
        }
        derivative.add(result.derivative());
        remaining = result.input();
        while (true) {
            result = expression.parse(remaining);
            if (result.isFailure()) {
                break;
            }
            derivative.add(result.derivative());
            remaining = result.input();
        }
        return Result.success(action.invoke(derivative), remaining);
    }
}
