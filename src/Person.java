import com.oocourse.elevator3.PersonRequest;

public class Person {
    private int id;
    private int fromFloor;
    private int toFloor;
    private int finalFloor;

    public void initPerson(PersonRequest personRequest) {
        this.id = personRequest.getPersonId();
        this.fromFloor = personRequest.getFromFloor();
        this.toFloor = personRequest.getToFloor();
        this.finalFloor = personRequest.getToFloor();
    }

    public int getFromFloor() {
        return this.fromFloor;
    }

    public int getToFloor() {
        return this.toFloor;
    }

    public int getPersonId() {
        return this.id;
    }

    public void changeToFloor(int x) {
        this.toFloor = x;
    }

    public int getFinalFloor() {
        return this.finalFloor;
    }

    public void changeFromFloor(int x) {
        this.fromFloor = x;
    }
}
