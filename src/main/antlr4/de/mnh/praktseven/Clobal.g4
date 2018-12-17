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
    : ID '=' expr                                               #Assign
    ;

ifStat
    : 'if' '('expr ')' stat ('else' stat)?                      #IfElse
    ;

forStat
    : 'for' '(' assignStat ';' expr ';' assignStat ')' block    #For
    ;

returnStat
    : 'return' expr                                             #Return
    ;

printStat
    : 'printf' '(' expr ')'                                     #Print
    ;

varDecl
    : type ID ';'                                               #VarDeclaration
    ;

type: 'int'
    ;

expr: '-' expr                          #PreMinus
    | '!' expr                          #Negate
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

POT : '^' ;
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