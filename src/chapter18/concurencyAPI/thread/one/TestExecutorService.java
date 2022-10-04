package chapter18.concurencyAPI.thread.one;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestExecutorService {

    @Test
    void testNewSingleThreadExecutorMethod() {

        ExecutorService service = null;

        Runnable task1 = () -> {
            System.out.println("inside task 1");
        };

        Runnable task2 = () -> {
            System.out.println("inside task 2");
        };

        try {
            service = Executors.newSingleThreadExecutor();
            service.execute(task1);
            service.execute(task2);
            service.execute(task1);
            System.out.println("end");
        } finally {
            if (Objects.nonNull(service)) {
                service.shutdown();
            }
        }
    }

    @Test
    void testAwaitTerminationMethodOnExecutorService() {
        ExecutorService service = null;

        Runnable task = () -> System.out.println("inside runnable");

        service = Executors.newSingleThreadExecutor();

        service.execute(task);

        try {
            service.shutdown();
            boolean terminated = service.awaitTermination(20, TimeUnit.SECONDS);
            assertTrue(terminated);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSubmitMethodOfExecutorServiceWithRunnable() {
        ExecutorService service = null;

        Runnable task1 = () -> {
            System.out.println("inside task 1");
        };

        try {
            service = Executors.newSingleThreadExecutor();
            Future<?> result = service.submit(task1);

            assertNotNull(result);
        } finally {
            if (Objects.nonNull(service)) {
                service.shutdown();
            }
        }
    }

    @Test
    void testSubmitMethodOfExecutorServiceWithCallable() {
        ExecutorService service = null;
        final var returnedString = "ran successfully";

        Callable<String> task1 = () -> {
            System.out.println("inside task 1");
            return returnedString;
        };

        try {
            service = Executors.newSingleThreadExecutor();
            Future<?> result = service.submit(task1);

            assertEquals(returnedString , result.get(3L, TimeUnit.SECONDS));

        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(service)) {
                service.shutdown();
            }
        }
    }

    @Test
    void testInvokeAllMethodOnExecutorService() {
        ExecutorService service = null;
        Callable<Integer> result = () -> 13+42;

        try {
            service = Executors.newSingleThreadExecutor();
            List<Future<Integer>> resultList = service.invokeAll(List.of(result, result, result));

            for (var rsl : resultList) {
                assertEquals(13+42, rsl.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testInvokeAnyMethodOnExecutorService() {
        ExecutorService service = null;
        final String rsl = "callable result";
        Callable<String> callable = () -> rsl;

        try {
            service = Executors.newSingleThreadExecutor();
            String info = service.invokeAny(List.of(callable));

            assertEquals(rsl, info);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(service)) {
                service.shutdown();
            }
        }
    }

    @Test
    void testAllFutureMethods() throws InterruptedException, ExecutionException {
        ExecutorService service = null;
        Runnable task1 = () -> {
            System.out.println("Inside another thread");
        };

        Runnable task2 = () -> {
            System.out.println("inside task 2");
        };

        try {
            service = Executors.newSingleThreadExecutor();
            Future<?> result1 = service.submit(task1);
            boolean canceledSuccess = result1.cancel(true);

            Thread.sleep(1000);

            assertTrue(result1.isDone());        //should be done because was canceled
            assertTrue(result1.isCancelled());
            assertTrue(canceledSuccess);
            assertThrows(CancellationException.class, result1::get);     //because was canceled, no execution
            assertThrows(CancellationException.class, () -> result1.get(10L, TimeUnit.SECONDS));     //because was canceled, no execution

            /*******************************/

            Future<?> result2 = service.submit(task2);
            Thread.sleep(1000);
            try {
                result2.get(5L, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                //result is not ready - throws exception
                e.printStackTrace();
            }


        } finally {
            if (Objects.nonNull(service)) {
                service.shutdown();
            }
        }

    }
}
