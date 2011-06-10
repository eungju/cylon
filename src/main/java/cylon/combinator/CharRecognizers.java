package cylon.combinator;

public class CharRecognizers {
    public static Parser term(final CharSequence symbol) {
       return new Parser() {
           public Result parse(CharSequence input) {
               if (input.length() >= symbol.length() && input.subSequence(0, symbol.length()).equals(symbol)) {
                   return Result.success(input.subSequence(0, symbol.length()), input.subSequence(symbol.length(), input.length()));
               }
               return Result.failure(input);
           }
       };
    }

    public static Parser regex(String regex) {
        return new RegexParser(regex);
    }

    public static Parser eol() {
        return new Parser() {
            public Result parse(CharSequence input) {
                if (input.length() == 0 || input.charAt(0) == '\r' || input.charAt(0) == '\n') {
                    return Result.success("", input);
                }
                return Result.failure(input);
            }
        };
    }
}
