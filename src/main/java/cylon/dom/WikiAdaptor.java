package cylon.dom;

public interface WikiAdaptor {
	//page resources
	boolean isPageExist(String pageName);
	boolean isPageExist(String neighborWikiId, String pageName);
	String pageUrl(String pageName);
	String pageUrl(String neighborWikiId, String pageName);
	String pageEditUrl(String pageName);
	String pageEditUrl(String neighborWikiId, String pageName);
	String pageName(String pageUrl);
	
	//attachment resources
	boolean hasAttachmentOfName(String name);
	boolean hasAttachmentOfUrl(String url);
	String attachmentUrl(String name);
	String attachmentName(String url);
}