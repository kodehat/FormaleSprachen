package de.mnh.praktfour;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;

public class CalcB {

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length > 0) {
            inputFile = args[0];
        }
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String expr = br.readLine();
        int line = 1;
        CalcBParser parser = new CalcBParser(null);
        parser.setBuildParseTree(false);

        while (expr != null) {
            ANTLRInputStream input = new ANTLRInputStream(expr + " n");
            CalcBLexer lexer = new CalcBLexer(input);
            lexer.setLine(line);
            lexer.setCharPositionInLine(0);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            parser.setInputStream(tokens);
            parser.stat();
            expr = br.readLine();
            line++;
        }
    }

}
