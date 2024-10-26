public class MinHeap1 {
    protected Mode1[] heap;
    protected int size;
    protected int capacity;

    public MinHeap1(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new Mode1[capacity];
    }

    protected int parent(int i) { return (i - 1) / 2; }
    protected int leftChild(int i) { return 2 * i + 1; }
    protected int rightChild(int i) { return 2 * i + 2; }

    protected void swap(int i, int j) {
        Mode1 temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Dynamically resize the heap when it's full
    protected void resizeHeap() {
        Mode1[] newHeap = new Mode1[capacity * 2];  // Double the size
        System.arraycopy(heap, 0, newHeap, 0, capacity);  // Copy existing elements
        capacity *= 2;  // Update the capacity
        heap = newHeap;  // Point to the new heap array
    }

    public void insert(Mode1 job) {
        // Check if heap is full, if so, resize it
        if (size == capacity) {
            resizeHeap();  // Resize the heap instead of printing a message
        }
        heap[size] = job;
        int current = size;
        size++;

        // Bubble up to maintain the heap property based on processing time
        while (current > 0 && heap[current].processingTime < heap[parent(current)].processingTime) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public Mode1 extractMin() {
        if (size <= 0) return null;
        if (size == 1) {
            size--;
            return heap[0];
        }

        Mode1 root = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapify(0);

        return root;
    }

    protected void heapify(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int smallest = i;

        if (left < size && heap[left].processingTime < heap[smallest].processingTime) {
            smallest = left;
        }
        if (right < size && heap[right].processingTime < heap[smallest].processingTime) {
            smallest = right;
        }

        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }
}