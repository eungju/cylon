package cylon.dom;

class DomUtils {
	public static String toString(Object node, Object[] children) {
		StringBuilder buf = new StringBuilder();
		buf.append('(');
		buf.append(node.getClass().getSimpleName());
		for (Object each : children) {
			buf.append(' ');
			buf.append(each);
		}
		buf.append(')');
		return buf.toString();
	}
}
