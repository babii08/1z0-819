package chapter18.concurencyAPI.thread.multiple;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadSafetyWithCyclicBarrier {

    @Test
    void testCodeWithoutCyclicBarrier() {

        class LionPenManager {
            private void removeLions() {
                System.out.println("Removing lions");
            }

            private void cleanPen() {
                System.out.println("Cleaning the pen");
            }

            private void addLions() {
                System.out.println("Adding lions");
            }

            public void performTask() {
                removeLions();
                cleanPen();
                addLions();
            }
        }

        ExecutorService service = null;
        LionPenManager lionPenManager = new LionPenManager();
        try {
            service = Executors.newFixedThreadPool(4);
            for (int i = 0; i < 4; i++) {
                service.submit(lionPenManager::performTask);
            }
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }

    }

    @Test
    void testCodeWithCyclicBarrier() {

        class LionPenManager {
            private void removeLions() {
                System.out.println("Removing lions");
            }

            private void cleanPen() {
                System.out.println("Cleaning the pen");
            }

            private void addLions() {
                System.out.println("Adding lions");
            }

            public void performTask(CyclicBarrier barrier1, CyclicBarrier barrier2) {
                try {
                    removeLions();
                    barrier1.await();
                    cleanPen();
                    barrier2.await();
                    addLions();
                } catch (BrokenBarrierException | InterruptedException  e) {
                    e.printStackTrace();
                }
            }
        }

        ExecutorService service = null;
        LionPenManager lionPenManager = new LionPenManager();
        try {
            service = Executors.newFixedThreadPool(4);
            CyclicBarrier barrier1 = new CyclicBarrier(4);
            CyclicBarrier barrier2 = new CyclicBarrier(4);
            for (int i = 0; i < 4; i++) {
                service.submit(() -> lionPenManager.performTask(barrier1, barrier2));
            }
        } finally {
            if (service != null) {
                service.shutdown();
            }
        }

    }

}
