import com.oocourse.elevator3.TimableOutput;

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
    private int sym;
    private int access;
    private ArrayList<Person> dest;
    private Strategy strategy;
    private RequestTable requestTable;
    private ArrayList<Integer> ableFloor;

    public void initElevator(int id, int number, RequestTable requestTable, double speed,
                             int floor, ArrayList<Integer> ableFloor, int access) {
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
        this.sym = 0;
        this.ableFloor = ableFloor;
        this.access = access;
    }

    public void run() {
        String advice;
        lo: while (true) {
            try {
                advice =
                    strategy.advice(floor, direction, dest, number, fullNumber, maintain,
                        ableFloor);
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
                                maintain, ableFloor);
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
            requestTable.getServing(floor);
            requestTable.getOnlyIn(floor);
            TimableOutput.println(
                String.format("OPEN-%d-%d", this.floor, this.id));
            sleep(200);
            for (Person person : dest) {
                TimableOutput.println(
                    String.format("OUT-%d-%d-%d", person.getPersonId(), this.floor, this.id));
                if (person.getToFloor() != floor) {
                    person.changeFromFloor(floor);
                    requestTable.addRequest(person, floor);
                }
                else {
                    requestTable.addOutPerson();
                }
            }
            sleep(200);
            TimableOutput.println(
                String.format("CLOSE-%d-%d", this.floor, this.id));
            requestTable.releaseServing(floor);
            requestTable.releaseOnlyIn(floor);
            dest.clear();
        }
        TimableOutput.println(
            String.format("MAINTAIN_ABLE-%d", this.id));
        this.sym = 1;
        requestTable.subMaintain();
    }

    void setMaintain() {
        this.maintain = true;
    }

    void move() throws InterruptedException {
        sleep((long) (1000 * speed));
        if (Objects.equals(
            strategy.advice(floor, direction, dest, number, fullNumber, maintain, ableFloor),
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
        ArrayList<Person> people = new ArrayList<>();
        forPeople(people);
        int symOnly = 0;
        requestTable.getServing(floor);
        if (strategy.onlyOut(floor, direction, number, fullNumber, ableFloor, dest)) {
            symOnly = 1;
            requestTable.getOnlyIn(floor);
        }
        TimableOutput.println(
            String.format("OPEN-%d-%d", this.floor, this.id));
        sleep(200);
        ArrayList<Person> s1 = new ArrayList<>();
        if (!dest.isEmpty()) {
            for (Person person : this.dest) {
                if (person.getToFloor() == floor) {
                    TimableOutput.println(
                        String.format("OUT-%d-%d-%d", person.getPersonId(), this.floor, this.id));
                    requestTable.setPath(person);
                    if (person.getToFloor() == person.getFinalFloor()) {
                        requestTable.addOutPerson();
                    } else {
                        person.changeFromFloor(floor);
                        requestTable.addRequest(person, floor);
                    }
                    number--;
                } else {
                    s1.add(person);
                }
            }
            this.dest = s1;
        }
        if (!people.isEmpty()) {
            for (Person person : people) {
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
        if (symOnly == 1) {
            requestTable.releaseOnlyIn(floor);
        }
        requestTable.releaseServing(floor);
    }

    void reserve() {
        this.direction = !direction;
    }

    public int getAccess() {
        return this.access;
    }

    void forPeople(ArrayList<Person> people) throws InterruptedException {
        if (!requestTable.getWaitQueue().isEmpty()) {
            if (requestTable.getWaitQueue().containsKey(floor)) {
                if (!requestTable.getWaitQueue().get(floor).isEmpty()) {
                    ArrayList<Person> extra = new ArrayList<>();
                    int k = 0;
                    while ((k + number < fullNumber) &&
                        !requestTable.getWaitQueue().get(floor).isEmpty()) {
                        Person person = requestTable.getRequest(floor);
                        if (ableFloor.contains(person.getToFloor())) {
                            people.add(person);
                            k++;
                        }
                        else {
                            extra.add(person);
                        }
                        if (!requestTable.getWaitQueue().containsKey(floor)) {
                            break;
                        }
                    }
                    if (!extra.isEmpty()) {
                        for (Person person:extra) {
                            requestTable.addRequest(person,floor);
                        }
                    }
                }
            }
        }
    }
}



