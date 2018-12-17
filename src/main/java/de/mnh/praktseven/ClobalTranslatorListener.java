package de.mnh.praktseven;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClobalTranslatorListener extends ClobalBaseListener {
    public class Tuple<K, V> {

        public K index;
        public V value;

        public Tuple(K first, V second) {
            this.index = first;
            this.value = second;
        }
    }

    private STGroup stGroup;
    private ST template;

    private List<ST> functions = new LinkedList<>();
    private List<ST> stats = new LinkedList<>();

    private int globalVarDecls = 0;
    private int globalNextIndex = 0;

    private String currentFunction;

    private ParseTreeProperty<ST> templates = new ParseTreeProperty<>();
    private Map<String, Tuple<Integer, Integer>> stack = new HashMap<>();

    public ClobalTranslatorListener(STGroupFile file) {
        this.stGroup = file;
        this.template = t("program");
    }

    public void setValue(ParseTree node, ST value) {
        templates.put(node, value);
    }

    public ST getValue(ParseTree node) {
        return templates.get(node);
    }

    public ST t(String instanceName) {
        return stGroup.getInstanceOf(instanceName);
    }

    public ST getTemplate() {
        template.add("globals", globalVarDecls);
        if (stats != null) template.add("stats", stats);
        template.add("functions", functions);
        return template;
    }

    @Override
    public void enterFunctionDecl(ClobalParser.FunctionDeclContext ctx) {
        currentFunction = ctx.ID().getText();
    }

    @Override
    public void exitFunctionDecl(ClobalParser.FunctionDeclContext ctx) {
        functions.add(t("function")
                .add("name", currentFunction)
                .add("args", 0)
                .add("stats", getValue(ctx.block())));

        currentFunction = null;
    }

    @Override
    public void exitBlock(ClobalParser.BlockContext ctx) {
        List<ST> stList = ctx.stat().stream().map(s -> getValue(s.expr())).collect(Collectors.toList());
        if (currentFunction != null) {
            stats = stList;
        }
        System.out.println(stList);
    }

    @Override
    public void exitVarDeclaration(ClobalParser.VarDeclarationContext ctx) {
        globalVarDecls++;
    }

    @Override
    public void exitPreMinus(ClobalParser.PreMinusContext ctx) {
        setValue(ctx, t("uminus").add("expr", getValue(ctx.expr())));
    }

    @Override
    public void exitNegate(ClobalParser.NegateContext ctx) {
        setValue(ctx, t("negation").add("expr", getValue(ctx.expr())));
    }

    @Override
    public void exitBinOp(ClobalParser.BinOpContext ctx) {
        switch (ctx.op.getType()) {
            case ClobalParser.ADD:
                setValue(ctx, t("add")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.SUB:
                setValue(ctx, t("sub")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.MUL:
                setValue(ctx, t("mult")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.DIV:
                setValue(ctx, t("div")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.EQ:
                setValue(ctx, t("eq")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.NEQ:
                setValue(ctx, t("neq")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.GT:
                setValue(ctx, t("gt")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.LT:
                setValue(ctx, t("lt")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
        }
    }

    @Override
    public void exitInt(ClobalParser.IntContext ctx) {
        setValue(ctx, t("int")
                .add("int", ctx.INT().getText()));
    }

    @Override
    public void exitAssign(ClobalParser.AssignContext ctx) {
        int index = globalNextIndex++;
        stack.put(ctx.ID().getText(), new Tuple<>(index, Integer.valueOf(ctx.expr().getText())));
        setValue(ctx, t("assign")
                .add("expr", getValue(ctx.expr()))
                .add("index", index));
    }

    @Override
    public void exitPrint(ClobalParser.PrintContext ctx) {
        setValue(ctx, t("print")
                .add("expr", getValue(ctx.expr())));
    }
}
