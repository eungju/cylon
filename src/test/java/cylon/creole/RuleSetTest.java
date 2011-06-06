package cylon.creole;

import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class RuleSetTest {
	@Test public void simple() {
		Rule r1 = new Rule("a(b)c");
		Rule r2 = new Rule("d(e)f");
		RuleSet ruleSet = new RuleSet(new Rule[] {r1, r2}, 0);
		assertEquals("(a(b)c)|(d(e)f)", ruleSet.pattern().pattern());
		Matcher matcher = ruleSet.pattern().matcher("abcdef");
		matcher.find();
		assertEquals(r1, ruleSet.rule(matcher));
		assertEquals("abc", ruleSet.group(matcher, r1)[0]);
		assertEquals("b", ruleSet.group(matcher, r1)[1]);
		matcher.find();
		assertEquals(r2, ruleSet.rule(matcher));
		assertEquals("def", ruleSet.group(matcher, r2)[0]);
		assertEquals("e", ruleSet.group(matcher, r2)[1]);
	}
}
