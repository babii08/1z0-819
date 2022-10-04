package chapter7;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestEncapsulation {

    private static int one;
    private static final int two;
    private static final int three = 3;
//    private static final int four;    // DOES NOT COMPILE

    static {
        one = 1;
        two = 2;
//        three = 3;                     // DOES NOT COMPILE
//        two = 4;                       // DOES NOT COMPILE
    }

    private static class Koala {
        static int count = 0;
    }

    @Test
    public void testStaticAttributes() {
        Koala koala = new Koala();
        System.out.println(koala.count);
        koala = null;
        System.out.println(koala.count);
        Assertions.assertEquals(0, koala.count);
    }
}
