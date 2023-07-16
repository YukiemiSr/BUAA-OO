import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Bfs {
    private int[][] path;

    public void initBfs() {
        this.path = new int[12][12];
        for (int i = 1; i <= 11; i++) {
            for (int j = 1; j <= 11; j++) {
                path[i][j] = 6;
            }
        }
    }

    synchronized void addBfs(int access) {
        int[] x = new int[12];
        for (int i = 1; i <= 11; i++) {
            if ((access & (1 << (i - 1))) != 0) {
                x[i] = 1;
            }
        }
        for (int i = 1; i <= 11; i++) {
            for (int j = 1; j <= 11; j++) {
                if ((x[i] == 1) && (x[j] == 1)) {
                    path[i][j]++;
                }
            }
        }
    }

    synchronized void subBfs(int access) {
        int[] x = new int[12];
        for (int i = 1; i <= 11; i++) {
            if ((access & (1 << (i - 1))) != 0) {
                x[i] = 1;
            }
        }
        for (int i = 1; i <= 11; i++) {
            for (int j = 1; j <= 11; j++) {
                if ((x[i] == 1) && (x[j] == 1)) {
                    path[i][j]--;
                }
            }
        }
        notifyAll();
    }

    public void setPath(Person person) {
        int finalFloor = person.getFinalFloor();
        int start = person.getFromFloor();
        int x = getNextNodeInShortestPath(path, start, finalFloor);
        person.changeToFloor(x);
    }

    public int getNextNodeInShortestPath(int[][] x, int start, int end) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        Map<Integer, Integer> parent = new HashMap<>();
        queue.add(start);
        visited.add(start);
        boolean found = false;
        while (!queue.isEmpty() && !found) {
            int curr = queue.poll();
            for (int i = 0; i < x.length; i++) {
                if (x[curr][i] != 0 && !visited.contains(i)) {
                    queue.add(i);
                    visited.add(i);
                    parent.put(i, curr);
                    if (i == end) {
                        found = true;
                        break;
                    }
                }
            }
        }
        List<Integer> path = new ArrayList<>();
        int curr = end;
        while (curr != start) {
            path.add(curr);
            curr = parent.get(curr);
        }
        path.add(start);
        Collections.reverse(path);
        if (path.size() < 2) {
            return -1;
        }
        return path.get(1);
    }
}
