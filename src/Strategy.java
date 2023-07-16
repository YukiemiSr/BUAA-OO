import java.util.ArrayList;
import java.util.Map;

public class Strategy {
    private final RequestTable requestTable;

    Strategy(RequestTable requestTable) {
        this.requestTable = requestTable;
    }

    public String advice(int floor, boolean direction,
                         ArrayList<Person> dest, int number,
                         int fullNumber, boolean maintain,
                         ArrayList<Integer> ableFloor)
        throws InterruptedException {
        if (maintain) {
            return "maintain";
        }
        if (openIn(floor, direction, number, fullNumber, ableFloor) || openOut(floor, dest)) {
            return "open";
        }
        if (!dest.isEmpty()) {
            return "move";
        } else {
            if (requestTable.isEmpty()) {
                if (requestTable.isOver() && requestTable.getMaintain() &&
                    requestTable.inEqualOut()) {
                    return "over";
                } else {
                    return "wait";
                }
            }
            if (front(floor, direction, ableFloor)) {
                return "move";
            } else {
                if (!can(floor, direction, ableFloor)) {
                    return "wait";
                }
                return "reserve";
            }
        }
    }

    public boolean onlyOut(int floor, boolean direction, int number, int fullNumber,
                           ArrayList<Integer> ableFloor, ArrayList<Person> dest)
        throws InterruptedException {
        return (!openIn(floor, direction, number, fullNumber, ableFloor)) && (openOut(floor, dest));
    }

    static boolean openOut(int floor, ArrayList<Person> dest) {
        if (!dest.isEmpty()) {
            for (Person person : dest) {
                if (person.getToFloor() == floor) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean openIn(int floor, boolean direction, int number, int fullNumber,
                   ArrayList<Integer> ableFloor)
        throws InterruptedException {
        if (!this.requestTable.isEmpty()) {
            if (requestTable.getWaitQueue().containsKey(floor) && (number < fullNumber)) {
                ArrayList<Person> s = requestTable.getWaitQueue().get(floor);
                if (s != null) {
                    if (!s.isEmpty()) {
                        synchronized (requestTable) {
                            for (Person person : s) {
                                if (person.getToFloor() > floor && direction &&
                                    ableFloor.contains(person.getToFloor()) &&
                                    ableFloor.contains(person.getFromFloor()) &&
                                    ableFloor.contains(floor)) {
                                    return true;
                                }
                                if (person.getToFloor() < floor && !direction &&
                                    ableFloor.contains(person.getToFloor()) &&
                                    ableFloor.contains(person.getFromFloor()) &&
                                    ableFloor.contains(floor)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean front(int floor, boolean direction, ArrayList<Integer> ableFloor)
        throws InterruptedException {
        if (!requestTable.isEmpty()) {
            synchronized (requestTable) {
                for (Map.Entry<Integer, ArrayList<Person>> entry : requestTable.getWaitQueue()
                    .entrySet()) {
                    int i = entry.getKey();
                    if (i > floor && direction) {
                        for (Person person : entry.getValue()) {
                            if (ableFloor.contains(person.getToFloor()) &&
                                ableFloor.contains(person.getFromFloor())) {
                                return true;
                            }
                        }
                    } else if (i < floor && !direction) {
                        for (Person person : entry.getValue()) {
                            if (ableFloor.contains(person.getToFloor()) &&
                                ableFloor.contains(person.getFromFloor())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean can(int floor, boolean direction, ArrayList<Integer> ableFloor)
        throws InterruptedException {
        if (!requestTable.isEmpty()) {
            synchronized (requestTable) {
                for (Map.Entry<Integer, ArrayList<Person>> entry : requestTable.getWaitQueue()
                    .entrySet()) {
                    for (Person person : entry.getValue()) {
                        if (ableFloor.contains(person.getToFloor()) &&
                            ableFloor.contains(person.getFromFloor())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}