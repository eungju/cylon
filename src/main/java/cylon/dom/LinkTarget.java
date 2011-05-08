package cylon.dom;

import cylon.support.ObjectSupport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkTarget extends ObjectSupport {
    private static final String WIKI_ID_REGEXP = "[a-z0-9][a-z0-9_]{2,19}";
    private static final String PAGE_NAME_REGEXP = "[^\"#$%*+\\./:;@\\[\\\\\\]^`{|}~]+";
	/**
	 * See http://en.wikipedia.org/wiki/URI_scheme
	 */
	private static final Pattern URL_PATTERN = Pattern.compile("(\\w+):([^:]*)");
	private static final String OFFICIAL_SCHEMES = "aaa|aaas|acap|cap|cid|crid|data|dav|dict|dns|fax|file|ftp|go|gopher|h323|http|https|im|imap|ldap|mailto|mid|news|nfs|nntp|pop|pres|rtsp|sip|sips|snmp|tel|telnet|urn|wais|xmpp";
	private static final Pattern SAFE_URL = Pattern.compile("(" + OFFICIAL_SCHEMES + "):[^:]*");
	//private static final Pattern SAFE_URL = Pattern.compile("(https?|ftp|mailto):[^:]*");
	private static final Pattern PAGE_PATTERN = Pattern.compile("(?:(" + WIKI_ID_REGEXP + ")::)?(" + PAGE_NAME_REGEXP + ")");
	private String target;
	private String neighborWikiId;
	private String pageName;
	private String attachmentName;
	private String url;
	
	public LinkTarget(String target) {
		this.target = target;
		Matcher matcher = null;
		if ((matcher = URL_PATTERN.matcher(target)).matches()) {
			String scheme = matcher.group(1);
			if ("attachment".equals(scheme)) {
				attachmentName = matcher.group(2);
			} else {
				url = target;
			}
		} else if ((matcher = PAGE_PATTERN.matcher(target)).matches()) {
			neighborWikiId = matcher.group(1);
			pageName = matcher.group(2);
		} else {
			url = target;
		}
	}
	
	public boolean isPage() {
		return pageName != null;
	}
	
	public String getPageName() {
		return pageName;
	}
	
	public boolean isNeighborWiki() {
		return neighborWikiId != null;
	}
	
	public String getNeighborWikiId() {
		return neighborWikiId;
	}
	
	public boolean isAttachment() {
		return attachmentName != null;
	}
	
	public String getAttachmentName() {
		return attachmentName;
	}
	
	public boolean isUrl() {
		return url != null;
	}
	
	public boolean isSafeUrl() {
		return SAFE_URL.matcher(url).matches();
	}
	
	public String getUrl() {
		return url;
	}
	
	public String toString() {
		return target;
	}

	public String getTarget() {
		return target;
	}
}
