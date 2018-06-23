lexer grammar TemplateLexer;

OPEN_TMP: '${'-> pushMode(TMP);

TMP_ESC: '\\$';

TMP_TEXT: ~'$'+;

mode TMP;

CLOSE_TMP: '}' -> popMode;

fragment ESC :   '\\' ([{}"\\/bfnrt] | UNICODE) ; //escapes
fragment UNICODE : 'u' HEX HEX HEX HEX ; //unicode characters
fragment HEX : [0-9a-fA-F] ; // hex characters

DOT:'.';
ID: LETTER+;
LETTER : [a-zA-Z];
NUMBER: DIGIT+;
DIGIT:[0-9];

WS: [ \t\n\r] -> skip; //dump whitespace