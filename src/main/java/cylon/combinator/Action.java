package cylon.combinator;

public interface Action {
    public Object apply(Object from);

    public static final Action NOTHING = new Action() {
        public Object apply(Object from) {
            return from;
        }
    };
}
