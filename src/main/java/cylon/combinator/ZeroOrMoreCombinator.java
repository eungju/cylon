package cylon.combinator;

import java.util.ArrayList;
import java.util.List;

public class ZeroOrMoreCombinator extends ActionParser {
    private final Parser parser;

    public ZeroOrMoreCombinator(Parser parser) {
        this.parser = parser;
    }

    public Result parse(CharSequence input) {
        List<Object> consumed = new ArrayList<Object>();
        CharSequence remaining = input;
        while (true) {
            Result result = parser.parse(remaining);
            if (result.isFailure()) {
                break;
            }
            consumed.add(result.consumed());
            remaining = result.input();
        }
        return Result.success(action.apply(consumed), remaining);
    }
}
