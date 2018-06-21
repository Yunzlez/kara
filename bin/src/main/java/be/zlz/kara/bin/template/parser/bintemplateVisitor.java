// Generated from C:/JEE/Projects/zlzbin-j/bin/src/main/resources/antlr\bintemplate.g4 by ANTLR 4.7
package be.zlz.kara.bin.template.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link bintemplateParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface bintemplateVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link bintemplateParser#template}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplate(bintemplateParser.TemplateContext ctx);
	/**
	 * Visit a parse tree produced by {@link bintemplateParser#placeholder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlaceholder(bintemplateParser.PlaceholderContext ctx);
}