grammar CalcB;

@parser::members {
int eval(int left, int op, int right) {
    switch (op) {
        case POT: return (int) Math.pow(left, right);
        case MUL: return left * right;
        case DIV:
            if (right == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return left / right;
        case ADD: return left + right;
        case SUB: return left - right;
    }
    return 0;
}
}

stat: expr 'n'                              {System.out.println($expr.v);}
    | 'n'
    ;

expr returns [int v]
    : '(' expr ')'                          {$v = $expr.v;}
    | <assoc=right> a=expr op='^' b=expr    {$v = eval($a.v, $op.type, $b.v);}
    | a=expr op=('*' | '/') b=expr          {$v = eval($a.v, $op.type, $b.v);}
    | a=expr op=('+' | '-') b=expr          {$v = eval($a.v, $op.type, $b.v);}
    | '(' expr ')'                          {$v = $expr.v;}
    | INT                                   {$v = $INT.int;}
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