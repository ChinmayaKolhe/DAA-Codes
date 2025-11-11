package DAA;
import java.util.*;

public class DijkstraAmbulance {

    static class Edge {
        int to, weight;
        Edge(int t, int w) { to = t; weight = w; }
    }

    static void dijkstra(int src, List<List<Edge>> g, int[] dist, int[] parent) {
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        Arrays.fill(parent, -1);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0], d = cur[1];
            if (d > dist[u]) continue;

            for (Edge e : g.get(u)) {
                if (dist[e.to] > d + e.weight) {
                    dist[e.to] = d + e.weight;
                    parent[e.to] = u;
                    pq.add(new int[]{e.to, dist[e.to]});
                }
            }
        }
    }

    static List<Integer> getPath(int dest, int[] parent) {
        List<Integer> path = new ArrayList<>();
        for (int i = dest; i != -1; i = parent[i]) path.add(i);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter intersections (V) and roads (E): ");
        int V = sc.nextInt(), E = sc.nextInt();

        List<List<Edge>> g = new ArrayList<>();
        for (int i = 0; i < V; i++) g.add(new ArrayList<>());

        System.out.println("Enter each road: u v w");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt(), v = sc.nextInt(), w = sc.nextInt();
            g.get(u).add(new Edge(v, w));
            g.get(v).add(new Edge(u, w)); // bidirectional
        }

        System.out.print("Enter ambulance source: ");
        int src = sc.nextInt();

        System.out.print("Enter number of hospitals: ");
        int hCount = sc.nextInt();
        int[] hospitals = new int[hCount];
        System.out.print("Enter hospital nodes: ");
        for (int i = 0; i < hCount; i++) hospitals[i] = sc.nextInt();

        int[] dist = new int[V], parent = new int[V];
        dijkstra(src, g, dist, parent);

        System.out.println("\n--- All Shortest Routes ---");
        for (int i = 0; i < V; i++) {
            if (dist[i] == Integer.MAX_VALUE) continue;
            System.out.println("To " + i + " (" + dist[i] + " min): " + getPath(i, parent));
        }

        // Find nearest hospital
        int nearest = -1, min = Integer.MAX_VALUE;
        for (int h : hospitals)
            if (dist[h] < min) { min = dist[h]; nearest = h; }

        System.out.println("\n--- Nearest Hospital ---");
        if (nearest == -1)
            System.out.println("No hospital reachable.");
        else
            System.out.println("Hospital " + nearest + " in " + min + " min via " + getPath(nearest, parent));

        sc.close();
    }
}
