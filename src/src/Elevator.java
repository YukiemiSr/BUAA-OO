import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;
import java.util.Objects;

public class Elevator extends Thread {
    private int id;
    private int floor;
    private int number;
    private boolean direction;
    private ArrayList<PersonRequest> dest;
    private Strategy strategy;
    private RequestTable requestTable;

    public void initElevator(int id, RequestTable requestTable) {
        this.id = id;
        this.floor = 1;
        this.direction = true;
        this.dest = new ArrayList<>();
        this.requestTable = requestTable;
        this.strategy = new Strategy(requestTable);
        this.number = 0;
    }

    public void run() {
        String advice;
        while (!dest.isEmpty() || !requestTable.isOver() || !requestTable.isEmpty()) {
            try {
                advice = strategy.advice(this.floor, this.direction, this.dest, number);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            switch (advice) {
                case "over":
                    break;
                case "move":
                    try {
                        move();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "open":
                    ArrayList<PersonRequest> people = new ArrayList<>();
                    synchronized (requestTable) {
                        try {
                            String s = strategy.advice(floor, direction, dest, number);
                            if (!Objects.equals(s, "open")) {
                                break;
                            }
                            open(people);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case "reserve":
                    reserve();
                    break;
                case "wait":
                    synchronized (requestTable) {
                        try {
                            requestTable.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                default:
            }
        }
    }

    void move() throws InterruptedException {
        sleep(400);
        if (direction) {
            if (floor < 11) {
                floor++;
            } else {
                floor--;
                direction = false;
            }
            TimableOutput.println(
                String.format("ARRIVE-%d-%d", this.floor, this.id));
        } else {
            if (floor > 1) {
                floor--;
            } else {
                direction = true;
                floor++;
            }
            TimableOutput.println(
                String.format("ARRIVE-%d-%d", this.floor, this.id));
        }
    }

    void open(ArrayList<PersonRequest> people) throws InterruptedException {
        if (!requestTable.getWaitQueue().isEmpty()) {
            if (requestTable.getWaitQueue().containsKey(floor)) {
                if (!requestTable.getWaitQueue().get(floor).isEmpty()) {
                    int k = 0;
                    while ((k + number < 6) &&
                        !requestTable.getWaitQueue().get(floor).isEmpty()) {
                        people.add(requestTable.getRequest(floor));
                        k++;
                        if (!requestTable.getWaitQueue().containsKey(floor)) {
                            break;
                        }
                    }
                }
            }
        }
        TimableOutput.println(
            String.format("OPEN-%d-%d", this.floor, this.id));
        sleep(200);
        ArrayList<PersonRequest> s1 = new ArrayList<>();
        if (!dest.isEmpty()) {
            for (PersonRequest person : this.dest) {
                if (person.getToFloor() == floor) {
                    TimableOutput.println(
                        String.format("OUT-%d-%d-%d", person.getPersonId(), this.floor, this.id));
                    number--;
                } else {
                    s1.add(person);
                }
            }
            this.dest = s1;
        }
        if (!people.isEmpty()) {
            for (PersonRequest person : people) {
                if (number >= 6) {
                    break;
                }
                this.dest.add(person);
                TimableOutput.println(
                    String.format("IN-%d-%d-%d", person.getPersonId(), this.floor, this.id));
                number++;
            }
        }
        sleep(200);
        TimableOutput.println(
            String.format("CLOSE-%d-%d", this.floor, this.id));
    }

    void reserve() {
        this.direction = !direction;
    }
}



