package cylon.combinator;

import cylon.support.ObjectSupport;

public class Result extends ObjectSupport {
    private final Object consumed;
    private final CharSequence input;

    private Result(Object consumed, CharSequence input) {
        this.consumed = consumed;
        this.input = input;
    }

    public static Result success(Object consumed, CharSequence input) {
        return new Result(consumed, input);
    }

    public static Result failure(CharSequence input) {
        return new Result(null, input);
    }

    public boolean isFailure() {
        return consumed == null;
    }

    public boolean isSuccess() {
        return consumed != null;
    }

    public Object consumed() {
        if (isFailure()) {
            throw new IllegalStateException("Failure");
        }
        return consumed;
    }

    public CharSequence input() {
        return input;
    }
}
