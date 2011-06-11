package cylon.combinator;

public abstract class Parser {
    protected Action action = Action.NOTHING;

    public Parser with(Action action) {
        this.action = action;
        return this;
    }

    public abstract Result parse(CharSequence input);
}
