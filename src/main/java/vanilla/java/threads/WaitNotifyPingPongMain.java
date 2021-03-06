package vanilla.java.threads;

import org.jetbrains.annotations.NotNull;

/**
 * Created by peter.lawrey on 12/01/2016.
 */
public class WaitNotifyPingPongMain {
    boolean toggle = false;
    long count = 0;

    public static void main(String... args) throws InterruptedException {
        WaitNotifyPingPongMain wnppm = new WaitNotifyPingPongMain();

        Thread t1 = wnppm.createThread(true);
        Thread t2 = wnppm.createThread(false);
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        t1.interrupt();
        t2.interrupt();
        long time = System.currentTimeMillis() - start;
        t1.join();
        t2.join();
        System.out.printf("Counted to %,d toggles per second.%n", wnppm.count * 1000 / time);
    }

    @NotNull
    Thread createThread(boolean flag) {
        Thread thread = new Thread(() -> runLoop(flag), "toggle-" + flag);
        thread.start();
        return thread;
    }

    synchronized void runLoop(boolean flag) {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                while (toggle == flag) {
                    wait(10);
                }
                toggle = flag;
                count++;
                notifyAll();
            }
        } catch (InterruptedException ignore) {
        } finally {
            System.out.println(flag + " - finished");
        }
    }
}
