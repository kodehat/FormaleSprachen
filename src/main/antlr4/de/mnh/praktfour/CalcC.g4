grammar CalcC;

prog: stat+
    ;

stat: expr 'n'                             # PrintExpr
    | ID '=' expr 'n'                      # Assign
    | 'clear' 'n'                          # Clear
    | 'n'                                  # Blank
    ;

expr: <assoc=right> expr op='^' expr       # Pot
    | expr op=('*' | '/') expr             # MulDiv
    | expr op=('+' | '-') expr             # AddSub
    | expr op=('==' | '<' | '>') expr      # EqLtGt
    | cond=expr '?' o1=expr ':' o2=expr    # CondExpr
    | INT                                  # Int
    | ID                                   # Id
    | '(' expr ')'                         # Parens
    ;

POT   : '^' ;
MUL   : '*' ;
DIV   : '/' ;
ADD   : '+' ;
SUB   : '-' ;
EQ    : '==' ;
LT    : '<' ;
GT    : '>' ;
ID    : [a-zA-Z]+ ;
INT   : [0-9]+ ;
WS    : [ \t\r\n]+ -> skip ;