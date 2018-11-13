package de.mnh.praktthree;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ExerciseThreeTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "10*2/2+4", "(expr (expr (expr (expr 10) * (expr 2)) / (expr 2)) + (expr 4))" },
        });
    }

    @Parameterized.Parameter
    public String expression;

    @Parameterized.Parameter(1)
    public String result;

    @Test
    public void testVolumeOfFile() {
        ANTLRInputStream input = new ANTLRInputStream(this.expression);
        ExerciseThreeLexer lexer = new ExerciseThreeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExerciseThreeParser parser = new ExerciseThreeParser(tokens);
        ParseTree tree = parser.expr();
        String treeAsString = tree.toStringTree(parser);

        assertEquals(this.result, treeAsString);
    }

}
