package chapter18.concurencyAPI.thread.multiple;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestSafetyAccessToThreads {

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

    @Test
    void testThreadSafetyWithLockFramework() {
        ExecutorService service = null;
        class SheepManager {
            private int sheepCount = 0;
            private final Lock lock = new ReentrantLock();    //gives access based on availability not on order of requests
            public void incrementAndReport() {                //response might not be ordered
                try {
                    lock.lock();
                    sheepCount *= 3;
                    System.out.println(++sheepCount);
                } finally {
                    lock.unlock();
                }
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReport);
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

    @Test
    void testThreadSafetyWithLockFrameworkIncludingFairness() {
        ExecutorService service = null;
        class SheepManager {
            private int sheepCount = 0;
            private final Lock lock = new ReentrantLock(true);    //gives access in the order it was requested
            public void incrementAndReport() {
                try {
                    lock.lock();
                    System.out.println("Thread id:" + Thread.currentThread().getId());
                    sheepCount *= 3;
                    System.out.println(++sheepCount);
                } finally {
                    lock.unlock();
                }
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 10; i++)
                service.submit(manager::incrementAndReport);
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

    @Test
    void testThreadSafetyWithTryLockFramework() {
        ExecutorService service = null;
        class SheepManager {
            private int sheepCount = 0;
            private final Lock lock = new ReentrantLock();
            public void incrementAndReport() {
                final var locked = lock.tryLock();
                if (locked) {
                    try {
                        System.out.println("Thread id:" + Thread.currentThread().getId());
                        sheepCount *= 3;
                        System.out.println(++sheepCount);
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Could not lock");
                }
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 10; i++) {
                Thread.sleep(2);        //will cause a few "Could not lock" as the method execution might exceed 2 milis;
                service.submit(manager::incrementAndReport);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

    @Test
    void testThreadSafetyWithTryLockWithTime() {
        ExecutorService service = null;
        class SheepManager {
            private int sheepCount = 0;
            private final Lock lock = new ReentrantLock();
            public void incrementAndReport() {
                boolean locked = false;
                try {
                    locked = lock.tryLock(15, TimeUnit.SECONDS);     //most probably no "Could not lock" as it waits for 15 seconds
                } catch (InterruptedException e) {                        //for previous task to finish. Which is plenty of time for this
                    e.printStackTrace();                                  //small method
                }
                if (locked) {
                    try {
                        sheepCount *= 3;
                        System.out.println(++sheepCount);
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Could not lock");
                }
            }
        }

        try {
            SheepManager manager = new SheepManager();
            service = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 10; i++) {
                Thread.sleep(2);        //will cause a few "Could not lock" as the method execution might exceed 2 milis;
                service.submit(manager::incrementAndReport);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }
    }

}
