package de.mnh.praktseven;

import de.mnh.praktseven.interpreter.Interpreter;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.stringtemplate.v4.STGroupFile;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PCodeTester {

    private PrintStream originalSystemOut;
    private ByteArrayOutputStream systemOutContent;

    @Before
    public void redirectSystemOutStream() {
        originalSystemOut = System.out;
        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
    }

    private void restoreSystemOutStream() {
        System.setOut(originalSystemOut);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "praktseven/condExpr.clobal", 1 },
                { "praktseven/for.clobal", 8 },
                { "praktseven/forTwice.clobal", 64 },
                { "praktseven/funcCall.clobal", 30 },
                { "praktseven/ifElse.clobal", 1 },
                { "praktseven/ifgt.clobal", 4 },
                { "praktseven/iflt.clobal", 1 },
                { "praktseven/neq.clobal", 1 },
                { "praktseven/not.clobal", 4 },
                { "praktseven/printf.clobal", 7 },
                { "praktseven/uminus.clobal", -3 },
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(1)
    public int result;

    @Test
    public void testPCode() throws Exception {
        InputStream is = new FileInputStream(this.filePath);
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

        // Get execution result of generated code
        Interpreter interpreter = new Interpreter();
        Interpreter.load(interpreter, new ByteArrayInputStream(listener.getGeneratedCode().getBytes()));
        interpreter.exec();

        // Restore System.out
        restoreSystemOutStream();

        // Print the file path and the result to console
        System.out.println(this.filePath + ": " + systemOutContent.toString());

        assertEquals(result, Integer.parseInt(systemOutContent.toString().trim()));
    }

}
