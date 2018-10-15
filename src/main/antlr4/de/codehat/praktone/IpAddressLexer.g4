lexer grammar IpAddressLexer;

// Token definitions
IP   : PART DOT PART DOT PART DOT PART
     ;

// Regular expressions used in token definitions
fragment PART    : [0-9] | [0-9][0-9] | '1'[0-9][0-9] | '2'[0-5][0-5]
                 ;
fragment DOT     : '.'
                 ;

// Whitespace to be ignored
WS   : [ \t\r\n]+ ->skip
     ;