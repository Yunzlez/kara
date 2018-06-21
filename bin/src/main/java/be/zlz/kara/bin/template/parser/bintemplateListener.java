// Generated from C:/JEE/Projects/zlzbin-j/bin/src/main/resources/antlr\bintemplate.g4 by ANTLR 4.7
package be.zlz.kara.bin.template.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link bintemplateParser}.
 */
public interface bintemplateListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link bintemplateParser#template}.
	 * @param ctx the parse tree
	 */
	void enterTemplate(bintemplateParser.TemplateContext ctx);
	/**
	 * Exit a parse tree produced by {@link bintemplateParser#template}.
	 * @param ctx the parse tree
	 */
	void exitTemplate(bintemplateParser.TemplateContext ctx);
	/**
	 * Enter a parse tree produced by {@link bintemplateParser#placeholder}.
	 * @param ctx the parse tree
	 */
	void enterPlaceholder(bintemplateParser.PlaceholderContext ctx);
	/**
	 * Exit a parse tree produced by {@link bintemplateParser#placeholder}.
	 * @param ctx the parse tree
	 */
	void exitPlaceholder(bintemplateParser.PlaceholderContext ctx);
}