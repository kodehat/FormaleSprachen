package de.mnh.praktone;

import de.codehat.praktone.IpAddressLexer;
import de.codehat.praktone.TimeLexer;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;

public class IpAddressLexerTestRig {
    public static void main(String[] args) throws Exception {
        CharStream input;
        if (args.length > 0) {
            input = new ANTLRFileStream(args[0]);
        } else {
            input = new ANTLRInputStream(System.in);
        }
        IpAddressLexer lex = new IpAddressLexer(input);
        Token t;

        do {
            t = lex.nextToken();
            if (t.getType() == TimeLexer.TIME) {
                System.out.println("Token: " + t + ", Type: " + lex.getTokenNames()[t.getType()]);
            }
        } while (t.getType() != Token.EOF);
    }
}
