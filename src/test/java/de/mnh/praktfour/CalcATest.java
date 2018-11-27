package de.mnh.praktfour;

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
public class CalcATest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "10*2/2+4 n", 14 },
                { "(2^2)-4 n", 0 },
                { "3*(8+4)-2 n", 34 },
                { "0-12*3 n", -36 },
                { "5+2 n", 7 },
                { "3+7 n", 10 },
                { "3-7*2 n", -11 },
                { "3==2+2-1 n", 1},
                { "5==12*45/3 n", 0},
                { "5>4-2 n", 1},
                { "10>22*2 n", 0},
                { "10<100/2 n", 1},
                { "10<2^2 n", 0},
                { "a=2+5 n a+6 n", 13},
                { "2==2 ? 5+5 : 4+4 n", 10},
                { "3>2*5 ? 5+5 : 4+4 n", 8},
                { "if (10-10==0) 2+2 n else 4+4 n", 4},
                { "if (10*10>1000) 2+2 n else 4+4 n", 8},
        });
    }

    @Parameterized.Parameter
    public String expression;

    @Parameterized.Parameter(1)
    public int result;

    @Test
    public void testVolumeOfFile() {
        ANTLRInputStream input = new ANTLRInputStream(this.expression);
        CalcALexer lexer = new CalcALexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcAParser parser = new CalcAParser(tokens);
        ParseTree tree = parser.prog();
        CalcAExprVisitor visitor = new CalcAExprVisitor();
        int output = visitor.visit(tree);

        assertEquals(this.result, output);
    }

}