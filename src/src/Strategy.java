import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Strategy {
    private final RequestTable requestTable;

    Strategy(RequestTable requestTable) {
        this.requestTable = requestTable;
    }

    public String advice(int floor, boolean direction,
                         ArrayList<PersonRequest> dest, int number) throws InterruptedException {
        if (openIn(floor, direction, number) || openOut(floor, dest)) {
            return "open";
        }
        if (!dest.isEmpty()) {
            return "move";
        } else {
            if (requestTable.isEmpty()) {
                if (requestTable.isOver()) {
                    return "over";
                } else {
                    return "wait";
                }
            }
            if (front(floor, direction)) {
                return "move";
            } else {
                return "reserve";
            }
        }
    }

    static boolean openOut(int floor, ArrayList<PersonRequest> dest) {
        if (!dest.isEmpty()) {
            for (PersonRequest person : dest) {
                if (person.getToFloor() == floor) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean openIn(int floor, boolean direction, int number) throws InterruptedException {
        if (!this.requestTable.isEmpty()) {
            if (requestTable.getWaitQueue().containsKey(floor) && (number < 6)) {
                ArrayList<PersonRequest> s = requestTable.getWaitQueue().get(floor);
                if (s != null) {
                    if (!s.isEmpty()) {
                        synchronized (requestTable) {
                            for (PersonRequest person : s) {
                                if (person.getToFloor() > floor && direction) {
                                    return true;
                                }
                                if (person.getToFloor() < floor && !direction) {
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

    boolean front(int floor, boolean direction) throws InterruptedException {
        if (!requestTable.isEmpty()) {
            synchronized (requestTable) {
                for (Integer i : requestTable.getWaitQueue().keySet()) {
                    if (i > floor && direction) {
                        return true;
                    } else if (i < floor && !direction) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}