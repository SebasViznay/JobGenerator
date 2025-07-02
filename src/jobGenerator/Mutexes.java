package jobGenerator;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

class Mutexes {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void execute(List<Job> jobs, CountDownLatch latch) {
        for (Job job : jobs) {
            Thread thread = new Thread(() -> {
                processJob(job);
                latch.countDown(); // Signal job completion
            });
            thread.start(); // Start each job in a separate thread
        }
    }

    private static void processJob(Job job) {
        lock.lock(); // Acquire the lock
        try {
            String resource = job.jobType.equals("Print") ? "Printer" : "Scanner";
            System.out.println(job.user + " started a " + job.jobType + " job on " + resource + " (" + job.length + " pages)");

            // Simulate job processing
            for (int i = 0; i < job.length; i++) {
                try {
                    Thread.sleep(1000); // Simulate 1 second per page
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(job.user + ": " + job.jobType + " on " + resource + " - Page " + (i + 1) + " completed");
            }

            System.out.println(job.user + " completed the " + job.jobType + " job");
        } finally {
            lock.unlock(); // Release the lock
        }
    }
}
