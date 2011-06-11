package cylon.combinator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharRecognizers {
    public static Parser term(final CharSequence symbol) {
       return new Parser() {
           public Result parse(CharSequence input) {
               if (input.length() >= symbol.length() && input.subSequence(0, symbol.length()).equals(symbol)) {
                   return Result.success(action.apply(input.subSequence(0, symbol.length())), input.subSequence(symbol.length(), input.length()));
               }
               return Result.failure(input);
           }
       };
    }

    public static Parser regex(final String regex) {
        return new Parser() {
            private final Pattern pattern = Pattern.compile(regex);

            public Result parse(CharSequence input) {
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    if (matcher.start() == 0) {
                        return Result.success(action.apply(matcher.group(0)), input.subSequence(matcher.end(), input.length()));
                    }
                }
                return Result.failure(input);
            }
        };
    }

    public static Parser eol() {
        return new Parser() {
            public Result parse(CharSequence input) {
                if (input.length() == 0 || input.charAt(0) == '\r' || input.charAt(0) == '\n') {
                    return Result.success(action.apply(""), input);
                }
                return Result.failure(input);
            }
        };
    }
}
