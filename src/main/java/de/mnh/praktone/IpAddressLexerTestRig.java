package de.mnh.praktone;

import de.codehat.praktone.IpAddressLexer;
import de.codehat.praktone.TimeLexer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class IpAddressLexerTestRig {
    public static void main(String[] args) throws Exception {
        CharStream input;
        if (args.length > 0) {
            input = new ANTLRFileStream(args[0]);
        } else {
            input = new ANTLRInputStream(System.in);
        }
        IpAddressLexer lex = new IpAddressLexer(input);
        STGroup group = new STGroupFile("praktone.stg");
        ST st;
        Token t;

        // Suppress "token recognition error" messages
        lex.removeErrorListeners();

        do {
            t = lex.nextToken();
            if (t.getType() == IpAddressLexer.IP) {
                st = group.getInstanceOf("ip");
                st.add("val", t.getText());
                st.add("row", t.getLine());
                st.add("col", t.getCharPositionInLine());
                System.out.println(st.render());
            }
        } while (t.getType() != Token.EOF);
    }
}
