import java.util.ArrayList;
import java.util.HashMap;

public class Building {
    private HashMap<Integer, Elevator> elevators;
    private RequestTable requestTable;//一个座一个等候表
    private Bfs bfs;

    public void initBuilding() {
        elevators = new HashMap<>();
        bfs = new Bfs();
        bfs.initBfs();
        this.requestTable = new RequestTable(bfs);
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator();
            ArrayList<Integer> ableFloor = new ArrayList<>();
            for (int j = 1; j <= 11; j++) {
                ableFloor.add(j);
            }
            elevator.initElevator(i, 6, requestTable, 0.4, 1, ableFloor, 2047);
            elevators.put(i, elevator);
            elevator.start();
        }
    }

    public void addRequest(Person person) throws InterruptedException {
        this.requestTable.addRequest(person, person.getFromFloor());
        this.requestTable.addInPerson();
    }

    public void addElevator(int id, int floor, int fullNumber, double speed, int access) {
        Elevator elevator = new Elevator();
        elevator.initElevator(id, fullNumber, requestTable, speed, floor,
            convertDecimalToBinary(access), access);
        elevators.put(id, elevator);
        bfs.addBfs(access);
        requestTable.changeAll();
        elevator.start();
    }

    public void setMaintain(int id) {
        Elevator elevator = elevators.get(id);
        this.requestTable.addMaintain();
        bfs.subBfs(elevator.getAccess());
        requestTable.changeAll();
        elevator.setMaintain();
    }

    public void setOver() {
        this.requestTable.setOver();
    }

    public static ArrayList<Integer> convertDecimalToBinary(int n) {
        int k = n;
        ArrayList<Integer> result = new ArrayList<>();
        int count = 0;

        while (k > 0) {
            int bit = k % 2;
            if (bit == 1) {
                result.add(count + 1);
            }
            k = k / 2;
            count++;
        }

        return result;
    }
}
