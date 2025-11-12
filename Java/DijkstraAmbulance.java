package DAA;
import java.util.*;

public class DijkstraAmbulance {
    static class Edge {
        int to, w;
        Edge(int t, int w) { this.to = t; this.w = w; }
    }

    static void dijkstra(int src, List<List<Edge>> g, int[] dist, int[] par) {
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0; Arrays.fill(par, -1);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0], d = cur[1];
            if (d > dist[u]) continue;

            for (Edge e : g.get(u)) {
                if (dist[e.to] > d + e.w) {
                    dist[e.to] = d + e.w;
                    par[e.to] = u;
                    pq.add(new int[]{e.to, dist[e.to]});
                }
            }
        }
    }

    static List<Integer> path(int dest, int[] par) {
        List<Integer> p = new ArrayList<>();
        for (int i = dest; i != -1; i = par[i]) p.add(i);
        Collections.reverse(p);
        return p;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter vertices and edges: ");
        int V = sc.nextInt(), E = sc.nextInt();

        List<List<Edge>> g = new ArrayList<>();
        for (int i = 0; i < V; i++) g.add(new ArrayList<>());

        System.out.println("Enter edges (u v w):");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt(), v = sc.nextInt(), w = sc.nextInt();
            g.get(u).add(new Edge(v, w));
            g.get(v).add(new Edge(u, w)); // bidirectional
        }

        System.out.print("Enter ambulance start: ");
        int src = sc.nextInt();

        System.out.print("Enter hospital count: ");
        int h = sc.nextInt();
        int[] hosp = new int[h];
        System.out.print("Hospitals: ");
        for (int i = 0; i < h; i++) hosp[i] = sc.nextInt();

        int[] dist = new int[V], par = new int[V];
        dijkstra(src, g, dist, par);

        int nearest = -1, min = Integer.MAX_VALUE;
        for (int x : hosp) if (dist[x] < min) { min = dist[x]; nearest = x; }

        System.out.println("\nNearest hospital: " + nearest + " (" + min + " min)");
        System.out.println("Path: " + path(nearest, par));
        sc.close();
    }
}
