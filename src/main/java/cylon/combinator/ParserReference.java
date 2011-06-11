package cylon.combinator;

public class ParserReference extends Parser {
    private Parser parser;

    public ParserReference set(Parser parser) {
        this.parser = parser;
        return this;
    }

    public Parser with(Action action) {
        parser.with(action);
        return this;
    }

    public Result parse(CharSequence input) {
        return parser.parse(input);
    }
}
