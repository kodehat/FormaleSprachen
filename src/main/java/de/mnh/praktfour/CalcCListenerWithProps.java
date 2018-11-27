package de.mnh.praktfour;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.HashMap;
import java.util.Map;

public class CalcCListenerWithProps extends CalcCBaseListener {

    ParseTreeProperty<Integer> values = new ParseTreeProperty<>();
    Map<String, Integer> memory = new HashMap<>();

    public void setValue(ParseTree node, int value) {
        values.put(node, value);
    }

    public int getValue(ParseTree node) {
        return values.get(node);
    }

    /** stat+ */
    @Override
    public void exitProg(CalcCParser.ProgContext ctx) {
        setValue(ctx, getValue(ctx.stat(ctx.stat().size() - 1)));
    }

    /** expr 'n' */
    @Override
    public void exitPrintExpr(CalcCParser.PrintExprContext ctx) {
        int value = getValue(ctx.expr());
        System.out.println(value);
        setValue(ctx, value);
    }

    /** ID '=' expr 'n' */
    @Override
    public void exitAssign(CalcCParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        int value = getValue(ctx.expr());
        memory.put(id, value);
        setValue(ctx, value);
    }

    /** 'clear' 'n' */
    @Override
    public void exitClear(CalcCParser.ClearContext ctx) {
        memory.clear();
    }

    /** <assoc=right> expr op='^' expr */
    @Override
    public void exitPot(CalcCParser.PotContext ctx) {
        int left = getValue(ctx.expr(0));
        int right = getValue(ctx.expr(1));
        // POT
        setValue(ctx, (int) Math.pow(left, right));
    }

    /** expr op=('*' | '/') expr */
    @Override
    public void exitMulDiv(CalcCParser.MulDivContext ctx) {
        int left = getValue(ctx.expr(0));
        int right = getValue(ctx.expr(1));
        // MUL
        if (ctx.op.getType() == CalcCParser.MUL) {
            setValue(ctx, left * right);

        // DIV
        } else {
            if (right == 0) {
                throw new ArithmeticException("Division by zero");
            }
            setValue(ctx, left / right);
        }
    }

    /** expr op=('+' | '-') expr */
    @Override
    public void exitAddSub(CalcCParser.AddSubContext ctx) {
        int left = getValue(ctx.expr(0));
        int right = getValue(ctx.expr(1));
        // ADD
        if (ctx.op.getType() == CalcCParser.ADD) {
            setValue(ctx, left + right);

        // SUB
        } else {
            setValue(ctx, left - right);
        }
    }

    /** expr op=('==' | '<' | '>') expr */
    @Override
    public void exitEqLtGt(CalcCParser.EqLtGtContext ctx) {
        int left = getValue(ctx.expr(0));
        int right = getValue(ctx.expr(1));
        // EQ
        if (ctx.op.getType() == CalcCParser.EQ) {
            setValue(ctx, left == right ? 1 : 0);

            // LT
        } else if (ctx.op.getType() == CalcCParser.LT) {
            setValue(ctx, left < right ? 1 : 0);

            // GT
        } else {
            setValue(ctx, left > right ? 1 : 0);
        }
    }

    /** cond=expr '?' o1=expr ':' o2=expr */
    @Override
    public void exitCondExpr(CalcCParser.CondExprContext ctx) {
        int value = getValue(ctx.cond) > 0 ? getValue(ctx.o1) : getValue(ctx.o2);
        setValue(ctx, value);
    }

    /** INT */
    @Override
    public void exitInt(CalcCParser.IntContext ctx) {
        String intText = ctx.INT().getText();
        setValue(ctx, Integer.valueOf(intText));
    }

    /** ID */
    @Override
    public void exitId(CalcCParser.IdContext ctx) {
        String id = ctx.ID().getText();
        setValue(ctx, memory.getOrDefault(id, 0));
    }

    /** '(' expr ')' */
    @Override
    public void exitParens(CalcCParser.ParensContext ctx) {
        setValue(ctx, getValue(ctx.expr()));
    }


}
