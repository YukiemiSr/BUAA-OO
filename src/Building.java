import com.oocourse.elevator2.PersonRequest;

import java.util.HashMap;

public class Building {
    private HashMap<Integer, Elevator> elevators;
    private RequestTable requestTable;//一个座一个等候表

    public void initBuilding(RequestTable requestTable) {
        elevators = new HashMap<>();
        this.requestTable = requestTable;
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator();
            elevator.initElevator(i, 6, requestTable, 0.4, 1);
            elevators.put(i, elevator);
            elevator.start();
        }
    }

    public void addRequest(PersonRequest person) throws InterruptedException {
        this.requestTable.addRequest(person, person.getFromFloor());
    }

    public void addElevator(int id, int floor, int fullNumber, double speed) {
        Elevator elevator = new Elevator();
        elevator.initElevator(id, fullNumber, requestTable, speed, floor);
        elevators.put(id, elevator);
        elevator.start();
    }

    public void setMaintain(int id) {
        Elevator elevator = elevators.get(id);
        this.requestTable.addMaintain();
        elevator.setMaintain();
    }

    public void setOver() {
        this.requestTable.setOver();
    }
}
