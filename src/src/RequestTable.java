import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestTable {
    private boolean end;
    private HashMap<Integer, ArrayList<PersonRequest>> waitQueue;

    RequestTable(HashMap<Integer, ArrayList<PersonRequest>> waitQueue) {
        this.waitQueue = waitQueue;
        this.end = false;
    }

    synchronized void addRequest(PersonRequest person) throws InterruptedException {
        int floor = person.getFromFloor();
        if (!waitQueue.containsKey(floor)) {
            ArrayList<PersonRequest> list = new ArrayList<>();
            list.add(person);
            waitQueue.put(floor, list);
        } else {
            waitQueue.get(floor).add(person);
        }
        notifyAll();
    }

    synchronized PersonRequest getRequest(int floor) throws InterruptedException {
        if (isEmpty() && !isOver()) {
            wait();
        }
        PersonRequest person = waitQueue.get(floor).get(0);
        waitQueue.get(floor).remove(person);
        if (waitQueue.get(floor).isEmpty()) {
            waitQueue.remove(floor);
        }
        return person;
    }

    synchronized boolean isEmpty() {
        return waitQueue.isEmpty();
    }

    synchronized boolean isOver() {
        return end;
    }

    synchronized void setOver() {
        this.end = true;
        notifyAll();
    }

    synchronized HashMap<Integer, ArrayList<PersonRequest>> getWaitQueue()
        throws InterruptedException {
        if (isEmpty() && !isOver()) {
            wait();
        }
        return this.waitQueue;
    }
}
