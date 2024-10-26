public class Mode2 extends Mode1{
    int priority;

    public Mode2(int id, int processingTime, int priority) {
        super(id, processingTime);
        this.priority = priority;
    }
}