package de.mnh.praktthree;

import de.mnh.prakttwo.HalsteadLexerTestRig;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ExerciseOneTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "10*2/2+4", "(expr (expr (term (term (term (fact 10)) * (fact 2)) / (fact 2))) + (term (fact 4)))" },
        });
    }

    @Parameterized.Parameter
    public String expression;

    @Parameterized.Parameter(1)
    public String result;

    @Test
    public void testVolumeOfFile() {
        ANTLRInputStream input = new ANTLRInputStream(this.expression);
        ExerciseOneLexer lexer = new ExerciseOneLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExerciseOneParser parser = new ExerciseOneParser(tokens);
        ParseTree tree = parser.expr();
        String treeAsString = tree.toStringTree(parser);

        assertEquals(this.result, treeAsString);
    }

}
