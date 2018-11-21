grammar CalcC;

stat: expr 'n'
    | 'n'
    ;

expr: '(' expr ')'                         # Parens
    | <assoc=right> expr op='^' expr       # Pot
    | expr op=('*' | '/') expr             # MulDiv
    | expr op=('+' | '-') expr             # AddSub
    | INT                                  # Int
    ;

LBRAC : '(' ;
RBRAC : ')' ;
POT   : '^' ;
MUL   : '*' ;
DIV   : '/' ;
ADD   : '+' ;
SUB   : '-' ;

INT   : [0-9]+ ;
WS    : [ \t\r\n]+ -> skip ;