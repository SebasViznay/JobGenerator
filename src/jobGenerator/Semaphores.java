package jobGenerator;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

class Semaphores {
    private static final Semaphore semaphore = new Semaphore(1); // Only one thread can access the resource at a time

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
        try {
            semaphore.acquire(); // Acquire the semaphore
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(); // Release the semaphore
        }
    }
}
