package chapter18.concurencyAPI.thread.one;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestScheduledExecutorService {

    @Test
    void testScheduleMethodOfScheduledExecutorServiceWithRunnable() throws InterruptedException {
        ScheduledExecutorService service = null;

        Runnable task = () -> {
            System.out.println("inside runnable");
        };

        try {
            service = Executors.newSingleThreadScheduledExecutor();

            service.schedule(task, 5, TimeUnit.SECONDS);
        } finally {
            Thread.sleep(6000);
            if (service != null) {
                service.shutdown();
            }
        }
    }


    @Test
    void testScheduleMethodOfScheduledExecutorServiceWithCallable() throws InterruptedException {
        ScheduledExecutorService service = null;

        Callable<String> task = () -> {
            System.out.println("inside runnable");
            return "result";
        };

        try {
            service = Executors.newSingleThreadScheduledExecutor();

            final ScheduledFuture<String> scheduledFutureResult = service.schedule(task, 5, TimeUnit.SECONDS);
            System.out.println(scheduledFutureResult.getDelay(TimeUnit.SECONDS));
            assertFalse(scheduledFutureResult.isDone());
        } finally {
            Thread.sleep(6000);
            if (service != null) {
                service.shutdown();
            }
        }
    }

    @Test
    void testScheduleAtFixedRateMethodOfScheduledExecutorService() {
        ScheduledExecutorService service = null;

        Runnable task = () -> {
            System.out.println("inside runnable");
        };

        try {
            service = Executors.newSingleThreadScheduledExecutor();

            service.scheduleAtFixedRate(task, 5, 10, TimeUnit.SECONDS);
        } finally {
            while (true) {
                //will run infinitely. You should see "inside runnable" written after 5 seconds initial delay, and
                //then at each 10 seconds, new "inside runnable" will pop-up.
                //This method "scheduledAtFixedRate" runs regardless if previous task finished or not. It might be
                //causing issues in case thread takes more than declared period to finish. That will lead to
                //threads being continuously created and will make application crash at some point.
            }
        }
    }

    @Test
    void testScheduleWithFixedDelayMethodOfScheduledExecutorService() {
        ScheduledExecutorService service = null;

        Runnable task = () -> {
            System.out.println("inside runnable");
        };

        try {
            service = Executors.newSingleThreadScheduledExecutor();

            service.scheduleWithFixedDelay(task, 5, 10, TimeUnit.SECONDS);
        } finally {
            while (true) {
                //will run infinitely. You should see "inside runnable" written after 5 seconds initial delay, and
                //then 10 seconds after each previous request finished, new "inside runnable" will pop-up.
                //This method "scheduledAtFixedRate" runs at specified period only after previous task finished.
            }
        }
    }

}
