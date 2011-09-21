package cylon.combinator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharRecognizers {
    public static ActionParser literal(final CharSequence symbol) {
       return new ActionParser() {
           public Result parse(CharSequence input) {
               if (input.length() >= symbol.length() && input.subSequence(0, symbol.length()).equals(symbol)) {
                   return Result.success(action.invoke(input.subSequence(0, symbol.length())), input.subSequence(symbol.length(), input.length()));
               }
               return Result.failure(input);
           }
       };
    }

    public static ActionParser regex(final String regex) {
        return new ActionParser() {
            private final Pattern pattern = Pattern.compile(regex);

            public Result parse(CharSequence input) {
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    if (matcher.start() == 0) {
                        return Result.success(action.invoke(matcher.group(0)), input.subSequence(matcher.end(), input.length()));
                    }
                }
                return Result.failure(input);
            }
        };
    }

    public static ActionParser eol() {
        return Combinators.choice(literal("\r\n"), literal("\r"), literal("\n"));
    }

    public static ActionParser eof() {
        return Combinators.not(Combinators.any());
    }
}
