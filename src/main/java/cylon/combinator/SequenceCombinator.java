package cylon.combinator;

import java.util.ArrayList;
import java.util.List;

public class SequenceCombinator extends ActionParser {
    private final Parser[] parsers;

    public SequenceCombinator(Parser... parsers) {
        this.parsers = parsers;
    }

    public Result parse(CharSequence input) {
        List<Object> consumed = new ArrayList<Object>();
        CharSequence remaining = input;
        for (Parser each : parsers) {
            Result result = each.parse(remaining);
            if (result.isFailure()) {
                return Result.failure(input);
            }
            consumed.add(result.consumed());
            remaining = result.input();
        }
        return Result.success(action.apply(consumed), remaining);
    }
}
