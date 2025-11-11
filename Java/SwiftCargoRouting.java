package DAA;
import java.util.*;

public class SwiftCargoRouting {
    static class Edge {
        int from, to, cost;
        Edge(int f, int t, int c) { from = f; to = t; cost = c; }
    }

    static int[] stage; // stage of each node
    static List<Edge>[] graph;

    static int[] dist, parent;

    static void shortestPath(int V, int stages) {
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0; // source in stage 0

        for (int s = 0; s < stages; s++) {
            for (int u = 0; u < V; u++) {
                if (stage[u] != s || dist[u] == Integer.MAX_VALUE) continue;
                for (Edge e : graph[u]) {
                    if (dist[e.to] > dist[u] + e.cost) {
                        dist[e.to] = dist[u] + e.cost;
                        parent[e.to] = u;
                    }
                }
            }
        }
    }

    static List<Integer> getPath(int dest) {
        List<Integer> path = new ArrayList<>();
        for (int i = dest; i != -1; i = parent[i]) path.add(i);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter total cities (nodes): ");
        int V = sc.nextInt();
        System.out.print("Enter total stages: ");
        int stages = sc.nextInt();

        stage = new int[V];
        System.out.println("Enter stage of each city (0 to " + (stages - 1) + "):");
        for (int i = 0; i < V; i++) stage[i] = sc.nextInt();

        graph = new ArrayList[V];
        for (int i = 0; i < V; i++) graph[i] = new ArrayList<>();

        System.out.print("Enter total routes (edges): ");
        int E = sc.nextInt();
        System.out.println("Enter each route: from to cost");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt(), v = sc.nextInt(), c = sc.nextInt();
            if (stage[v] > stage[u]) graph[u].add(new Edge(u, v, c));
        }

        dist = new int[V];
        parent = new int[V];
        Arrays.fill(parent, -1);

        shortestPath(V, stages);

        System.out.print("Enter destination city: ");
        int dest = sc.nextInt();

        System.out.println("\nMinimum Cost: " + dist[dest]);
        System.out.println("Optimal Path: " + getPath(dest));

        sc.close();
    }
}
