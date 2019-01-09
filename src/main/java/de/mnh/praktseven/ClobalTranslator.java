package de.mnh.praktseven;

import de.mnh.praktseven.interpreter.Interpreter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stringtemplate.v4.STGroupFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class ClobalTranslator {

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

        // Create lexer
        ClobalLexer lexer = new ClobalLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create parser
        ClobalParser parser = new ClobalParser(tokens);
        ParseTree tree = parser.file();

        // Create parse-tree-walker
        ParseTreeWalker walker = new ParseTreeWalker();
        ClobalTranslatorListener listener = new ClobalTranslatorListener(new STGroupFile("pcode.stg"));
        walker.walk(listener, tree);

        // Print generated code
        System.out.println("[CODE]:\n" + listener.getGeneratedCode());

        // Print execution result of generated code
        System.out.println("\n[RESULT]:");
        Interpreter interpreter = new Interpreter();
        Interpreter.load(interpreter, new ByteArrayInputStream(listener.getGeneratedCode().getBytes()));
        interpreter.exec();
    }
}