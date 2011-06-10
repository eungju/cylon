package cylon.combinator;

public class CharRecognizers {
    public static Parser regex(String regex) {
        return new RegexParser(regex);
    }

    public static Parser term(final char c) {
       return new Parser() {
           public Result parse(CharSequence input) {
               if (input.length() > 0 && input.charAt(0) == c) {
                   return Result.success(input.subSequence(0, 1), input.subSequence(1, input.length()));
               }
               return Result.failure(input);
           }
       };
    }
}
