package de.mnh.prakttwo;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.math.LongMath;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.math.RoundingMode;

public class HalsteadLexerTestRig {
    public static void main(String[] args) throws Exception {
        CharStream input;
        if (args.length > 0) {
            input = new ANTLRFileStream(args[0]);
        } else {
            input = new ANTLRInputStream(System.in);
        }
        HalsteadLexer lex = new HalsteadLexer(input);
        STGroup group = new STGroupFile("prakttwo.stg");
        ST st;
        Token t = lex.nextToken();
        HashMultiset<String> operandsSet = HashMultiset.create();
        HashMultiset<String> operatorsSet = HashMultiset.create();
        int operands = 0;
        int operators = 0;

        while (t == null || t.getType() != HalsteadLexer.EOF) {

            // Fill multiset and add to operands amount
            if (t.getType() == HalsteadLexer.OPERAND) {
                operandsSet.add(t.getText());
                operands++;
            }
            // Fill multiset and add to operators amount
            else if (t.getType() == HalsteadLexer.OPERATOR) {
                operatorsSet.add(t.getText());
                operators++;
            }

            t = lex.nextToken();
        }

        // Print each operand and operator with their amount
        operandsSet.elementSet().forEach((s) -> System.out.println(s + ": " + operandsSet.count(s)));
        operatorsSet.elementSet().forEach((s) -> System.out.println(s + ": " + operatorsSet.count(s)));

        // Distinct operators
        int n1 = operatorsSet.elementSet().size();
        // Distinct operands
        int n2 = operandsSet.elementSet().size();

        // All operators
        int N1 = operatorsSet.size();
        // All operands
        int N2 = operandsSet.size();

        System.out.printf("n1: %d, n2: %d; N1: %d, N2: %d\n", n1, n2, N1, N2);

        // Vocabulary size
        int n = n1 + n2;
        // Implementation size
        int N = N1 + N2;

        System.out.println("n: " + n + ", N: " + N);

        // Halstead-Volume
        int V = N * LongMath.log2(n, RoundingMode.HALF_EVEN);
        // Difficulty
        double D = (n1 / (double) 2) * (N2 / (double) n2);
        // Efford
        double E = D * V;

        System.out.printf("Halstead-Volume: %d, Difficulty: %2f, Efford: %2f\n", V, D, E);
    }
}
