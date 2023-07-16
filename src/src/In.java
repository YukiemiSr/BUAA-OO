import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class In extends Thread {
    private final RequestTable requestTable;

    In(RequestTable requestTable) {
        this.requestTable = requestTable;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            synchronized (requestTable) {
                if (request == null) {
                    requestTable.setOver();
                    break;
                } else {
                    try {
                        requestTable.addRequest(request);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
