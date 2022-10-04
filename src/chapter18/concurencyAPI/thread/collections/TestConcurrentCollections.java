package chapter18.concurencyAPI.thread.collections;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConcurrentCollections {

    @Test
    void testConcurrentHashMap() {
        Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
        concurrentMap.put("1", 1);
        concurrentMap.put("2", 2);

        assertEquals(Set.of("1","2"), concurrentMap.keySet());
    }

    @Test
    void testConcurrentLinkedQueue() {
        ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        concurrentQueue.offer("1");
        concurrentQueue.offer("1");
        concurrentQueue.offer("2");

        assertEquals(List.of("1","1","2"), new ArrayList<>(concurrentQueue));
    }

    @Test
    void testConcurrentSkipListMap() {
        ConcurrentSkipListMap<Integer, String> concurrentOrderedMap = new ConcurrentSkipListMap<>();
        concurrentOrderedMap.put(1, "1");
        concurrentOrderedMap.put(2, "2");
        concurrentOrderedMap.put(2, "2");       //is not added as map not allowing duplicates
        concurrentOrderedMap.put(0, "0");

        assertEquals(Set.of(0,1,2), concurrentOrderedMap.keySet());
    }

    @Test
    void testConcurrentSkipListSet() {
        ConcurrentSkipListSet<Integer> concurrentOrderedSet = new ConcurrentSkipListSet<>();
        concurrentOrderedSet.add(4);
        concurrentOrderedSet.add(2);
        concurrentOrderedSet.add(2);       //is not added as map not allowing duplicates
        concurrentOrderedSet.add(1);

        assertEquals(Set.of(1,2,4), concurrentOrderedSet);
    }

    @Test
    void testCopyOnWriteArrayList() {
        List<String> list = new CopyOnWriteArrayList<>();

        list.add("a");
        list.add("d");
        list.add("b");

        for (var c : list) {
            System.out.println(c);
            list.add("q");      //let you add new variable, but does that to an underlying collection, which is merged
        }                       //back into the original list after we complete iteration

        assertEquals(6, list.size());
        assertEquals(List.of("a","d","b","q","q","q"), list);
    }

    @Test
    void testCopyOnWriteArraySet() {
        Set<String> set = new CopyOnWriteArraySet<>();

        set.add("a");
        set.add("d");
        set.add("b");

        for (var c : set) {
            System.out.println(c);
            set.add("q");      //works similar to list collection but being a set - doesn't allow duplicate
        }

        assertEquals(4, set.size());
        assertEquals(Set.of("a","d","b","q"), set);
    }

    @Test
    void testLinkedBlockingQueue() throws InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

        queue.offer("w");
        queue.offer("z", 5, TimeUnit.SECONDS);     //newly provided method. Adds an item to the queue, waiting the specified time and returning false if the time elapses before space is available

        queue.offer("e");
        queue.poll();
        queue.poll(5, TimeUnit.SECONDS);           //newly provided method. Retrieves and removes an item from the queue, waiting the specified time and returning null if the time elapses before the item is available

        assertEquals(List.of("e"), new ArrayList<>(queue));

    }
}
