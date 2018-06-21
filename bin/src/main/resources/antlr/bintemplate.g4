grammar bintemplate;

template
    : (TEXT | placeholder)* EOF
    ;

placeholder
    : '{{' ID '}}'
    ;

ID : LETTER+;

TEXT: ANY+;

LETTER:[a-zA-Z];

ANY: .?~[{}];