package de.mnh.praktfour;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class CalcCListenerWithProps extends CalcCBaseListener {

    ParseTreeProperty<Integer> values = new ParseTreeProperty<>();

    public void setValue(ParseTree node, int value) {
        values.put(node, value);
    }

    public int getValue(ParseTree node) {
        return values.get(node);
    }

    @Override
    public void exitParens(CalcCParser.ParensContext ctx) {
        setValue(ctx, getValue(ctx.expr()));
    }

    @Override
    public void exitPot(CalcCParser.PotContext ctx) {
        int left = getValue(ctx.expr(0));
        int right = getValue(ctx.expr(1));
        // POT
        setValue(ctx, (int) Math.pow(left, right));
    }

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

    @Override
    public void exitInt(CalcCParser.IntContext ctx) {
        String intText = ctx.INT().getText();
        setValue(ctx, Integer.valueOf(intText));
    }

    @Override
    public void exitStat(CalcCParser.StatContext ctx) {
        setValue(ctx, getValue(ctx.expr()));
    }
}
