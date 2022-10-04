package chapter5.arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TestArrays {

    @Test
    public void testSearch() {
        int[] numbers = {2,4,6,8};

        Assertions.assertEquals(Arrays.binarySearch(new int[]{2, 4, 6, 8}, 3), -2);
    }

    @Test
    public void testArrayListSet() {
        var list = new ArrayList<String>();

        list.add("first");

        Assertions.assertEquals(list.set(0, "second"), "first");
    }

    @Test
    public void testArrayListConversion() {

        var array = new int[] {12,13,14,15};


        var list = Arrays.asList(array);

        Assertions.assertEquals(list.toString(), "["+array.toString()+"]");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
           list.remove(13);
        });
    }
}
