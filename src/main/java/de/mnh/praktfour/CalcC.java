package de.mnh.praktfour;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.InputStream;

public class CalcC {

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
        CalcCLexer lexer = new CalcCLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcCParser parser = new CalcCParser(tokens);
        ParseTree tree = parser.stat();
        ParseTreeWalker walker = new ParseTreeWalker();
        CalcCListenerWithProps calcProp = new CalcCListenerWithProps();
        walker.walk(calcProp, tree);
        System.out.println(calcProp.getValue(tree));
    }

}
