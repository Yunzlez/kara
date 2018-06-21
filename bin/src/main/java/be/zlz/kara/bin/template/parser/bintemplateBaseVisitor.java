// Generated from C:/JEE/Projects/zlzbin-j/bin/src/main/resources/antlr\bintemplate.g4 by ANTLR 4.7
package be.zlz.kara.bin.template.parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link bintemplateVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class bintemplateBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements bintemplateVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitTemplate(bintemplateParser.TemplateContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitPlaceholder(bintemplateParser.PlaceholderContext ctx) { return visitChildren(ctx); }
}