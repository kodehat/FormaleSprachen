lexer grammar HalsteadLexer;

OPERAND           : IDENTIFIER | TYPESPEC | CONSTANT
                  ;

OPERATOR          : SCSPEC | TYPE_QUAL | RESERVED | OPERATORS
                  ;

IGNORE            : COMMENTS | CLOSED_BRACKETS | COLON | DO | INCLUDE
                  ;

// OPERAND
fragment TYPESPEC : 'bool' | 'char' | 'double' | 'float' | 'int' | 'long' | 'short' | 'signed' | 'unsigned' | 'void'
                  ;

fragment IDENTIFIER
                  : LETTER_AND_UNDERSCORE (LETTER_AND_UNDERSCORE | DIGIT)*
                  ;

fragment CONSTANT : DIGIT+ | '\'' .*? '\'' | '"' .*? '"'
                  ;

// OPERATOR
fragment SCSPEC   : 'auto' | 'extern' | 'inline' | 'register' | 'static' | 'typedef' | 'virtual' | 'mutable'
                  ;

fragment TYPE_QUAL
                  : 'const' | 'friend' | 'volatile'
                  ;

fragment RESERVED : 'asm' | 'break' | 'case' | 'class' | 'continue' | 'default' | 'delete' | 'while(' | 'else' | 'enum'
                    | 'for(' | 'goto' | 'if(' | 'new' | 'operator' | 'private' | 'protected' | 'public' | 'return'
                    | 'sizeof' | 'struct' | 'switch(' | 'this' | 'union' | 'namespace' | 'using' | 'try' | 'catch'
                    | 'throw' | 'const_cast' | 'static_cast' | 'dynamic_cast' | 'reinterpret_cast' | 'typeid'
                    | 'template' | 'explicit' | 'true' | 'false' | 'typename'
                  ;

fragment OPERATORS
                  : '!' | '!=' | '%' | '%=' | '&' | '&&' | '&=' | '(' | '*' | '*=' | '+' | '++' | '+=' | ',' | '-'
                    | '--' | '-=' | '->' | '...' | '/' | '/=' | '::' | '<' | '<<' | '<<=' | '<=' | '==' | '>' | '>='
                    | '>>' | '>>=' | '?' | '[' | '^' | '^=' | '{' | '||' | '=' | '~' | ';' | '|'
                  ;

// IGNORE
fragment COMMENTS : '//' .*? '\n' | '/*' .*? '*/' | '//*' .*? '*//'
                  ;

fragment CLOSED_BRACKETS
                  : ')' | ']' | '}'
                  ;

fragment COLON    : ':'
                  ;

fragment DO       : 'do'
                  ;

fragment INCLUDE  : '#include' .*? '\n'
                  ;

// OTHER
fragment LETTER_AND_UNDERSCORE
                  : [a-zA-Z_]
                  ;

fragment DIGIT    : [0-9]
                  ;

// Whitespace to be ignored
WS   : [ \t\r\n]+ ->skip
     ;