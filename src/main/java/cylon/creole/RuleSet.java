package cylon.creole;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RuleSet {
	private int flags;
	private Map<Rule, Integer> indexedRules;
	private Pattern pattern;

	public RuleSet(Rule[] rules, int flags) {
		this.flags = flags;
		indexedRules = new HashMap<Rule, Integer>();
		StringBuilder merged = new StringBuilder();
		int groupIndex = 1;
		int index = 0;
		for (Rule each : rules) {
			indexedRules.put(each, groupIndex);
			groupIndex += each.groupCount() + 1;

			if (index++ != 0) {
				merged.append('|');
			}
			merged.append('(');
			merged.append(each.expression());
			merged.append(')');
		}
		pattern = Pattern.compile(merged.toString(), flags);
	}

	public Pattern pattern() {
		return pattern;
	}

	public Pattern pattern(Rule rule) {
		return Pattern.compile(rule.expression(), flags);
	}

	public Rule rule(Matcher matcher) {
		for (Map.Entry<Rule, Integer> each : indexedRules.entrySet()) {
			if (matcher.group(each.getValue()) != null) {
				return each.getKey();
			}
		}
		return null;
	}

	public String[] group(Matcher matcher, Rule rule) {
		String[] group = new String[rule.groupCount() + 1];
		for (int i = 0; i < group.length; i++) {
			group[i] = matcher.group(indexedRules.get(rule) + i);
		}
		return group;
	}
}