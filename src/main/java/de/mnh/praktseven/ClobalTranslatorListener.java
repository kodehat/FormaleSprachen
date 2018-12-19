package de.mnh.praktseven;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.*;
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

    private Map<String, List<ST>> functions = new HashMap<>();
    private List<ST> functionSt = new LinkedList<>();

    private List<ST> globalStats = new LinkedList<>();
    private int globalVarDecls = 0;
    private int globalNextIndex = 0;

    private String currentFunction;

    private ParseTreeProperty<ST> templates = new ParseTreeProperty<>();
    private ParseTreeProperty<List<ST>> templateList = new ParseTreeProperty<>();
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
        template.add("stats", globalStats);
        template.add("functions", functionSt);
        return template;
    }

    @Override
    public void enterFunctionDecl(ClobalParser.FunctionDeclContext ctx) {
        currentFunction = ctx.ID().getText();
        functions.put(currentFunction, new LinkedList<>());
    }

    @Override
    public void exitFunctionDecl(ClobalParser.FunctionDeclContext ctx) {
        if (currentFunction.equals("main")) functions.get(currentFunction).add(t("extramain"));
        functionSt.add(t("function")
                .add("name", currentFunction)
                .add("args", 0)
                .add("stats", functions.get(currentFunction)));
        currentFunction = null;
        System.out.println("FUNC DECL BLOCK:");
        templateList.get(ctx.block()).forEach(s -> System.out.println(s.render()));
    }

    @Override
    public void exitBlock(ClobalParser.BlockContext ctx) {
//        List<ST> stList = buildStatsFromContext(ctx.stat());
//        if (currentFunction == null) {
//            globalStats.addAll(stList);
//        } else {
//            functions.put(currentFunction, stList);
//        }
        System.out.println("BLOCK:");
        ctx.stat().forEach(s -> System.out.println(s.getClass().getSimpleName()));
        List<ST> t = new LinkedList<>();
        ctx.stat().forEach(s -> t.add(getValue(s)));
        templateList.put(ctx, t);
    }

    @Override
    public void exitBlockSt(ClobalParser.BlockStContext ctx) {
        functions.get(currentFunction).add(getValue(ctx.block()));
    }

    @Override
    public void exitVarDeclaration(ClobalParser.VarDeclarationContext ctx) {
        globalStats.add(t("vardecl")
            .add("type", ctx.type().getText())
            .add("id", ctx.ID().getText()));

        globalVarDecls++;
    }

    @Override
    public void exitPreMinus(ClobalParser.PreMinusContext ctx) {
        if (ctx.expr() instanceof ClobalParser.IdContext) {
            setValue(ctx, t("uminus")
                    .add("expr", getValue(ctx.expr()))
                    .add("index", globalNextIndex++));
            globalVarDecls++;
        } else if (ctx.expr() instanceof ClobalParser.IntContext) {
            setValue(ctx, t("int")
                    .add("int", -1 * Integer.parseInt(ctx.expr().getText())));
        }
    }

    @Override
    public void exitNegate(ClobalParser.NegateContext ctx) {
        setValue(ctx, t("negation")
                .add("expr", getValue(ctx.expr())));
    }

    @Override
    public void exitBinOp(ClobalParser.BinOpContext ctx) {
        switch (ctx.op.getType()) {
            case ClobalParser.ADD:
                setValue(ctx, t("add")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
                break;
            case ClobalParser.SUB:
                setValue(ctx, t("sub")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
                break;
            case ClobalParser.MUL:
                setValue(ctx, t("mult")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
                break;
            case ClobalParser.DIV:
                setValue(ctx, t("div")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
                break;
            case ClobalParser.EQ:
                setValue(ctx, t("eq")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
                break;
            case ClobalParser.NEQ:
                setValue(ctx, t("neq")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
                break;
            case ClobalParser.GT:
                setValue(ctx, t("gt")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
            case ClobalParser.LT:
                setValue(ctx, t("lt")
                        .add("a", getValue(ctx.expr(0)))
                        .add("b", getValue(ctx.expr(1))));
                break;
        }
    }

    @Override
    public void exitId(ClobalParser.IdContext ctx) {
        setValue(ctx, t("id")
            .add("id", stack.get(ctx.ID().getText()).index));
    }

    @Override
    public void exitInt(ClobalParser.IntContext ctx) {
        setValue(ctx, t("int")
                .add("int", ctx.INT().getText()));
    }

    @Override
    public void exitAssign(ClobalParser.AssignContext ctx) {
        int index = stack.containsKey(ctx.ID().getText()) ? stack.get(ctx.ID().getText()).index : globalNextIndex++;
        stack.put(ctx.ID().getText(), new Tuple<>(index, Integer.valueOf(ctx.expr().getText())));
        setValue(ctx, t("assign")
                .add("expr", getValue(ctx.expr()))
                .add("index", index));
        if (currentFunction != null) {
            //functions.get(currentFunction).add(getValue(ctx));
        }
    }

    @Override
    public void exitAssignSt(ClobalParser.AssignStContext ctx) {
        setValue(ctx, getValue(ctx.assignStat()));
    }

    @Override
    public void exitExprSt(ClobalParser.ExprStContext ctx) {
        setValue(ctx, getValue(ctx.expr()));
    }

    @Override
    public void exitPrint(ClobalParser.PrintContext ctx) {
        setValue(ctx, t("print")
                .add("expr", getValue(ctx.expr())));
    }

    @Override
    public void exitPrintSt(ClobalParser.PrintStContext ctx) {
        setValue(ctx, getValue(ctx.printStat()));
    }

    @Override
    public void exitIfElse(ClobalParser.IfElseContext ctx) {
//        setValue(ctx, t("if")
//            .add("cond", getValue(ctx.expr()))
//            .add("s1", buildStatsFromContext(ctx.stat(0)))
//            .add("s2", buildStatsFromContext(ctx.stat(1))));
        setValue(ctx, t("if")
                .add("cond", getValue(ctx.expr()))
                .add("s1", getValue(ctx.stat(0)))
                .add("s2", getValue(ctx.stat(1))));
    }

    @Override
    public void exitIfSt(ClobalParser.IfStContext ctx) {
        if (currentFunction != null) {
            functions.get(currentFunction).add(getValue(ctx.ifStat()));
        }
    }

/*    private List<ST> buildStatsFromContext(ClobalParser.StatContext ctx) {
        List<ST> stList = new LinkedList<>();
        stList.add(getValue(ctx.block()));
        stList.add(getValue(ctx.ifStat()));
        stList.add(getValue(ctx.forStat()));
        stList.add(getValue(ctx.returnStat()));
        stList.add(getValue(ctx.assignStat()));
        stList.add(getValue(ctx.printStat()));
        stList.add(getValue(ctx.expr()));
        stList = stList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return stList;
    }

    private List<ST> buildStatsFromContext(List<ClobalParser.StatContext> ctx) {
        List<ST> stList = new LinkedList<>();
        stList.addAll(ctx.stream().map(s -> getValue(s.assignStat())).collect(Collectors.toList()));
        stList.addAll(ctx.stream().map(s -> getValue(s.block())).collect(Collectors.toList()));
        stList.addAll(ctx.stream().map(s -> getValue(s.ifStat())).collect(Collectors.toList()));
        stList.addAll(ctx.stream().map(s -> getValue(s.forStat())).collect(Collectors.toList()));
        stList.addAll(ctx.stream().map(s -> getValue(s.returnStat())).collect(Collectors.toList()));
        stList.addAll(ctx.stream().map(s -> getValue(s.printStat())).collect(Collectors.toList()));
        stList.addAll(ctx.stream().map(s -> getValue(s.expr())).collect(Collectors.toList()));
        stList = stList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return stList;
    }*/
}
