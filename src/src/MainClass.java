import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        HashMap<Integer, ArrayList<PersonRequest>> queue = new HashMap<>();
        RequestTable requestTable = new RequestTable(queue);
        In in = new In(requestTable);
        in.start();
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator();
            elevator.initElevator(i, requestTable);
            elevator.start();
        }
    }
}
