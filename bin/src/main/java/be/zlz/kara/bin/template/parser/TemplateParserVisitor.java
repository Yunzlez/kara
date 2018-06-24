// Generated from C:/JEE/Projects/zlzbin-j/bin/src/main/resources/antlr\TemplateParser.g4 by ANTLR 4.7
package be.zlz.kara.bin.template.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TemplateParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TemplateParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TemplateParser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(TemplateParser.TemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link TemplateParser#placeholder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlaceholder(TemplateParser.PlaceholderContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PlainPlaceholder}
	 * labeled alternative in {@link TemplateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlainPlaceholder(TemplateParser.PlainPlaceholderContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IterPlaceholder}
	 * labeled alternative in {@link TemplateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterPlaceholder(TemplateParser.IterPlaceholderContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjectPlaceHolder}
	 * labeled alternative in {@link TemplateParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPlaceHolder(TemplateParser.ObjectPlaceHolderContext ctx);
	/**
	 * Visit a parse tree produced by {@link TemplateParser#iter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIter(TemplateParser.IterContext ctx);
}