public class TaskScheduler {
    public static void main(String[] args) {
        MinHeap1 heap1 = null;
        MinHeap2 heap2 = null;
        MinHeap3 heap3 = null;
        boolean isMode3 = false;  // Flag to determine if it's Mode3
        boolean validInput = true;  // Flag for valid input format
        int count = 0;

        Mode2[] jobsMode2 = new Mode2[10];  // Use for Mode2 jobs
        Mode3[] jobsMode3 = new Mode3[10];  // Use for Mode3 jobs

        // Read the first line from StdIn
        if (!StdIn.isEmpty()) {
            String firstLine = StdIn.readLine().trim();
            String[] firstLineParts = firstLine.split(" ") ;  // Split by spaces

            int numElements = firstLineParts.length;

            // If there are 2 elements, run Task 1 (Mode1)
            if (numElements == 2) {
                heap1 = new MinHeap1(10);  // Initialize MinHeap1 for Mode1
                processMode1(heap1, firstLineParts);  // Process Mode1 jobs directly

            } else if (numElements == 3) {
                // Process the first job for Task 2 or Mode3
                int jobId = Integer.parseInt(firstLineParts[0].trim());
                int processingTime = Integer.parseInt(firstLineParts[1].trim());
                int thirdValue = Integer.parseInt(firstLineParts[2].trim());

                // If any third value is greater than or equal to 20, it's Mode3 (arrival time)
                if (thirdValue >= 20) {
                    isMode3 = true;
                    jobsMode3[count] = new Mode3(jobId, processingTime, thirdValue);  // Store as Mode3 job
                } else {
                    jobsMode2[count] = new Mode2(jobId, processingTime, thirdValue);  // Store as Mode2 job
                }
                count++;

                // Read the rest of the jobs and store them in the respective jobs array
                while (!StdIn.isEmpty()) {
                    String line = StdIn.readLine().trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(" ");

                    // Validate that each line has exactly 3 elements
                    if (parts.length != 3) {
                        validInput = false;
                        break;
                    }

                    jobId = Integer.parseInt(parts[0].trim());
                    processingTime = Integer.parseInt(parts[1].trim());
                    thirdValue = Integer.parseInt(parts[2].trim());

                    // If any third value is greater than or equal to 20, it's Mode3 (arrival time)
                    if (thirdValue >= 20) {
                        isMode3 = true;
                    }

                    // Resize the arrays if necessary
                    if (isMode3) {
                        if (count == jobsMode3.length) {
                            Mode3[] newJobsMode3 = new Mode3[jobsMode3.length * 2];  // Double the size
                            System.arraycopy(jobsMode3, 0, newJobsMode3, 0, jobsMode3.length);
                            jobsMode3 = newJobsMode3;
                        }
                        jobsMode3[count] = new Mode3(jobId, processingTime, thirdValue);  // Store as Mode3 job
                    } else {
                        if (count == jobsMode2.length) {
                            Mode2[] newJobsMode2 = new Mode2[jobsMode2.length * 2];  // Double the size
                            System.arraycopy(jobsMode2, 0, newJobsMode2, 0, jobsMode2.length);
                            jobsMode2 = newJobsMode2;
                        }
                        jobsMode2[count] = new Mode2(jobId, processingTime, thirdValue);  // Store as Mode2 job
                    }
                    count++;
                }

                // If the input format is valid, process the jobs based on the mode
                if (validInput) {
                    if (isMode3) {
                        heap3 = new MinHeap3(count);  // Create a heap for Mode3
                        processMode3(heap3, jobsMode3, count);  // Process Mode3 jobs
                    } else {
                        heap2 = new MinHeap2(count);  // Create a heap for Mode2
                        processMode2(heap2, jobsMode2, count);  // Process Mode2 jobs
                    }
                } else {
                    System.out.println("Invalid input format. Each line must contain exactly 3 elements.");
                }

            } else {
                // If the input doesn't have 2 or 3 elements on the first line, it's invalid
                System.out.println("Error: Invalid number of elements on the first line. Expected 2 or 3 elements.");
            }
        }
    }

    // Process Mode1 jobs (Task 1: based on processing time)
    private static void processMode1(MinHeap1 heap, String[] firstLineParts) {
        System.out.println("Processing with Mode1");
        int count = 0;

        // Process the first job
        int id = Integer.parseInt(firstLineParts[0].trim());
        int processingTime = Integer.parseInt(firstLineParts[1].trim());
        Mode1 job = new Mode1(id, processingTime);
        heap.insert(job);
        count++;

        // Process remaining jobs from StdIn
        while (!StdIn.isEmpty()) {
            String line = StdIn.readLine().trim();
            if (line.isEmpty()) break;

            String[] parts = line.split(" ");  // Split by spaces
            if (parts.length != 2) break;

            id = Integer.parseInt(parts[0].trim());
            processingTime = Integer.parseInt(parts[1].trim());
            job = new Mode1(id, processingTime);
            heap.insert(job);
            count++;
        }

        // Process and output job execution order
        processJobs(heap, count);
    }

    // Process Mode2 jobs (priority-based scheduling)
    private static void processMode2(MinHeap2 heap, Mode2[] jobs, int count) {
        System.out.println("Processing with Mode2");

        int totalCompletionTime = 0;  // Track total completion time across all jobs
        int currentTime = 0;  // Track the current time across all jobs
        StringBuilder executionOrder = new StringBuilder();  // To build the execution order output

        // Insert and process jobs in priority order
        int currentPriority = 1;  // Start with the highest priority (lowest number)

        while (currentPriority <= 5) {  // Assuming priority ranges from 1 to 5
            // Insert jobs of the current priority level into the heap
            for (int i = 0; i < count; i++) {
                if (jobs[i].priority == currentPriority) {
                    heap.insert(jobs[i]);  // Insert only jobs of the current priority
                }
            }

            // Process all jobs of the current priority level
            while (!heap.isEmpty()) {
                Mode2 job = heap.extractMin();  // Extract job with shortest processing time
                currentTime += job.processingTime;  // Increase current time by the job's processing time
                totalCompletionTime += currentTime;  // Add current time (completion time of the job) to total completion time
                executionOrder.append(job.id).append(" ");  // Add job to execution order
            }

            // Move to the next priority level
            currentPriority++;
        }

        // Output the execution order and average completion time
        System.out.println("Execution order: [" + executionOrder.toString().trim().replace(" ", ", ") + "]");
        System.out.printf("Average completion time: %.1f\n", (double) totalCompletionTime / count);
    }

    // Process Mode3 jobs (arrival time-based scheduling)
    private static void processMode3(MinHeap3 heap, Mode3[] jobs, int count) {
        System.out.println("Processing with Mode3");

        // Manual sorting of jobs based on arrival time
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (jobs[i].arrivalTime > jobs[j].arrivalTime) {
                    // Swap jobs
                    Mode3 temp = jobs[i];
                    jobs[i] = jobs[j];
                    jobs[j] = temp;
                }
            }
        }

        int currentTime = 0;
        int totalCompletionTime = 0;
        StringBuilder executionOrder = new StringBuilder();
        int index = 0;  // Track the next job to be added to the heap

        while (index < count || !heap.isEmpty()) {
            // Add jobs to the heap that have arrived by the current time
            while (index < count && jobs[index].arrivalTime <= currentTime) {
                heap.insert(jobs[index]);
                index++;
            }

            if (!heap.isEmpty()) {
                // Extract the job with the shortest processing time
                Mode3 job = heap.extractMin();
                executionOrder.append(job.id).append(" ");
                currentTime += job.processingTime;  // Simulate job running until completion
                totalCompletionTime += currentTime;
            } else if (index < count) {
                // If no jobs have arrived, jump to the next job's arrival time
                currentTime = jobs[index].arrivalTime;
            }
        }

        // Output results
        System.out.println("Execution order: [" + executionOrder.toString().trim().replace(" ", ", ") + "]");
        System.out.printf("Average completion time: %.1f\n", (double) totalCompletionTime / count);
    }

    // Process jobs for any heap type
    private static void processJobs(MinHeap1 heap, int count) {
        double currentTime = 0.0;
        double totalCompletionTime = 0.0;
        StringBuilder executionOrder = new StringBuilder();

        while (!heap.isEmpty()) {
            Mode1 job = heap.extractMin();
            currentTime += job.processingTime;
            totalCompletionTime += currentTime;
            executionOrder.append(job.id).append(" ");
        }

        // Output results
        System.out.println("Execution order: [" + executionOrder.toString().trim().replace(" ", ", ") + "]");
        System.out.println("Average completion time: " + totalCompletionTime / count);
    }
}
