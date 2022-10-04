package chapter15.interfaceMethods;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class ConvenienceMethods {

    @Test
    void checkStaticNotOnPredicate() {
        Predicate<Integer> integ = Objects::isNull;
        Predicate<Integer> orPred = Predicate.not(integ);

        assertTrue(integ.test(null));
        assertFalse(orPred.test(null));
    }

    @Test
    void checkOrOnPredicate() {
        Predicate<String> emptyString = String::isBlank;
        Predicate<String> notShortString = (x) -> x.length() > 3;
        Predicate<String> specificString = emptyString.or(notShortString);

        assertTrue(specificString.test(""));
        assertTrue(specificString.test("more than 3 characters"));
        assertFalse(specificString.test("no"));
    }

    @Test
    void checkAndThenOnConsumer() {
        final var list = new ArrayList<String>();
        Consumer<String> cons = list::add;
        Consumer<String> andThenCons = cons.andThen((x) -> list.add("and then " + x));

        final var simpleConsumer = List.of("1");
        final var consumerWithAndThen = List.of("1", "and then 1");
        cons.accept("1");                             //list["1"]
        assertEquals(simpleConsumer, list);
        list.clear();                                   //list[]
        andThenCons.accept("1");                     //list["1", "and then 1"]
        assertEquals(consumerWithAndThen, list);
    }

    @Test
    void checkAndThenOnFunction() {

        Function<String, Integer> func = String::length;            //takes a string and return an integer
        Function<String, Integer> functionWithAndThen = func
                .andThen(x -> x.compareTo(18));                     //takes a string.
                                                                    //run the first function on line 54 which returns an int
                                                                    //the return int is used in the function declared inside andThen method

        assertEquals(22, (int) func.apply("this string size is 22"));
        assertTrue(func.apply("this string size is greater than 15") > 0);
        assertFalse(functionWithAndThen.apply("string size < 18") > 0);
    }

    @Test
    void checkComposeOnFunction() {
        Function<String, Integer> func = String::length;            //takes a string and return an integer
        Function<Double, Integer> functionWithCompose = func
                .compose((x) -> x.toString());              //takes a Double. That double is then ran through the function
                                                            //declared inside compose. The result of that function is String
                                                            //The function "func" should have the parameter of type String
                                                            //otherwise will not compile. After the function inside compose
                                                            //the "func" is run and it returns and Integer. In case
                                                            //"functionWithCompose" would have had another return type than Integer - it will fail to compile


        assertEquals(5, func.apply("abcde"));
        assertEquals(5, functionWithCompose.apply(23.12));
        assertNotEquals(5, functionWithCompose.apply(23.1));
    }
}
