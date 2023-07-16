import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        HashMap<Integer, ArrayList<PersonRequest>> queue = new HashMap<>();
        RequestTable requestTable = new RequestTable(queue);
        Building building = new Building();
        HashMap<Integer, Building> buildings = new HashMap<>();
        building.initBuilding(requestTable);
        buildings.put(1, building);
        In in = new In(buildings);
        in.start();
    }
}
