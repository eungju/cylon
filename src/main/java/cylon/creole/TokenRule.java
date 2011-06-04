package cylon.creole;

abstract class TokenRule extends Rule {
    public TokenRule(String expression) {
        super(expression);
    }
    public abstract void matched(String[] group, AbstractCreoleParser parser);
}
