package de.mnh.praktone;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class TimeLexerTestRig {
    public static void main(String[] args) throws Exception {
        CharStream input;
        if (args.length > 0) {
            input = new ANTLRFileStream(args[0]);
        } else {
            input = new ANTLRInputStream(System.in);
        }
        TimeLexer lex = new TimeLexer(input);
        STGroup group = new STGroupFile("praktone.stg");
        ST st;
        Token t;

        // Suppress "token recognition error" messages
        lex.removeErrorListeners();

        do {
            t = lex.nextToken();

            if (t.getType() == TimeLexer.TIME) {
                st = group.getInstanceOf("time");

                st.add("val", t.getText());
                st.add("row", t.getCharPositionInLine());
                st.add("col", t.getLine());
                System.out.println(st.render());
            }
        } while (t.getType() != Token.EOF);
    }
}
