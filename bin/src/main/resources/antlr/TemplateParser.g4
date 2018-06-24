parser grammar TemplateParser;

options {
    tokenVocab=TemplateLexer;
    }

template
    : (TMP_TEXT | TMP_ESC | placeholder)* EOF
    ;

placeholder
    : OPEN_TMP expr CLOSE_TMP
    ;

expr: ID            #PlainPlaceholder
    | iter          #IterPlaceholder
    | ID DOT ID     #ObjectPlaceHolder
    ;

iter: ID NUMBER
    ;

