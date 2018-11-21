package de.mnh.praktfour;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CalcCTest {

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
        });
    }

    @Parameterized.Parameter
    public String expression;

    @Parameterized.Parameter(1)
    public int result;

    @Test
    public void testVolumeOfFile() {
        ANTLRInputStream input = new ANTLRInputStream(this.expression);
        CalcCLexer lexer = new CalcCLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcCParser parser = new CalcCParser(tokens);
        ParseTree tree = parser.stat();
        ParseTreeWalker walker = new ParseTreeWalker();
        CalcCListenerWithProps calcProp = new CalcCListenerWithProps();
        walker.walk(calcProp, tree);
        int output = calcProp.getValue(tree);

        assertEquals(this.result, output);
    }

}