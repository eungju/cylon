package cylon.combinator;

import cylon.support.ObjectSupport;

public class Result extends ObjectSupport {
    public static Result success(CharSequence recognized, CharSequence remaining) {
        return new Success(recognized, remaining);
    }

    public static Result failure(CharSequence input) {
        return new Failure(input);
    }

    public static class Success extends Result {
        private CharSequence recognized;
        private CharSequence remaining;

        public Success(CharSequence recognized, CharSequence remaining) {
            this.recognized = recognized;
            this.remaining = remaining;
        }
    }

    public static class Failure extends Result {
        private CharSequence input;

        public Failure(CharSequence input) {
            this.input = input;
        }
    }
}
