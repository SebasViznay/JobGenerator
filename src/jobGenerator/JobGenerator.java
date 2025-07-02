package jobGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Generates random jobs for users with random attributes
class Job {
    String user;       // User ID (e.g., P1, P2)
    String jobType;    // Job type: Print or Scan
    int length;        // Number of pages
    int arrivalTime;   // Arrival time in seconds

    public Job(String user, String jobType, int length, int arrivalTime) {
        this.user = user;
        this.jobType = jobType;
        this.length = length;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "User: " + user + ", Job: " + jobType + ", Pages: " + length + ", Arrival Time: " + arrivalTime + "s";
    }
}

class JobGenerator {
    public static List<Job> generateJobs() {
        String[] users = {"P1", "P2", "P3", "P4", "P5"}; // Users
        List<Job> jobQueue = new ArrayList<>();
        Random random = new Random();

        // Generate 10 jobs for each user with random attributes
        for (String user : users) {
            int previousTime = 0;
            for (int i = 0; i < 10; i++) {
                String jobType = random.nextBoolean() ? "Print" : "Scan"; // Random job type
                int length = random.nextInt(3) == 0
                        ? random.nextInt(5) + 1  // Short job: 1-5 pages
                        : random.nextInt(10) + 6; // Medium job: 6-15 pages
                if (random.nextInt(5) == 0) { // Large job: 16-50 pages
                    length = random.nextInt(35) + 16;
                }
                int arrivalTime = previousTime + random.nextInt(5) + 1; // Random interval (1-5 seconds)
                jobQueue.add(new Job(user, jobType, length, arrivalTime));
                previousTime = arrivalTime; // Update previous arrival time
            }
        }

        // Sort jobs by arrival time
        jobQueue.sort((j1, j2) -> Integer.compare(j1.arrivalTime, j2.arrivalTime));
        return jobQueue;
    }
}

