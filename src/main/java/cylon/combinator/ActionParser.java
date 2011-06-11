package cylon.combinator;

public abstract class ActionParser implements Parser {
    protected Action action = Action.NOTHING;

    public Parser with(Action action) {
        this.action = action;
        return this;
    }
}
