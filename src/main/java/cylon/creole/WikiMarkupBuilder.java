package cylon.creole;

/**
 * 마크업은 보호하고 마크업이 아닌 문자는 필요한 경우 이스케이프한다.
 * 이스케이핑은 가능한 적게 한다.
 * 
 * 클라이언트에서 어떤 때는 추상화 정도가 다른 newline/emit, markup, bold/italic/.../etc 을 섞어 쓰고 있는데 추상화 정도가 같도록 하면 좋겠다.
 */
public class WikiMarkupBuilder {
	private StringBuilder buffer = new StringBuilder();

	public String toString() {
		return buffer.toString();
	}
	
	char lastChar() {
		return charAt(-1);
	}
	
	char charAt(int index) {
		if (index < 0) {
			return buffer.charAt(buffer.length() + index);
		} else {
			return buffer.charAt(index);
		}
	}
	
	boolean isChar(int index, char c) {
		return buffer.length() < Math.abs(index) || charAt(index) == c;
	}
	
	boolean isOnNewLine() {
		if (buffer.length() == 0) return true;
		char c = lastChar();
		return c == '\n' || c == '\r'; 
	}
	
	boolean isOnEmptyLine() {
		int pos = buffer.length() - 1;
		while (0 <= pos) {
			char c = buffer.charAt(pos--);
			if (c == '\n' || c == '\r') {
				return true;
			} else if (c == '\t' || c == ' '){
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public WikiMarkupBuilder newline() {
		newline(false);
		return this;
	}
	
	public WikiMarkupBuilder newline(boolean force) {
		if (force) {
			buffer.append('\n');
			return this;
		}
		if( buffer.length() == 0 ) {
			return this;
		}
		if (!isOnNewLine()) { 
			buffer.append('\n');
		}
			
		return this;
	}
	
	public WikiMarkupBuilder emit(char c, int times) {
		for (int i = 0; i < times; i++) {
			buffer.append(c);
		}
		return this;
	}
	
	public WikiMarkupBuilder emit(String str) {
		buffer.append(str);
		return this;
	}
	
	public WikiMarkupBuilder markup(String markup) {
		if (isChar(-1, markup.charAt(0)) && !isChar(-2, '~')) {
			buffer.insert(buffer.length() - 1, '~');
		}
		buffer.append(markup);
		return this;
	}
	
	public WikiMarkupBuilder bold() {
		return markup("**");
	}

	public WikiMarkupBuilder italic() {
		return markup("//");
	}

	public WikiMarkupBuilder superscript() {
		return markup("^^");
	}

	public WikiMarkupBuilder subscript() {
		return markup(",,");
	}

	public WikiMarkupBuilder strike() {
		return markup("--");
	}

	public WikiMarkupBuilder underline() {
		return markup("__");
	}

	public WikiMarkupBuilder citation() {
		return markup("``");
	}

	public WikiMarkupBuilder forcedLinebreak() {
		return markup("\\\\");
	}

	private static final String INLINE_PUNCT = "\\*/_-^,[]{}<>`";
	private static final String LIST_PUNCT = "#*";
	private static final String BLOCK_PUNCT = "=-|>{[" + LIST_PUNCT;
	public WikiMarkupBuilder unformatted(String str) {
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (BLOCK_PUNCT.indexOf(c) >= 0 && isOnNewLine()) {
				buffer.append('~');
			} else if (LIST_PUNCT.indexOf(c) >= 0 && isOnEmptyLine()) {
				buffer.append('~');
			} else if (INLINE_PUNCT.indexOf(c) >= 0 && isChar(-1, c) && !isChar(-2, '~')) {
				buffer.append('~');
			} else if (INLINE_PUNCT.indexOf(c) >= 0 && isChar(-1, '~')) {
				buffer.append('~');
			}
			buffer.append(c);
		}
		return this;
	}
	
	private static final String PREFORMATTED_PUNCTATIONS_REGEX = "(^\\}\\}\\})";
	public WikiMarkupBuilder preformatted(String str) {
		buffer.append(str.replaceAll(PREFORMATTED_PUNCTATIONS_REGEX, "~$1"));
		return this;
	}
}

