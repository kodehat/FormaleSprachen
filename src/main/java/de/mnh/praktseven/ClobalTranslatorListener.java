package de.mnh.praktseven;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
import java.util.stream.Collectors;

public class ClobalTranslatorListener extends ClobalBaseListener {

    private STGroup stGroup;

    private String currentFunction;

    private int i = 0;

    private List<ST> functionTemplates = new LinkedList<>();

    private Map<String, Integer> variables = new HashMap<>();

    private ParseTreeProperty<ST> templates = new ParseTreeProperty<>();

    public ClobalTranslatorListener(STGroupFile file) {
        this.stGroup = file;
    }

    private void setValue(ParseTree node, ST value) {
        templates.put(node, value);
    }

    private ST getValue(ParseTree node) {
        return templates.get(node);
    }

    /**
     * Add a new variable to the variable map
     * with the next possible index.
     *
     * @param name name of the variable
     */
    private void addVariable(String name) {
        if (!variables.containsKey(name)) {
            variables.put(name, variables.size());
        } else {
            throw new RuntimeException(String.format("Variable %s already declared!", name));
        }
    }

    /**
     * Returns the index of a declared variable.
     * Throws a {@link RuntimeException}, if
     * variable not declared.
     *
     * @param name name of the variable
     * @return index of the variable
     */
    private int getVariableIndex(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else {
            throw new RuntimeException(String.format("Variable %s not declared!", name));
        }
    }

    private ST t(String instanceName) {
        return stGroup.getInstanceOf(instanceName);
    }

    public String getGeneratedCode() {
        return t("program")
            .add("globalsCount", variables.size())
            .add("functions", functionTemplates)
            .render();
    }

    @Override
    public void enterFunctionDecl(ClobalParser.FunctionDeclContext ctx) {
        currentFunction = ctx.ID().getText();
    }

    @Override
    public void exitFunctionDecl(ClobalParser.FunctionDeclContext ctx) {
        functionTemplates.add(t("functionDecl")
            .add("name", ctx.ID())
            .add("block", getValue(ctx.block()))
        );
        currentFunction = null;
    }

    @Override
    public void exitBlock(ClobalParser.BlockContext ctx) {
        setValue(ctx, t("block")
            .add("stats", ctx.stat().stream().map(s -> getValue(s)).collect(Collectors.toList()))
        );
    }

    @Override
    public void exitStat(ClobalParser.StatContext ctx) {
        if (ctx.expr() != null) {
            setValue(ctx, getValue(ctx.expr()));
        } else if (ctx.printStat() != null) {
            setValue(ctx, getValue(ctx.printStat()));
        } else if (ctx.assignStat() != null) {
            setValue(ctx, getValue(ctx.assignStat()));
        } else if (ctx.returnStat() != null) {
            setValue(ctx, getValue(ctx.returnStat()));
        } else if (ctx.forStat() != null) {
            setValue(ctx, getValue(ctx.forStat()));
        } else if (ctx.ifStat() != null) {
            setValue(ctx, getValue(ctx.ifStat()));
        } else if (ctx.block() != null) {
            setValue(ctx, getValue(ctx.block()));
        }
    }

    @Override
    public void exitVarDecl(ClobalParser.VarDeclContext ctx) {
        addVariable(ctx.ID().getText());
    }

    @Override
    public void exitUminus(ClobalParser.UminusContext ctx) {
        // Add a new temporary global variable
        setValue(ctx, t("uminus")
            .add("value", getValue(ctx.expr()))
        );
    }

    @Override
    public void exitNot(ClobalParser.NotContext ctx) {
        setValue(ctx, t("not")
            .add("value", getValue(ctx.expr()))
            .add("i", (i++) + "")
        );
    }

    @Override
    public void exitBinOp(ClobalParser.BinOpContext ctx) {
        switch (ctx.op.getType()) {
            case ClobalParser.ADD:
                setValue(ctx, t("add")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                );
                break;
            case ClobalParser.SUB:
                setValue(ctx, t("sub")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                );
                break;
            case ClobalParser.MUL:
                setValue(ctx, t("mult")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                );
                break;
            case ClobalParser.DIV:
                setValue(ctx, t("div")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                );
                break;
            case ClobalParser.EQ:
                setValue(ctx, t("eq")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                );
                break;
            case ClobalParser.NEQ:
                setValue(ctx, t("neq")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                        .add("i", (i++) + "")
                );
                break;
            case ClobalParser.GT:
                setValue(ctx, t("gt")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                );
            case ClobalParser.LT:
                setValue(ctx, t("lt")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1)))
                );
                break;
        }
    }

    @Override
    public void exitCondExpr(ClobalParser.CondExprContext ctx) {
        setValue(ctx, t("cond")
            .add("cond", getValue(ctx.expr(0)))
            .add("t", getValue(ctx.expr(1)))
            .add("f", getValue(ctx.expr(2)))
            .add("i", (i++) + "")
        );
    }

    @Override
    public void exitParens(ClobalParser.ParensContext ctx) {
        setValue(ctx, getValue(ctx.expr()));
    }

    @Override
    public void exitFuncCall(ClobalParser.FuncCallContext ctx) {
        setValue(ctx, t("funcCall")
            .add("name", ctx.ID().getText())
        );
    }

    @Override
    public void exitId(ClobalParser.IdContext ctx) {
        setValue(ctx, t("id")
            .add("index", getVariableIndex(ctx.ID().getText()))
        );
    }

    @Override
    public void exitInt(ClobalParser.IntContext ctx) {
        setValue(ctx, t("int")
                .add("value", ctx.INT().getText())
        );
    }

    @Override
    public void exitAssignStat(ClobalParser.AssignStatContext ctx) {
        setValue(ctx, t("assign")
            .add("index", getVariableIndex(ctx.ID().getText()))
            .add("value", getValue(ctx.expr()))
        );
    }

    @Override
    public void exitPrintStat(ClobalParser.PrintStatContext ctx) {
        setValue(ctx, t("print")
            .add("value", getValue(ctx.expr()))
        );
    }

    @Override
    public void exitReturnStat(ClobalParser.ReturnStatContext ctx) {
        if (currentFunction.equals("main")) {
            setValue(ctx, t("halt"));
        } else {
            setValue(ctx, t("return")
                .add("value", getValue(ctx.expr()))
            );
        }
    }

    @Override
    public void exitIfStat(ClobalParser.IfStatContext ctx) {
        setValue(ctx, t("ifElse")
            .add("cond", getValue(ctx.expr()))
            .add("t", getValue(ctx.stat(0)))
            .add("f", getValue(ctx.stat(1)))
            .add("i", (i++) + "")
        );
    }

    @Override
    public void exitForStat(ClobalParser.ForStatContext ctx) {
        setValue(ctx, t("for")
            .add("assign", getValue(ctx.assignStat(0)))
            .add("cond", getValue(ctx.expr()))
            .add("step", getValue(ctx.assignStat(1)))
            .add("block", getValue(ctx.block()))
            .add("i", (i++) + "")
        );
    }
}
