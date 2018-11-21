package de.mnh.praktfour;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;

public class CalcA {

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length > 0) {
            inputFile = args[0];
        }
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        CalcALexer lexer = new CalcALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcAParser parser = new CalcAParser(tokens);
        ParseTree tree = parser.stat();
        CalcAExprVisitor visitor = new CalcAExprVisitor();
        visitor.visit(tree);
    }

}
