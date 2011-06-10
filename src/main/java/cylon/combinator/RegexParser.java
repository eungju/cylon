package cylon.combinator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser implements Parser {
    private final Pattern pattern;

    public RegexParser(String regex) {
        pattern = Pattern.compile(regex);
    }

    public Result parse(CharSequence input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            if (matcher.start() == 0) {
                return Result.success(matcher.group(0), input.subSequence(matcher.end(), input.length()));
            }
        }
        return Result.failure(input);
    }
}
