package cylon.combinator;

public class Result {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Success success = (Success) o;

            if (!recognized.equals(success.recognized)) return false;
            if (!remaining.equals(success.remaining)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = recognized.hashCode();
            result = 31 * result + remaining.hashCode();
            return result;
        }
    }

    public static class Failure extends Result {
        private CharSequence input;

        public Failure(CharSequence input) {
            this.input = input;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Failure failure = (Failure) o;

            if (!input.equals(failure.input)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return input.hashCode();
        }
    }
}
