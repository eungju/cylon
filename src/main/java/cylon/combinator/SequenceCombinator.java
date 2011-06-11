package cylon.combinator;

import java.util.ArrayList;
import java.util.List;

public class SequenceCombinator extends ActionParser {
    private final Parser[] expressions;

    public SequenceCombinator(Parser... expressions) {
        this.expressions = expressions;
    }

    public Result parse(CharSequence input) {
        List<Object> derivative = new ArrayList<Object>();
        CharSequence remaining = input;
        for (Parser each : expressions) {
            Result result = each.parse(remaining);
            if (result.isFailure()) {
                return Result.failure(input);
            }
            derivative.add(result.derivative());
            remaining = result.input();
        }
        return Result.success(action.invoke(derivative), remaining);
    }
}
