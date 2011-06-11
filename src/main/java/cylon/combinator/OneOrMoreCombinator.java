package cylon.combinator;

import java.util.ArrayList;
import java.util.List;

public class OneOrMoreCombinator extends Parser {
    private final Parser parser;

    public OneOrMoreCombinator(Parser parser) {
        this.parser = parser;
    }

    public Result parse(CharSequence input) {
        List<Object> consumed = new ArrayList<Object>();
        CharSequence remaining = input;
        Result result = parser.parse(remaining);
        if (result.isFailure()) {
            return Result.failure(input);
        }
        consumed.add(result.consumed());
        remaining = result.input();
        while (true) {
            result = parser.parse(remaining);
            if (result.isFailure()) {
                break;
            }
            consumed.add(result.consumed());
            remaining = result.input();
        }
        return Result.success(action.apply(consumed), remaining);
    }
}
