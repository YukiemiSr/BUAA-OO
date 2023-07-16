package com.oocourse.spec3.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;

public class MinPath {
    static final int MAXN = 10050;
    static final int inf = 0x003f3f3f;
    private final int[] ans = new int[MAXN];
    private final int[] pre = new int[MAXN];
    private final int[] dis = new int[MAXN];
    private HashMap<Integer, Integer> map;
    private HashMap<Integer, Person> people;

    public int find(int x) {
        if (pre[x] != x) {
            pre[x] = find(pre[x]);
        }
        return pre[x];
    }

    public void init(HashMap<Integer, Integer> map, HashMap<Integer, Person> people) {
        this.people = people;
        this.map = map;
    }

    void dijkstra(int s) {
        boolean[] visited = new boolean[people.size()];
        Arrays.fill(visited, false);
        Arrays.fill(dis, inf);
        Arrays.fill(ans, inf);
        for (int i = 0; i < people.size(); ++i) {
            pre[i] = i;
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>((u, v) -> dis[u] - dis[v]);
        pq.offer(s);
        dis[s] = 0;
        while (!pq.isEmpty()) {
            int u = pq.poll();
            if (visited[u]) {
                continue;
            }
            visited[u] = true;
            for (int v = 0; v < people.size(); ++v) {
                int x = exchange(u, v);
                if (!visited[v] && dis[u] + x < dis[v]) {
                    dis[v] = dis[u] + x;
                    if (u != s) {
                        pre[v] = u;
                    }
                    pq.offer(v);
                }
            }
        }
    }

    public int search(int id) {
        int n = people.size();
        dijkstra(id);
        for (int i = 0; i < n; ++i) {
            if (pre[i] != i) {
                ans[id] = Math.min(ans[id], exchange(id, i) + dis[i]);
            }
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i != id && j != id && find(i) != find(j)) {
                    ans[id] = Math.min(ans[id], dis[i] + dis[j] + exchange(i, j));
                }
            }
        }
        return ans[id];
    }

    int exchange(int x, int y) {
        if (people.get(map.get(x)).isLinked(people.get(map.get(y)))) {
            return people.get(map.get(x)).queryValue(people.get(map.get(y)));
        } else {
            return inf;
        }
    }
}
