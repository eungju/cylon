package cylon.combinator;

import cylon.support.ObjectSupport;

public class Result extends ObjectSupport {
    private final Object derivative;
    private final CharSequence input;

    private Result(Object derivative, CharSequence input) {
        this.derivative = derivative;
        this.input = input;
    }

    public static Result success(Object derivative, CharSequence input) {
        return new Result(derivative, input);
    }

    public static Result failure(CharSequence input) {
        return new Result(null, input);
    }

    public boolean isFailure() {
        return derivative == null;
    }

    public boolean isSuccess() {
        return derivative != null;
    }

    public Object derivative() {
        if (isFailure()) {
            throw new IllegalStateException("Failure");
        }
        return derivative;
    }

    public CharSequence input() {
        return input;
    }
}
