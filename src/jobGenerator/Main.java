package jobGenerator;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {

        // Generate jobs
        List<Job> jobs = JobGenerator.generateJobs();

        // Print generated jobs
        System.out.println("=== Generated Jobs ===");
        for (Job job : jobs) {
            System.out.println(job);
        }

        // Starting timer
        long startTime = System.currentTimeMillis(); 

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Choose Execution Type ===");
            System.out.println("1. Unsynchronized Execution");
            System.out.println("2. Execution with Mutex");
            System.out.println("3. Execution with Semaphores");
            System.out.println("4. Execution with Peterson's Solution");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            // Latch to wait for all threads to finish
            CountDownLatch latch = new CountDownLatch(jobs.size());

            switch (choice) {
                case 1:
                    System.out.println("\n=== Unsynchronized Execution ===");
                    UnsynchronizedExecution.execute(jobs, latch);
                    break;
                case 2:
                    System.out.println("\n=== Execution with Mutex ===");
                    Mutexes.execute(jobs, latch);
                    break;
                case 3:
                    System.out.println("\n=== Execution with Semaphores ===");
                    Semaphores.execute(jobs, latch);
                    break;
                case 4:
                    System.out.println("\n=== Execution with Peterson's Solution ===");
                    PetersonSolution.execute(jobs);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return; 
                default:
                    System.out.println("Invalid choice. Please try again.");
                    continue;
            }

            // Wait for all threads to finish execution
            try {
                latch.await();
                System.out.println("\n=== All Jobs Completed ===");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Print total execution time
            long endTime = System.currentTimeMillis(); // End executing time
            System.out.println("Total Execution Time: " + (endTime - startTime) + " ms");

            // Restart clock for the next execution
            startTime = System.currentTimeMillis();
        }
    }
}
