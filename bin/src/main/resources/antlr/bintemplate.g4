grammar bintemplate;

template
    : (TEXT | placeholder)* EOF
    ;

placeholder
    : '{{' expr '}}'
    ;

expr: ID
    | iter
    | (ID | iter) '.' ID
    ;

iter: ID DIGIT+
    ;

ID : LETTER+;

TEXT: ANY+;

DIGIT: [0-9];
LETTER:[a-zA-Z];

ANY: .?~[{}];