package de.mnh.praktthree;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExerciseTestRig {

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
        ExerciseThreeLexer lexer = new ExerciseThreeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExerciseThreeParser parser = new ExerciseThreeParser(tokens);
        ParseTree tree = parser.expr();
        System.out.println(tree.toStringTree(parser));
    }

}
