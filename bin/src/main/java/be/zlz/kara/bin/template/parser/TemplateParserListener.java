// Generated from TemplateParser.g4 by ANTLR 4.7.1
package be.zlz.kara.bin.template.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TemplateParser}.
 */
public interface TemplateParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TemplateParser#template}.
	 * @param ctx the parse tree
	 */
	void enterTemplate(TemplateParser.TemplateContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#template}.
	 * @param ctx the parse tree
	 */
	void exitTemplate(TemplateParser.TemplateContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#placeholder}.
	 * @param ctx the parse tree
	 */
	void enterPlaceholder(TemplateParser.PlaceholderContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#placeholder}.
	 * @param ctx the parse tree
	 */
	void exitPlaceholder(TemplateParser.PlaceholderContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(TemplateParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(TemplateParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TemplateParser#iter}.
	 * @param ctx the parse tree
	 */
	void enterIter(TemplateParser.IterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TemplateParser#iter}.
	 * @param ctx the parse tree
	 */
	void exitIter(TemplateParser.IterContext ctx);
}