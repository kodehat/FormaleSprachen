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

import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HalsteadLexerTestRig {

    public static class Results {
        private int n1;
        private int n2;
        private int n;
        private int N1;
        private int N2;
        private int N;
        private int V;
        private double D;
        private double E;

        public Results(int n1, int n2, int n, int n11, int n21, int n3, int v, double d, double e) {
            this.n1 = n1;
            this.n2 = n2;
            this.n = n;
            this.N1 = n11;
            this.N2 = n21;
            this.N = n3;
            this.V = v;
            this.D = d;
            this.E = e;
        }

        public int getSmallN1() {
            return n1;
        }

        public int getSmallN2() {
            return n2;
        }

        public int getSmallN() {
            return n;
        }

        public int getBigN1() {
            return N1;
        }

        public int getBigN2() {
            return N2;
        }

        public int getBigN() {
            return N;
        }

        public int getV() {
            return V;
        }

        public double getD() {
            return D;
        }

        public double getE() {
            return E;
        }
    }

    private static final String[] FILE_NAMES = {
      "Beispiel.c", "eval1.c", "extract.c", "ggt1.c", "ggt2.c", "main.c"
    };

    public static void main(String[] args) {
        Arrays.stream(FILE_NAMES).forEach((s) -> {
            try {
                handle(s, System.in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static Results handle(String file, InputStream in) throws Exception{
        CharStream input;
        if (file != null) {
            input = new ANTLRFileStream(file);
        } else {
            input = new ANTLRInputStream(in);
        }
        HalsteadLexer lex = new HalsteadLexer(input);
        STGroup group = new STGroupFile("prakttwo.stg");
        ST st = group.getInstanceOf("halstead");
        Token t = lex.nextToken();
        HashMultiset<String> operandsSet = HashMultiset.create();
        HashMultiset<String> operatorsSet = HashMultiset.create();

        while (t == null || t.getType() != HalsteadLexer.EOF) {

            // Fill multiset
            if (t.getType() == HalsteadLexer.OPERAND) {
                operandsSet.add(t.getText());
            }
            // Fill multiset
            else if (t.getType() == HalsteadLexer.OPERATOR) {
                operatorsSet.add(t.getText());
            }

            t = lex.nextToken();
        }

        Map<String, Integer> operandsMap = operandsSet.elementSet()
                .stream()
                .collect(Collectors.toMap((s) -> s, operandsSet::count));
        Map<String, Integer> operatorsMap = operatorsSet.elementSet()
                .stream()
                .collect(Collectors.toMap((s) -> s, operatorsSet::count));

        // Distinct operators
        int n1 = operatorsSet.elementSet().size();
        // Distinct operands
        int n2 = operandsSet.elementSet().size();

        // All operators
        int N1 = operatorsSet.size();
        // All operands
        int N2 = operandsSet.size();

        // Vocabulary size
        int n = n1 + n2;
        // Implementation size
        int N = N1 + N2;

        // Halstead-Volume
        int V = N * LongMath.log2(n, RoundingMode.DOWN);
        // Difficulty
        double D = (n1 / (double) 2) * (N2 / (double) n2);
        // Efford
        double E = D * V;

        st.add("name", file != null ? file : "From InputStream");
        st.add("operands_map", operandsMap);
        st.add("operators_map", operatorsMap);
        st.add("n1", n1);
        st.add("n2", n2);
        st.add("n", n);
        st.add("N1", N1);
        st.add("N2", N2);
        st.add("N", N);
        st.add("V", V);
        st.add("D", new DecimalFormat("#.##").format(D));
        st.add("E", E);

        System.out.println(st.render());
        return new Results(n1, n2, n, N1, N2, N, V, D, E);
    }
}
