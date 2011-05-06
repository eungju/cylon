package cylon.creole;

import java.util.regex.Pattern;

public class Rule {
	private String expression;
	private int groupCount;

	public Rule(String expression) {
		this.expression = expression;
		this.groupCount = Pattern.compile(expression).matcher("").groupCount();
	}

	public String expression() {
		return expression;
	}
	
	public int groupCount() {
		return groupCount;
	}
}