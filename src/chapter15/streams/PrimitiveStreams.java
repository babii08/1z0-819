package chapter15.streams;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

public class PrimitiveStreams {

    @Test
    void testAverageMethod() {
        IntStream intStream = IntStream.of(3, 1, 12, 321, 11, 67);
        LongStream longStream = LongStream.of();
        DoubleStream doubleStream = DoubleStream.of(3.2, 1.3, 8.11, 31.8);

        //all the averages return OptionalDouble and value can be get with getAsDouble no matter stream type
        assertNotNull(intStream.average().getAsDouble());
        assertFalse(longStream.average().isPresent());
        assertNotNull(doubleStream.average().getAsDouble());
    }

    @Test
    void testBoxedMethod() {
        IntStream intStream = IntStream.of(3, 1, 2);
        LongStream longStream = LongStream.of(3, 1, 12);
        DoubleStream doubleStream = DoubleStream.of(3.2, 1.3);

        assertEquals(Stream.of(3, 1, 2).collect(toList()), intStream.boxed().collect(toList()));
        assertEquals(Stream.of(3L, 1L, 12L).collect(toList()), longStream.boxed().collect(toList()));
        assertEquals(Stream.of(3.2, 1.3).collect(toList()), doubleStream.boxed().collect(toList()));


        //There is one more possibility to transform between different types of stream

        assertEquals(IntStream.of(3, 1, 2).mapToObj(x -> x).collect(Collectors.toList()), List.of(3, 1, 2));

        final var someStream = Stream.of("abc", "cde", "fgh").mapToDouble(x -> x.length()); //creates a DoubleStream from Stream

        // map() -> to map to same type of stream from any type of stream. Ex IntStream.map = IntStream
        // mapToObj() -> to create Stream<U> from a primitive Stream. Ex: DoubleStream.mapToObj() = Stream<U>
        // mapToDouble  |
        // mapToInt     |- to create corresponding primitive stream from any other type of stream. Ex LongStream.mapToDouble = DoubleStream
        // mapToLong    |


    }

    @Test
    void testMinMaxMethod() {
        assertEquals(IntStream.of(3, 1, 2).max().getClass(), OptionalInt.class);

        assertEquals(LongStream.of(3, 1, 12).min().getClass(), OptionalLong.class);

        assertEquals(DoubleStream.of(3.2, 1.3).max().getClass(), OptionalDouble.class);
    }

    @Test
    void testRange() {
        int []arrayInt = {1,2,3};
        assertArrayEquals(IntStream.range(1, 4).toArray(), arrayInt);

        long []arrayLong = {1L,2L,3L,4L,5L};
        assertArrayEquals(LongStream.rangeClosed(1, 5).toArray(), arrayLong);

        //there is no range for DoubleStream - logic
    }

    @Test
    void testSummaryStatistics() {
        IntStream intStream = IntStream.of(3, 1, 2);
        LongStream longStream = LongStream.of(3, 1, 14);
        DoubleStream doubleStream = DoubleStream.of(3.2, 1.3);

        assertEquals(doubleStream.summaryStatistics().getClass(), DoubleSummaryStatistics.class);

        LongSummaryStatistics summaryStatistics = longStream.summaryStatistics();
        assertEquals(summaryStatistics.getCount(), 3);
        assertEquals(summaryStatistics.getAverage(), 6.0);
        assertEquals(summaryStatistics.getSum(), 18);
        assertEquals(summaryStatistics.getMax(), 14);
        assertEquals(summaryStatistics.getMin(), 1);
    }

    @Test
    void testFlatMap() {
        List<Integer> integerList = List.of(1,2,3);
        IntStream ints = integerList.stream().flatMapToInt(IntStream::of);              //flatToMapInt
        DoubleStream doubles = integerList.stream().flatMapToDouble(DoubleStream::of);  //flatToMapDouble
        LongStream longs = integerList.stream().flatMapToLong(LongStream::of);          //flatToMapLong
    }


}
