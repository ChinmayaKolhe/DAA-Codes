package DAA;
import java.util.*;

public class TSP {
    static int N;
    static int[][] dist;
    static int minCost = Integer.MAX_VALUE;
    static int[] bestPath;

    static void tspBranchBound(int[] path, boolean[] visited, int level, int cost) {
        if (level == N) {
            cost += dist[path[level-1]][path[0]];
            if (cost < minCost) {
                minCost = cost;
                bestPath = path.clone();
            }
            return;
        }

        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                int newCost = cost + dist[path[level-1]][i];
                if (newCost < minCost) { // Bound condition
                    path[level] = i;
                    visited[i] = true;
                    tspBranchBound(path, visited, level + 1, newCost);
                    visited[i] = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of cities: ");
        N = sc.nextInt();

        dist = new int[N][N];
        System.out.println("Enter distance matrix:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dist[i][j] = sc.nextInt();
            }
        }

        int[] path = new int[N];
        boolean[] visited = new boolean[N];
        path[0] = 0; // Start from city 0
        visited[0] = true;

        System.out.println("\nCalculating optimal route...");
        tspBranchBound(path, visited, 1, 0);

        System.out.println("\nOptimal Route:");
        for (int i = 0; i < N; i++) {
            System.out.print(bestPath[i] + " → ");
        }
        System.out.println(bestPath[0]);

        System.out.println("Minimum Cost: " + minCost);

        sc.close();
    }
}