import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.HashMap;

public class In extends Thread {
    private final HashMap<Integer, Building> buildings;

    In(HashMap<Integer, Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            Building building = buildings.get(1);
            if (request == null) {
                building.setOver();
                break;
            } else {
                if (request instanceof PersonRequest) {
                    try {
                        Person person = new Person();
                        person.initPerson((PersonRequest) request);
                        building.addRequest(person);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (request instanceof ElevatorRequest) {
                    building.addElevator(((ElevatorRequest) request).getElevatorId(),
                        ((ElevatorRequest) request).getFloor(),
                        ((ElevatorRequest) request).getCapacity(),
                        ((ElevatorRequest) request).getSpeed(),
                        ((ElevatorRequest) request).getAccess());
                } else if (request instanceof MaintainRequest) {
                    building.setMaintain(((MaintainRequest) request).getElevatorId());
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
