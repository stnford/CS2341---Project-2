public class MinHeap3 extends MinHeap1 {

    public MinHeap3(int capacity) {
        super(capacity);  // Reuse the constructor from MinHeap1
        this.heap = new Mode3[capacity];  // Use Mode3 jobs (Job ID, Processing Time, Arrival Time)
    }

    @Override
    protected void resizeHeap() {
        Mode3[] newHeap = new Mode3[capacity * 2];  // Double the size
        System.arraycopy(heap, 0, newHeap, 0, capacity);  // Copy existing elements
        capacity *= 2;  // Update the capacity
        heap = newHeap;  // Point to the new heap array
    }

    @Override
    public void insert(Mode1 job) {
        if (!(job instanceof Mode3)) {
            throw new IllegalArgumentException("MinHeap3 can only accept Mode3 jobs");
        }

        Mode3 mode3Job = (Mode3) job;  // Cast Mode1 to Mode3
        if (size == capacity) {
            resizeHeap();  // Dynamically resize the heap when full
        }
        heap[size] = mode3Job;
        int current = size;
        size++;

        // Bubble up based on processing time (SPT rule)
        while (current > 0 && heap[current].processingTime < heap[parent(current)].processingTime) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public Mode3 extractMin() {
        return (Mode3) super.extractMin();  // Return Mode3 job
    }

    public boolean isEmpty() {
        return size == 0;
    }
}