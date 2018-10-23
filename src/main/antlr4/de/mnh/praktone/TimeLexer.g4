lexer grammar TimeLexer;

// Token definitions
TIME   : HOUR COLON MINANDSEC (COLON MINANDSEC)?
       ;

// Regular expressions used in token definitions
fragment HOUR    : [0-1][0-9] | '2'[0-3]
                 ;
fragment MINANDSEC
                 : [0-5][0-9]
                 ;
fragment COLON   : ':'
                 ;

// Whitespace to be ignored
WS   : [ \t\r\n]+ ->skip
     ;