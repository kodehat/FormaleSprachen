package de.mnh.prakttwo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class HalsteadLexerTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "Beispiel.c", 2340 },
                { "eval1.c", 1395 },
                { "extract.c", 220 },
                { "ggt1.c", 96 },
                { "ggt2.c", 144 },
                { "main.c", 156 },
        });
    }

    @Parameterized.Parameter
    public String file;

    @Parameterized.Parameter(1)
    public int expectedVolume;

    @Test
    public void testVolumeOfFile() throws Exception {
        HalsteadLexerTestRig.Results results = HalsteadLexerTestRig.handle(this.file, null);
        assertEquals(this.expectedVolume, results.getV());
    }

}
