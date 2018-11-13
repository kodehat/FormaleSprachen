grammar ExerciseTwo;

prog: stat+ ;

stat: expr NL
    | NL
    ;

expr: term
    | term (('+'|'-') term)+
    ;

term: fact
    | fact (('*'|'/') fact)+
    ;

fact: ('+'|'-') fact
    | '(' expr ')'
    | INT
    ;

INT : [0-9]+ ;
NL  : '\r'? '\n' ;
WS  : [ \t]+ -> skip ;