grammar CalcB;

@header {
import java.util.*;
}

@parser::members {
/** "memory" for the calculator; variable/value pairs go here */
Map<String, Integer> memory = new HashMap<>();

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
        case EQ: return left == right ? 1 : 0;
        case LT: return left < right ? 1 : 0;
        case GT: return left > right ? 1 : 0;
    }
    return 0;
}
}

prog returns [int v]
    : stat+                                 {$v = $stat.v;}
    ;

stat returns [int v]
    : expr 'n'                              {$v = $expr.v; System.out.println($expr.v);}
    | ID '=' expr 'n'                       {$v = $expr.v; memory.put($ID.text, $expr.v);}
    | 'clear' 'n'                           {$v = 0; memory.clear();}
    | 'n'
    ;

expr returns [int v]
    : <assoc=right> a=expr op='^' b=expr    {$v = eval($a.v, $op.type, $b.v);}
    | a=expr op=('*' | '/') b=expr          {$v = eval($a.v, $op.type, $b.v);}
    | a=expr op=('+' | '-') b=expr          {$v = eval($a.v, $op.type, $b.v);}
    | a=expr op=('==' | '<' | '>') b=expr   {$v = eval($a.v, $op.type, $b.v);}
    | cond=expr '?' o1=expr ':' o2=expr     {$v = $cond.v > 0 ? $o1.v : $o2.v;}
    | '(' expr ')'                          {$v = $expr.v;}
    | INT                                   {$v = $INT.int;}
    | ID
      {
      String id = $ID.text;
      $v = memory.containsKey(id) ? memory.get(id) : 0;
      }
    | '(' expr ')'                          {$v = $expr.v;}
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