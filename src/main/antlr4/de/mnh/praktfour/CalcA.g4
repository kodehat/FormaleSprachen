grammar CalcA;

prog: stat+
    ;

stat: expr 'n'                                              # printExpr
    | ID '=' expr 'n'                                       # assign
    | 'if' '('cond=expr')' o1=stat 'else' o2=stat           # ifElse
    | 'clear' 'n'                                           # clear
    | 'n'                                                   # blank
    ;

expr: <assoc=right> expr op='^' expr                        # Pot
    | expr op=('*' | '/') expr                              # MulDiv
    | expr op=('+' | '-') expr                              # AddSub
    | expr op=('==' | '<' | '>') expr                       # EqLtGt
    | cond=expr '?' o1=expr ':' o2=expr                     # CondExpr
    | INT                                                   # int
    | ID                                                    # id
    | '(' expr ')'                                          # parens
    ;

POT : '^' ;
MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
EQ  : '==' ;
LT  : '<' ;
GT  : '>' ;
ID  : [a-zA-Z]+ ;
INT : [0-9]+ ;
WS  : [ \t\r\n]+ -> skip ;