import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.Objects;

public class Elevator extends Thread {
    private int id;
    private int floor;
    private int number;
    private int fullNumber;
    private boolean direction;
    private boolean maintain;
    private double speed;
    private boolean over;
    private int sym;
    private ArrayList<PersonRequest> dest;
    private Strategy strategy;
    private RequestTable requestTable;

    public void initElevator(int id, int number, RequestTable requestTable, double speed,
                             int floor) {
        this.id = id;
        this.floor = floor;
        this.direction = true;
        this.fullNumber = number;
        this.dest = new ArrayList<>();
        this.requestTable = requestTable;
        this.strategy = new Strategy(requestTable);
        this.speed = speed;
        this.number = 0;
        this.maintain = false;
        this.over = false;
        this.sym = 0;
    }

    public void run() {
        String advice;
        lo:
        while (true) {
            try {
                advice =
                    strategy.advice(floor, direction, dest, number, fullNumber, maintain);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            switch (advice) {
                case "over":
                    break lo;
                case "maintain":
                    try {
                        maintain();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break lo;

                case "move":
                    try {
                        move();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "open":
                    synchronized (requestTable) {
                        try {
                            String s = strategy.advice(floor, direction, dest, number, fullNumber,
                                maintain);
                            if (!Objects.equals(s, "open")) {
                                break;
                            }
                            open();
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
        if (sym == 0 && maintain) {
            setState();
        }
    }

    void setState() {
        TimableOutput.println(
            String.format("MAINTAIN_ABLE-%d", this.id));
        requestTable.subMaintain();
        sym = 1;
    }

    void maintain() throws InterruptedException {
        if (!dest.isEmpty()) {
            TimableOutput.println(
                String.format("OPEN-%d-%d", this.floor, this.id));
            sleep(200);
            for (PersonRequest person : dest) {
                TimableOutput.println(
                    String.format("OUT-%d-%d-%d", person.getPersonId(), this.floor, this.id));
                if (person.getToFloor() != floor) {
                    requestTable.addRequest(person,floor);
                }
            }
            sleep(200);
            TimableOutput.println(
                String.format("CLOSE-%d-%d", this.floor, this.id));
            dest.clear();
        }
        TimableOutput.println(
            String.format("MAINTAIN_ABLE-%d", this.id));
        this.over = true;
        this.sym = 1;
        requestTable.subMaintain();
    }

    void setMaintain() {
        this.maintain = true;
    }

    void move() throws InterruptedException {
        sleep((long) (1000 * speed));
        if (Objects.equals(
            strategy.advice(floor, direction, dest, number, fullNumber, maintain),
            "maintain")) {
            return;
        }
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

    void open() throws InterruptedException {
        ArrayList<PersonRequest> people = new ArrayList<>();
        if (!requestTable.getWaitQueue().isEmpty()) {
            if (requestTable.getWaitQueue().containsKey(floor)) {
                if (!requestTable.getWaitQueue().get(floor).isEmpty()) {
                    int k = 0;
                    while ((k + number < fullNumber) &&
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
                if (number >= fullNumber) {
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



