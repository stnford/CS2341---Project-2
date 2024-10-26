public class MinHeap2 extends MinHeap1 {

    public MinHeap2(int capacity) {
        super(capacity);  // Reuse the constructor from MinHeap1
        this.heap = new Mode2[capacity];  // Use Mode2 jobs
    }

    // Dynamically resize the heap when it's full
    @Override
    protected void resizeHeap() {
        Mode2[] newHeap = new Mode2[capacity * 2];  // Double the size
        System.arraycopy(heap, 0, newHeap, 0, capacity);  // Copy existing elements
        capacity *= 2;  // Update the capacity
        heap = newHeap;  // Point to the new heap array
    }

    // Override the insert method to consider priority as well
    @Override
    public void insert(Mode1 job) {
        Mode2 mode2Job = (Mode2) job;  // Cast Mode1 to Mode2
        if (size == capacity) {
            resizeHeap();  // Dynamically resize the heap when full
        }
        heap[size] = mode2Job;
        int current = size;
        size++;

        // Bubble up based on priority first, and then processing time if priorities are the same
        while (current > 0 && compare((Mode2) heap[current], (Mode2) heap[parent(current)]) < 0) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Compare by priority first, and by processing time if priorities are equal
    private int compare(Mode2 a, Mode2 b) {
        if (a.priority != b.priority) {
            return a.priority - b.priority;  // Higher priority (lower number) goes first
        }
        return a.processingTime - b.processingTime;  // Use processing time if priorities are the same
    }

    @Override
    public Mode2 extractMin() {
        return (Mode2) super.extractMin();  // Return Mode2 job
    }
}