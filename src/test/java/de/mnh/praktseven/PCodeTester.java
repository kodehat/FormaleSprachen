package de.mnh.praktseven;

import de.mnh.praktseven.interpreter.Interpreter;
import org.junit.Test;

import static org.junit.Assert.*;

public class PCodeTester {

    @Test
    public void testPCode() throws Exception {
        Interpreter.main(new String[]{
                "./src/main/java/de/mnh/praktseven/interpreter/factorial.pcode"
        });

        Interpreter.main(new String[]{
                "./praktseven/printf.pcode"
        });

        Interpreter.main(new String[]{
                "-trace", "./praktseven/uminus.pcode"
        });
    }

}
