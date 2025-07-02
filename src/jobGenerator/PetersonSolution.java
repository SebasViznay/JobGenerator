package jobGenerator;

import java.util.List;
import java.util.concurrent.CountDownLatch;

class PetersonSolution {
    private static volatile boolean[] flag = new boolean[2];
    private static volatile int turn;

    public static void execute(List<Job> jobs) {
        for (int i = 0; i < jobs.size(); i += 2) {
            CountDownLatch latch = new CountDownLatch(2);
            Job job1 = jobs.get(i);
            Job job2 = (i + 1) < jobs.size() ? jobs.get(i + 1) : null;

            Thread thread1 = new Thread(() -> {
                long startTime = System.currentTimeMillis();
                processJob(job1, 0);
                long endTime = System.currentTimeMillis();
                latch.countDown();
            });

            Thread thread2 = new Thread(() -> {
                if (job2 != null) {
                    long startTime = System.currentTimeMillis();
                    processJob(job2, 1);
                    long endTime = System.currentTimeMillis();
                    latch.countDown();
                }
            });

            // Start both threads
            thread1.start();
            thread2.start();

            // Wait for both threads to complete
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processJob(Job job, int i) {
        String resource = job.jobType.equals("Print") ? "Printer" : "Scanner";
        int other = 1 - i;

        // Begin Peterson's solution to ensure mutual exclusion
        for (int page = 0; page < job.length; page++) {
            // Indicate the intent to enter the critical section
            flag[i] = true;
            turn = other;

            // Wait until the other thread has finished its critical section
            while (flag[other] && turn == other) {
                // Busy wait (spinlock)
            }

            // Critical section: access the shared resource (printer or scanner)
            System.out.println(job.user + " started a " + job.jobType + " job on " + resource + " (" + job.length + " pages)");
            try {
                Thread.sleep(1); // Simulate 100ms per page
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(job.user + ": " + job.jobType + " on " + resource + " - Page " + (page + 1) + " completed");

            // Exit the critical section
            flag[i] = false;
        }

        System.out.println(job.user + " completed the " + job.jobType + " job");
    }
}
