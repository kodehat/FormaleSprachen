package de.mnh.praktfour;

import java.util.HashMap;
import java.util.Map;

public class CalcAExprVisitor extends CalcABaseVisitor<Integer> {

    /** "memory" for the calculator; variable/value pairs go here */
    Map<String, Integer> memory = new HashMap<>();

    /** expr 'n' */
    @Override
    public Integer visitPrintExpr(CalcAParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr());
        System.out.println(value);
        return value;
    }

    /** ID '=' expr 'n' */
    @Override
    public Integer visitAssign(CalcAParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        int value = visit(ctx.expr());
        memory.put(id, value);
        return value;
    }

    /** 'if' '('cond=expr')' o1=stat 'else' o2=stat */
    @Override
    public Integer visitIfElse(CalcAParser.IfElseContext ctx) {
        return visit(ctx.cond) > 0 ? visit(ctx.o1) : visit(ctx.o2);
    }

    /** 'clear' 'n' */
    @Override
    public Integer visitClear(CalcAParser.ClearContext ctx) {
        memory.clear();
        return 0;
    }

    /** <assoc=right> expr op='^' expr */
    @Override
    public Integer visitPot(CalcAParser.PotContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        // POT
        return (int) Math.pow(left, right);
    }

    /** expr op=('*' | '/') expr */
    @Override
    public Integer visitMulDiv(CalcAParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        // MUL
        if (ctx.op.getType() == CalcAParser.MUL) {
            return left * right;
        }
        // DIV
        if (right == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left / right;
    }

    /** expr op=('+' | '-') expr */
    @Override
    public Integer visitAddSub(CalcAParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        // ADD
        if (ctx.op.getType() == CalcAParser.ADD) {
            return left + right;
        }
        // SUB
        return left - right;
    }

    /** expr op=('==' | '<' | '>') expr */
    @Override
    public Integer visitEqLtGt(CalcAParser.EqLtGtContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        // EQ
        if (ctx.op.getType() == CalcAParser.EQ) {
            return left == right ? 1 : 0;
        }
        // LT
        if (ctx.op.getType() == CalcAParser.LT) {
            return left < right ? 1 : 0;
        }
        // GT
        return left > right ? 1 : 0;
    }

    /** cond=expr '?' o1=expr ':' o2=expr */
    @Override
    public Integer visitCondExpr(CalcAParser.CondExprContext ctx) {
        return visit(ctx.cond) > 0 ? visit(ctx.o1) : visit(ctx.o2);
    }

    /** INT */
    @Override
    public Integer visitInt(CalcAParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    /** ID */
    @Override
    public Integer visitId(CalcAParser.IdContext ctx) {
        String id = ctx.ID().getText();
        return memory.getOrDefault(id, 0);
    }

    /** '(' expr ')' */
    @Override
    public Integer visitParens(CalcAParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}
