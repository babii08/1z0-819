package chapter18.concurencyAPI.thread.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class TestParallelStream {

    @Test
    void testParallelStreamCreation() {
        Stream<Integer> first = Stream.of(1,2,3,4);
        Stream<Integer> firstParallel = first.parallel();        // from existing stream by calling .parallel()

        Stream<Integer> second = List.of(1,2,3).parallelStream();    //from collection by calling .parallelStream()

    }

    @Test
    void testParallelDecomposition() {
        List<Integer> list = List.of(1,2,3,4);
        list.parallelStream()
                .map(x -> {
                    try {
                        System.out.println("performing complex analysis on item: " + x);
                        Thread.sleep(3000);
                        return x * 2;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return x;
                })
                .forEach(System.out::println);

        list.parallelStream()
                .map(x -> {
                    try {
                        System.out.println("performing complex analysis on item: " + x);
                        Thread.sleep(3000);
                        return x * 2;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return x;
                })
                .forEachOrdered(System.out::println);
    }

    @Test
    void testReduceMethodWithValidAccumulatorOnParallelStream() {
        final var list = List.of('w','o','l','f');

        final var result = list.parallelStream()
                .reduce(
                        "",
                        (s1, c) -> s1 + c,
                        (s2,s3) -> s2 + s3
                );

        System.out.println(result);
    }

    @Test
    void testReduceMethodWithProblematicIdentityOnParallelStream() {
        final var list = List.of("w","o","l","f");

        final var result = list.parallelStream()
                .reduce(
                        "X",
                        String::concat          //invalid identity - will be added at each concatenation
                );                              //with parallel streams - try always to use 3 item function with combiner

        System.out.println(result);
    }

    @Test
    void testReduceMethodWithProblematicAccumulatorOnParallelStream() {
        final var list = List.of(1,2,3,4,5);

        final var result = list.parallelStream()
                .reduce(
                        0,
                        (a,b) -> (a-b)
                );

        System.out.println(result);

        final var result1 = list.stream()
                .reduce(
                        0,
                        (a,b) -> (a-b)
                );

        System.out.println(result1);
    }

    @Test
    void testCombiningResultsWithCollect() {
        Stream<String> stream = Stream.of("w", "o", "l", "f").parallel();
        SortedSet<String> set = stream.collect(ConcurrentSkipListSet::new,
                Set::add,
                Set::addAll);
        System.out.println(set);
    }
}
