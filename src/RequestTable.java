import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestTable {
    private boolean end;
    private boolean state;
    private final HashMap<Integer, ArrayList<PersonRequest>> waitQueue;
    private int maintainNow;

    RequestTable(HashMap<Integer, ArrayList<PersonRequest>> waitQueue) {
        this.waitQueue = waitQueue;
        this.end = false;
        this.state = false;
        this.maintainNow = 0;
    }

    synchronized void addRequest(PersonRequest person,int floor) {
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

    synchronized boolean getMaintain() {
        return this.maintainNow == 0;
    }

    synchronized void addMaintain() {
        this.maintainNow++;
    }

    synchronized void subMaintain() {
        this.maintainNow--;
        notifyAll();
    }
}
