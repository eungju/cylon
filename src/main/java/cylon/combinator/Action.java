package cylon.combinator;

public interface Action {
    public Object invoke(Object from);

    public static final Action NOTHING = new Action() {
        public Object invoke(Object from) {
            return from;
        }
    };
}
