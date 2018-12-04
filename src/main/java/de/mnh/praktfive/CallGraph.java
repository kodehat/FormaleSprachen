package de.mnh.praktfive;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CallGraph {

    static class Graph {
        Set<String> nodes = new OrderedHashSet<>();
        MultiMap<String, String> edges = new MultiMap<>();
        Map<String, List<String>> nodeColors = new HashMap<>();

        public void edge(String source, String target) {
            edges.map(source, target);
        }

        public String toDOT() {
            STGroup group = new STGroupFile("praktfive.stg");
            ST st = group.getInstanceOf("dot");

            List<String> strNodes = new ArrayList<>();
            MultiMap<String, String> strEdges = new MultiMap<>();

            // Nodes
            for (String node : nodes) {
                if (nodeColors.containsKey(node)) {
                    node += " " + nodeColors.get(node).get(0);
                }
                strNodes.add(node);
            }

            // Edges
            for (String src : edges.keySet()) {
                for (String trg : edges.get(src)) {
                    if (src.equals(trg) && nodeColors.containsKey(src)) {
                        String color = nodeColors.get(src).remove(0);
                        trg += " " + color;
                    }
                    strEdges.map(src, trg);
                }
            }

            st.add("nodes", strNodes);
            st.add("edges", strEdges);

            return st.render();
        }

        public void putColor(String node, String color) {
            nodeColors.computeIfAbsent(node, k -> new LinkedList<>());
            nodeColors.get(node).add(String.format("[color = %s]", color));
        }

        @Override
        public String toString() {
            return String.format("edges: %s\n", edges.toString())
                    + String.format("functions: %s", nodes.toString());
        }
    }

    static class FunctionListener extends CymbolBaseListener {
        Graph graph = new Graph();
        String currentFunctionName = null;

        @Override
        public void enterFunctionDecl(CymbolParser.FunctionDeclContext ctx) {
            currentFunctionName = ctx.ID().getText();
            graph.nodes.add(currentFunctionName);
        }

        @Override
        public void exitCall(CymbolParser.CallContext ctx) {
            String funcName = ctx.ID().getText();
            graph.edge(currentFunctionName, funcName);

            if (currentFunctionName.equals(funcName)) {
                if (ctx.getParent() instanceof CymbolParser.ReturnContext) {
                    graph.putColor(currentFunctionName, "green");
                } else {
                    graph.putColor(currentFunctionName, "red");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String inputFile = null;
        if (args.length > 0) {
            inputFile = args[0];
        }
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);

        CymbolLexer lexer = new CymbolLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CymbolParser parser = new CymbolParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.file();

        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionListener collector = new FunctionListener();
        walker.walk(collector, tree);

        System.out.println(collector.graph.toDOT());
    }

}
