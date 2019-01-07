grammar Clobal;

file: (functionDecl | varDecl)+
    ;

functionDecl
    : type ID '('')' block
    ;

block
    : '{' stat* '}'
    ;

stat: block
    | ifStat
    | forStat
    | returnStat ';'
    | assignStat ';'
    | printStat ';'
    | expr ';'
    ;
     
assignStat
    : ID '=' expr
    ;

ifStat
    : 'if' '('expr ')' stat ('else' stat)?
    ;

forStat
    : 'for' '(' assignStat ';' expr ';' assignStat ')' block
    ;

returnStat
    : 'return' expr
    ;

printStat
    : 'printf' '(' expr ')'
    ;

varDecl
    : type ID ';'
    ;

type: 'int'
    ;

expr: '-' expr                          #Uminus
    | '!' expr                          #Not
    | expr op=('*'|'/') expr            #BinOp
    | expr op=('+'|'-') expr            #BinOp
    | expr op=('=='|'!='|'<'|'>') expr  #BinOp
    | expr '?' expr ':' expr            #CondExpr
    | ID                                #Id
    | INT                               #Int
    | '(' expr ')'                      #Parens
    | ID '(' ')'                        #FuncCall
    ;

fragment LETTER
    : [a-zA-Z]
    ;

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
EQ  : '==' ;
NEQ : '!=' ;
LT  : '<' ;
GT  : '>' ;
INT : [0-9]+ ;
ID  : LETTER (LETTER | [0-9])* ;
WS  : [ \t\r\n]+ -> skip ;
SL_COMMENT
    : '//' .*? '\n' -> skip
    ;