package chapter18.concurencyAPI.thread.multiple;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestThreadPoolsOfExecutorService {

    @Test
    void testFixedThreadPoolExecutorService() {
        ExecutorService service = null;

        class SheepManager {
            private Integer sheepCount = 0;
            public void incrementAndReport() {
                System.out.println(++sheepCount + " ");
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReport);
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

    @Test
    void testThreadSafetyWithAtomicClasses() {
        ExecutorService service = null;

        class SheepManager {
            private final AtomicInteger sheepCount = new AtomicInteger(0);        //atomic classes ex: AtomicInteger, AtomicBoolean, AtomicLong
            public void incrementAndReport() {
                sheepCount.getAndAccumulate(sheepCount.get(), (y, x) -> y * 3);     //somehow it multiplies it by 3
                System.out.println(sheepCount.incrementAndGet());
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReport);
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

    @Test
    void testThreadSafetyWithSynchronizedBlock() {
        ExecutorService service = null;

        class SheepManager {
            Object obj = new Object();
            private int sheepCount = 0;
            public void incrementAndReport() {
                synchronized (obj) {            //the obj object here doesn't have any logic. Usually you would have (this) instead.
                    sheepCount *= 3;            //obj was placed here only to emphasize that any object can serve as monitor
                    System.out.println(++sheepCount);
                }
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReport);
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }


    @Test
    void testThreadSafetyWithSynchronizedMethod() {
        ExecutorService service = null;

        class SheepManager {
            private int sheepCount = 0;
            public synchronized void incrementAndReport() {        //this example is equivalent to the one above, the only
                sheepCount *= 3;                                   //difference, here we have a synchronized method instead of block
                System.out.println(++sheepCount);
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReport);
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }
}
