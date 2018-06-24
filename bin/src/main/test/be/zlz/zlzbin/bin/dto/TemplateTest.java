package be.zlz.zlzbin.bin.dto;

import be.zlz.kara.bin.template.parser.TemplateBuilderVisitor;
import be.zlz.kara.bin.template.parser.TemplateLexer;
import be.zlz.kara.bin.template.parser.TemplateParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.util.*;

public class TemplateTest {

    @Test
    public void testSimpleTemplate(){
        CharStream stream = CharStreams.fromString("My Template is ${ting}, look at this list item: ${i0}\n" +
                "\n" +
                "my objects' property is ${object.property}\n" +
                "\n" +
                "I wanna use ${dol}!\n" +
                "\n" +
                "{\n" +
                "    \"isJson\":\"${isJson}\"\n" +
                "}");

        TemplateLexer lexer = new TemplateLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TemplateParser parser = new TemplateParser(tokenStream);


        Map<String, String> plainData = new HashMap<>();
        Map<String, List<String>> iterableData = new HashMap<>();
        Map<String, Map<String, String>> objectData = new HashMap<>();

        plainData.put("ting", "awesome");
        plainData.put("isjson", "true");

        List<String> items = Arrays.asList("item0", "item1", "item2");
        iterableData.put("i", items);

        Map<String, String> props = new HashMap<>();
        props.put("property", "myPropertyValue");
        props.put("property2", "myProperty2Value");
        objectData.put("object", props);

        TemplateBuilderVisitor visitor = new TemplateBuilderVisitor(plainData, iterableData, objectData);
        System.out.println(visitor.visitTemplate(parser.template()));
    }
}
