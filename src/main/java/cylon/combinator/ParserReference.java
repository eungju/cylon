package cylon.combinator;

public class ParserReference implements Parser {
    private Parser parser;

    public ParserReference set(Parser parser) {
        this.parser = parser;
        return this;
    }

    public Result parse(CharSequence input) {
        return parser.parse(input);
    }
}
