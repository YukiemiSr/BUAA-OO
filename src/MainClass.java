import com.oocourse.elevator3.TimableOutput;

import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Building building = new Building();
        building.initBuilding();
        HashMap<Integer, Building> buildings = new HashMap<>();
        buildings.put(1, building);
        In in = new In(buildings);
        in.start();
    }
}
