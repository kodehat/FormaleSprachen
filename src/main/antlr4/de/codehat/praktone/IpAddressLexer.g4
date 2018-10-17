lexer grammar IpAddressLexer;

// Token definitions
IP   : BYTE DOT BYTE DOT BYTE DOT BYTE
     ;

// Regular expressions used in token definitions
fragment BYTE    : [0-9] | [1-9][0-9] | '1'[0-9][0-9] | '2'[0-5][0-5]
                 ;
fragment DOT     : '.'
                 ;

// Whitespace to be ignored
WS   : [ \t\r\n]+ ->skip
     ;