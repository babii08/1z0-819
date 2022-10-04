package chapter15.streams;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class CreatingStreams {

    @Test
    void creatingStreams() {

        //Finite streams
        Stream<String> empty =  Stream.empty();             // count = 0
        Stream<Integer> singleElement = Stream.of(1);       // count = 1
        Stream<Integer> fromArray = Stream.of(1, 2, 3);     // count = 3

        //Infinite Streams
        Stream<Double> randoms = Stream.generate(Math::random);
        Stream<Integer> oddNumbers = Stream.iterate(1, n -> n + 2);
        Stream<Integer> oddNumberUnder100 = Stream.iterate(
                1,                // seed
                n -> n < 100,     // Predicate to specify when done
                n -> n + 2);      // UnaryOperator to get next value
    }

}
