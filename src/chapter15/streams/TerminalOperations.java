package chapter15.streams;

import com.sun.source.tree.Tree;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

public class TerminalOperations {

    @Test
    void testMinMaxOnStream() {
        Stream<String> empty = Stream.empty();
        Stream<String> s = Stream.of("monkey", "dog", "ape", "bonobo");
        Optional<String> min = s.min((s1, s2) -> s1.length()-s2.length());
        min.ifPresent(System.out::println);

        assertTrue(min.isPresent());       // Even if there are 2 records with same length ["dog", "ape"] min returns one of this
        assertFalse(empty.min(Comparator.comparingInt(String::length)).isPresent());
    }

    @Test
    void testReduceOnStream() {
//        Types of reduction:
//
//        T reduce(T identity, BinaryOperator<T> accumulator)
//
//        Optional<T> reduce(BinaryOperator<T> accumulator)
//
//        <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator, BinaryOperator<U> combiner)

        final var list = List.of("dog", "cat", "puma", "elephant", "mouse");
        Stream<String> stream1 = list.stream();
        Stream<String> stream2 = list.stream();
        Stream<String> stream3 = list.stream();

        final var firstReduce = stream1.reduce("", String::concat);                      //All this reduce are doing
        final var secondReduce = stream2.reduce(String::concat);                        //the same thing, it will concatenate
        final var thirdReduce = stream3.reduce("", String::concat, String::concat);     //all the strings in one string

        assertEquals("dogcatpumaelephantmouse", firstReduce);
        assertEquals("dogcatpumaelephantmouse", secondReduce.get());
        assertEquals("dogcatpumaelephantmouse", thirdReduce);
    }


    @Test
    void testDifferentScenarioReduceOnStream() {

        final var list = List.of("dog", "cat", "puma", "elephant", "mouse");
        Stream<String> stream = list.stream();
        final var firstReduce = stream.reduce("", (x1, x2) -> x1.concat("" + x2.charAt(0)));
        assertEquals("dcpem", firstReduce);


        Stream<String> stream1 = Collections.<String>emptyList().stream();
        final var secondReduce = stream1.reduce((x1, x2) -> x1.concat("" + x2.charAt(0)));
        assertFalse(secondReduce.isPresent());

        final var list2 = List.of("dog", "cat", "puma", "elephant", "mouse");
        Stream<String> stream2 = list2.stream();
        final var thirdReduce = stream2.reduce(true, (x1, x2) -> x1 && x2.length()>2, (x1, x2) -> x1 && x2);
        // This third reduce is hard to understand
        // The identity is a boolean, meaning that this reduce must return boolean in the end
        // Because of this, in second method argument, which is a biFunction, we take boolean (for the first iteration it is the identity)
        // and the first element from stream - which is a string, and it should be converted to boolean as well, hence x2.length()>2
        // In 3rd parameter we just have an AND operation between results of the previous operations
        // The result of this reduce should be true as there is no string with size less 2 or less

        assertTrue(thirdReduce);

        final var listToUseInReduce = new ArrayList<>();
        listToUseInReduce.add("lion");
        final var list3 = List.of("dog", "cat", "puma", "elephant", "mouse");
        Stream<String> stream3 = list3.stream();
        final var thirdReduceMoreComplicated = stream3
                .reduce(listToUseInReduce, (x1, x2) -> { x1.add(x2); return x1; }, (x1, x2) -> {x1.addAll(x2); return x1;});

        assertEquals(List.of("lion", "dog", "cat", "puma", "elephant", "mouse"), thirdReduceMoreComplicated);
    }

    @Test
    void testFirstSignatureCollect() {
//        Method signature
//        <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner)

        final var list = List.of("dog", "cat", "puma", "elephant", "mouse");
        Stream<String> stream = list.stream();
        TreeSet<String> treeSet = stream.collect(
                () -> new TreeSet<String>(),
                (set, streamElement) -> set.add(streamElement),
                (firstSet, secondSet) -> firstSet.addAll(secondSet)
        );

        final var sortedSet = Set.of("cat", "dog", "elephant", "mouse", "puma");
        assertEquals(sortedSet, treeSet);

        //same as treeSet but simplified
        TreeSet<String> treeSetSimplified = list.stream().collect(
                TreeSet::new,
                TreeSet::add,
                TreeSet::addAll
        );

        assertEquals(sortedSet, treeSetSimplified);
    }

    @Test
    void testSecondSignatureCollect() {
//        Method signature
//        <R,A> R collect(Collector<? super T, A,R> collector)

//        from documentation
//        Performs a mutable reduction operation on the elements of this stream using a Collector. A Collector encapsulates
//        the functions used as arguments to collect(Supplier, BiConsumer, BiConsumer), allowing for reuse of collection
//        strategies and composition of collect operations such as multiple-level grouping or partitioning.

//        collect seems to be a particular case of reduction where the identity is a mutable object
//        If we take previous method example in our scenario, I believe that this are mapped like follow
        final var list = List.of("dog", "cat", "puma", "elephant", "mouse", "monkey");
        Stream<String> stream = list.stream();
        TreeSet<Integer> treeSetSlightlyChanged = stream.collect(
                () -> new TreeSet<Integer>(),
                (set, streamElement) -> set.add(streamElement.length()),
                (firstSet, secondSet) -> firstSet.addAll(secondSet)
        );
//        R - TreeSet<>
//        A - element of collection, in our case Integer i.e TreeSet<Integer>
//        T - element of stream, i.e String

        TreeSet<String> treeSet = list.stream().collect(Collectors.toCollection(TreeSet::new));
//        difference from previous example is that for the above to compile the return parameter from collection should be
//        same as the paramater in the stream

        class Person {
            private String name;
            private int age;
            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
            public String getName() {
                return name;
            }
            public int getAge() {
                return age;
            }
        }

        final var personsList = List.of(
                new Person("Ada", 32),
                new Person("Tom", 21),
                new Person("John", 57),
                new Person("Edi", 13)
        );

        final var personStream = personsList.stream();
        // example of groupingBy
        // first argument of grouping will be the key of the result map
        // second argument is another Collector that will group values into a collection. In this case it will create a list
        // with all the names as the value of the resulted map.
        Map<Integer, List<String>> persons = personStream
                .collect(groupingBy(Person::getAge, mapping(Person::getName, toList())));


        //the above can be simplified by having all static methods imported as static
        Map<Integer, List<String>> persons1 = personsList.stream()
                .collect(groupingBy(Person::getAge, mapping(Person::getName, toList())));
    }


    @Test
    void testDifferentCollectorsMethods() {
        List<String> someList = List.of("coffee", "tea", "water");

        final var someResult = someList.stream().collect(Collectors.joining(", "));
        assertEquals("coffee, tea, water", someResult);
        final var joinedResult = someList.stream().collect(Collectors.joining());
        assertEquals("coffeeteawater", joinedResult);

        Double result = someList.stream().collect(Collectors.averagingInt(String::length));

        Map<String, Integer> resultedMap = someList.stream().collect(Collectors.toMap(s -> s, v -> v.length()));
        Map<String, Integer> resultedStringMap = someList.stream()
                .collect(Collectors.toMap(k -> k, String::length, (Integer i1, Integer i2) -> i1 + i2));    //this one is used in case there is a possibility for same key when creating map. In that case we provide a merging function for values

        System.out.println(resultedStringMap);

        Map<String, Integer> resultedStringTreeMap = someList.stream()
                .collect(Collectors.toMap(k -> k, String::length, (i1, i2) -> i1 + i2, TreeMap::new));
        //this one is used in case there is a possibility for same key when creating map. In that case
        // we provide a merging function for values


        var someAnimals = Stream.of("lions", "tigers", "bears");
        Map<Integer, List<String>> someAnimalsStream = someAnimals.collect(Collectors.groupingBy(String::length));


    }
}
