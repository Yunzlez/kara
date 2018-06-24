package be.zlz.kara.bin.template.parser;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.Map;

public class TemplateBuilderVisitor extends TemplateParserBaseVisitor {

    private StringBuilder sb = new StringBuilder();

    private Map<String, String> plainData;
    private Map<String, List<String>> iterableData;
    private Map<String, Map<String, String>> objectData;

    public TemplateBuilderVisitor(Map<String, String> plainData, Map<String, List<String>> iterableData, Map<String, Map<String, String>> objectData) {
        this.plainData = plainData;
        plainData.put("dol", "$");
        this.iterableData = iterableData;
        this.objectData = objectData;
    }

    @Override
    public Object visitTemplate(TemplateParser.TemplateContext ctx) {
        for (int i = 0; i < ctx.children.size() - 1; i++) { //get rid of EOF
            ParseTree child = ctx.children.get(i);
            if (child.getChildCount() == 0) {
                sb.append(child.getText());
            } else {
                sb.append(this.visitPlaceholder(((TemplateParser.PlaceholderContext) child)));
            }
        }
        return sb.toString();
    }

    @Override
    public Object visitPlaceholder(TemplateParser.PlaceholderContext ctx) {
        TemplateParser.ExprContext exprContext = ctx.expr();
        return exprContext.accept(this);
    }

    @Override
    public Object visitPlainPlaceholder(TemplateParser.PlainPlaceholderContext ctx) {
        String data = plainData.get(ctx.ID().getText().toLowerCase());
        if(data == null){
            data = "";
        }
        return data;
    }

    @Override
    public Object visitIterPlaceholder(TemplateParser.IterPlaceholderContext ctx) {
        List<String> iterData = iterableData.get(ctx.iter().ID().getText());
        int index = Integer.parseInt(ctx.iter().NUMBER().getText());
        if(iterData != null && index < iterData.size()){
            return iterData.get(index);
        } else {
            return "";
        }
    }

    @Override
    public Object visitObjectPlaceHolder(TemplateParser.ObjectPlaceHolderContext ctx) {
        Map<String, String> object = objectData.get(ctx.ID(0).getText());
        String data;
        if(object != null && (data = object.get(ctx.ID(1).getText())) != null){
            return data;
        } else {
            return "";
        }
    }
}
