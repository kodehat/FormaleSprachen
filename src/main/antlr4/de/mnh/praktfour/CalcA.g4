grammar CalcA;

stat: expr 'n'                             # printExpr
    | 'n'                                  # blank
    ;

expr: '(' expr ')'                         # parens
    | <assoc=right> expr op='^' expr       # Pot
    | expr op=('*' | '/') expr             # MulDiv
    | expr op=('+' | '-') expr             # AddSub
    | INT                                  # int
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