public class TaskScheduler {
    public static void main(String[] args) {
        MinHeap1 heap1 = null;
        MinHeap2 heap2 = null;
        MinHeap3 heap3 = null;
        boolean isMode3 = false;  // Flag to determine if it's Mode3
        boolean validInput = true;  // Flag for valid input format
        int count = 0;

        // Read the first line from StdIn
        if (!StdIn.isEmpty()) {
            String firstLine = StdIn.readLine().trim();
            String[] firstLineParts = firstLine.split(" ");  // Split by spaces

            int numElements = firstLineParts.length;

            // If there are 2 elements, run Task 1 (Mode1)
            if (numElements == 2) {
                heap1 = new MinHeap1(10);  // Initialize MinHeap1 for Mode1
                processMode1(heap1, firstLineParts);  // Process Mode1 jobs directly

            } else if (numElements == 3) {
                // Initialize a temporary storage for jobs to process later
                Mode1[] jobs = new Mode1[10];  // Start with a small array

                // Store the first job and check if it's Mode2 or Mode3
                int jobId = Integer.parseInt(firstLineParts[0].trim());
                int processingTime = Integer.parseInt(firstLineParts[1].trim());
                int thirdValue = Integer.parseInt(firstLineParts[2].trim());

                // If any third value is greater than 20, it's Mode3 (arrival time)
                if (thirdValue > 20) {
                    isMode3 = true;
                }

                // Store the job in the array (as Mode2 or Mode3 based on thirdValue)
                if (isMode3) {
                    jobs[count] = new Mode3(jobId, processingTime, thirdValue);  // Mode3 job
                } else {
                    jobs[count] = new Mode2(jobId, processingTime, thirdValue);  // Mode2 job
                }
                count++;

                // Read the rest of the lines to determine if it's Mode2 or Mode3
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

                    // If any third value is greater than 20, it's Mode3 (arrival time)
                    if (thirdValue > 20) {
                        isMode3 = true;
                    }

                    // Resize the array if necessary
                    if (count == jobs.length) {
                        Mode1[] newJobs = new Mode1[jobs.length * 2];  // Double the size
                        System.arraycopy(jobs, 0, newJobs, 0, jobs.length);
                        jobs = newJobs;
                    }

                    // Store the job in the array (as Mode2 or Mode3 based on thirdValue)
                    if (isMode3) {
                        jobs[count] = new Mode3(jobId, processingTime, thirdValue);  // Mode3 job
                    } else {
                        jobs[count] = new Mode2(jobId, processingTime, thirdValue);  // Mode2 job
                    }
                    count++;
                }

                // If the input format is valid, process the jobs based on the mode
                if (validInput) {
                    if (isMode3) {
                        heap3 = new MinHeap3(count);  // Create a heap for Mode3
                        processMode3(heap3, jobs, count);  // Process Mode3 jobs
                    } else {
                        heap2 = new MinHeap2(count);  // Create a heap for Mode2
                        processMode2(heap2, jobs, count);  // Process Mode2 jobs
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
    private static void processMode2(MinHeap2 heap, Mode1[] jobs, int count) {
        for (int i = 0; i < count; i++) {
            heap.insert(jobs[i]);  // Insert jobs into MinHeap2
        }

        // Process jobs based on priority (MinHeap2)
        int currentTime = 0;
        int totalCompletionTime = 0;
        StringBuilder executionOrder = new StringBuilder();

        while (!heap.isEmpty()) {
            Mode1 job = heap.extractMin();  // Extract job with highest priority
            currentTime += job.processingTime;
            totalCompletionTime += currentTime;
            executionOrder.append(job.id).append(" ");
        }

        // Output results
        System.out.println("Execution order: " + executionOrder.toString().trim());
        System.out.println("Average completion time: " + (double) totalCompletionTime / count);
    }

    // Process Mode3 jobs (arrival time-based scheduling)
    private static void processMode3(MinHeap3 heap, Mode1[] jobs, int count) {
        // Manual sorting of jobs based on arrival time
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                Mode3 job1 = (Mode3) jobs[i];
                Mode3 job2 = (Mode3) jobs[j];
                if (job1.arrivalTime > job2.arrivalTime) {
                    // Swap jobs
                    Mode1 temp = jobs[i];
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
            while (index < count && ((Mode3) jobs[index]).arrivalTime <= currentTime) {
                heap.insert(jobs[index]);
                index++;
            }

            if (!heap.isEmpty()) {
                // Extract the job with the shortest processing time
                Mode3 job = (Mode3) heap.extractMin();
                executionOrder.append(job.id).append(" ");
                currentTime += job.processingTime;  // Simulate job running until completion
                totalCompletionTime += currentTime;
            } else if (index < count) {
                // If no jobs have arrived, jump to the next job's arrival time
                currentTime = ((Mode3) jobs[index]).arrivalTime;
            }
        }

        // Output results
        System.out.println("Execution order: [" + executionOrder.toString().trim().replace(" ", ", ") + "]");
        System.out.println("Average completion time: " + (double) totalCompletionTime / count);
    }
    // Process jobs for any heap type
    private static void processJobs(MinHeap1 heap, int count) {
        int currentTime = 0;
        int totalCompletionTime = 0;
        StringBuilder executionOrder = new StringBuilder();

        while (!heap.isEmpty()) {
            Mode1 job = heap.extractMin();
            currentTime += job.processingTime;
            totalCompletionTime += currentTime;
            executionOrder.append(job.id).append(" ");
        }

        // Output results
        System.out.println("Execution order: [" + executionOrder.toString().trim().replace(" ", ", ") + "]");
        System.out.println("Average completion time: " + (double) totalCompletionTime / count);
    }
}