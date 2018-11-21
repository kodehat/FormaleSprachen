package de.mnh.praktfour;

public class CalcAExprVisitor extends CalcABaseVisitor<Integer> {

    /** expr n */
    @Override
    public Integer visitPrintExpr(CalcAParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr());
        System.out.println(value);
        return value;
    }

    /** INT */
    @Override
    public Integer visitInt(CalcAParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    /** expr op=('*'|'/') expr */
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

    /** expr op=('+'|'-') expr */
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

    /** <assoc=right> expr op='^' expr */
    @Override
    public Integer visitPot(CalcAParser.PotContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        // POT
        return (int) Math.pow(left, right);
    }

    /** '(' expr ')' */
    @Override
    public Integer visitParens(CalcAParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}
