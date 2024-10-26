public class Mode3 extends Mode1 {
    int arrivalTime;  // Arrival time specific to Task 3

    public Mode3(int id, int processingTime, int arrivalTime) {
        super(id, processingTime);  // Inherit id and processingTime from Mode1
        this.arrivalTime = arrivalTime;  // Initialize arrival time
    }
}