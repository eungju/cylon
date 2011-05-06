package cylon.creole;

/**
 * All WikiParser should pass this specification.
 */
public interface WikiParserSpec {
	void documentCanBeEmpty();
	void listAllowsMultiDepthIndentAndDedent();
	void listCanBeNested();
	void listCanBeMixed();
}
