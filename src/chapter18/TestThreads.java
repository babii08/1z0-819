package chapter18;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

public class TestThreads {

    @Test
    void testCreatingRunnable() {
        class ImplementsRunnable implements Runnable {
            @Override
            public void run() {
                System.out.println("Inside runnable");
            }
        }

        System.out.println("inside main thread");
        (new Thread(new ImplementsRunnable())).start();
    }

    @Test
    void testCreatingThread() {
        class ImplementsThread extends Thread {
            @Override
            public void run() {
                System.out.println("Inside thread");
            }
        }

        System.out.println("inside main thread");
        new ImplementsThread().start();
    }

    @Test
    void testCreatingCallable() {
        class ImplementsCallable implements Callable<String> {
            @Override
            public String call() throws Exception {
                System.out.println("Inside callable. Returning string...");
                return "Thread ran successfully";
            }
        }

    }

    @Test
    void testCreatingThreadWithLambda() {

        System.out.println("inside main thread");
        new Thread(() -> {
            System.out.println("inside thread declared with lambda");
        }).start();
    }

    @Test
    void testThreadSleep() throws InterruptedException {
        System.out.println("inside main thread");
        Thread.sleep(321L);
        new Thread(() -> {
            System.out.println("inside thread declared with lambda");
        }).start();
    }
}
