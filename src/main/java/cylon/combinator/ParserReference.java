package cylon.combinator;

public class ParserReference implements Parser {
    private Parser parser;

    public void set(Parser parser) {
        this.parser = parser;
    }

    public Result parse(CharSequence input) {
        return parser.parse(input);
    }
}
