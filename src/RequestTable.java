import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class RequestTable {
    private boolean end;
    private final HashMap<Integer, ArrayList<Person>> waitQueue;
    private int maintainNow;
    private final HashMap<Integer, Integer> serveNow;
    private final HashMap<Integer, Integer> openNow;
    private final Bfs bfs;
    private int inPerson;
    private final HashMap<Integer, Semaphore> serving;
    private HashMap<Integer, Semaphore> onlyIn;

    RequestTable(Bfs bfs) {
        this.waitQueue = new HashMap<>();
        this.end = false;
        this.maintainNow = 0;
        this.serveNow = new HashMap<>();
        this.openNow = new HashMap<>();
        this.bfs = bfs;
        this.serving = new HashMap<>();
        this.onlyIn = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            serveNow.put(i, 0);
            openNow.put(i, 0);
            Semaphore semaphore1 = new Semaphore(4);
            serving.put(i, semaphore1);
            Semaphore semaphore2 = new Semaphore(2);
            onlyIn.put(i, semaphore2);
        }
    }

    public void getServing(int floor) throws InterruptedException {
        this.serving.get(floor).acquire();
    }

    public void releaseServing(int floor) {
        this.serving.get(floor).release();
    }

    public void getOnlyIn(int floor) throws InterruptedException {
        this.onlyIn.get(floor).acquire();
    }

    public void releaseOnlyIn(int floor) {
        this.onlyIn.get(floor).release();
    }

    synchronized void addRequest(Person person, int floor) {
        person.changeFromFloor(floor);
        bfs.setPath(person);
        if (!waitQueue.containsKey(floor)) {
            ArrayList<Person> list = new ArrayList<>();
            list.add(person);
            waitQueue.put(floor, list);
        } else {
            waitQueue.get(floor).add(person);
        }
        notifyAll();
    }

    synchronized Person getRequest(int floor) throws InterruptedException {
        if (isEmpty() && !isOver()) {
            wait();
        }
        Person person = waitQueue.get(floor).get(0);
        waitQueue.get(floor).remove(person);
        if (waitQueue.get(floor).isEmpty()) {
            waitQueue.remove(floor);
        }
        bfs.setPath(person);
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

    synchronized HashMap<Integer, ArrayList<Person>> getWaitQueue()
        throws InterruptedException {
        if (isEmpty() && !isOver() && !inEqualOut()) {
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

    synchronized void changeAll() {
        for (Map.Entry<Integer, ArrayList<Person>> entry : waitQueue.entrySet()) {
            if (entry.getValue() != null) {
                if (!entry.getValue().isEmpty()) {
                    for (Person person : entry.getValue()) {
                        bfs.setPath(person);
                    }
                }
            }
        }
    }

    synchronized void addInPerson() {
        this.inPerson++;
    }

    synchronized void addOutPerson() {
        this.inPerson--;
        if (inPerson == 0) {
            notifyAll();
        }
    }

    synchronized boolean inEqualOut() {
        return inPerson == 0;
    }

    public void setPath(Person person) {
        bfs.setPath(person);
    }
}
