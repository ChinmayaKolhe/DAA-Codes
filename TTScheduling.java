package DAA;
import java.util.*;

public class TTScheduling {

    static int V; // number of courses
    static int[][] graph; // adjacency matrix
    static int[] color; // color assignment
    static int totalColors; // total available slots

    // Check if the current color can be assigned to course v
    static boolean isSafe(int v, int c) {
        for (int i = 0; i < V; i++)
            if (graph[v][i] == 1 && color[i] == c) // adjacent course with same color
                return false;
        return true;
    }

    // Backtracking function
    static boolean solveGraphColoring(int v) {
        if (v == V) return true; // all courses colored successfully

        for (int c = 1; c <= totalColors; c++) {
            if (isSafe(v, c)) {
                color[v] = c;
                if (solveGraphColoring(v + 1)) return true;
                color[v] = 0; // backtrack
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of courses: ");
        V = sc.nextInt();
        graph = new int[V][V];

        System.out.print("Enter number of conflict pairs (edges): ");
        int E = sc.nextInt();

        System.out.println("Enter conflict pairs (u v): ");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt(), v = sc.nextInt();
            graph[u][v] = graph[v][u] = 1;
        }

        System.out.print("Enter maximum number of available exam slots: ");
        totalColors = sc.nextInt();

        color = new int[V];
        Arrays.fill(color, 0);

        if (solveGraphColoring(0)) {
            System.out.println("\n===== Exam Timetable (Course → Slot) =====");
            for (int i = 0; i < V; i++)
                System.out.println("Course " + i + " → Slot " + color[i]);
            System.out.println("\nTotal Slots Used: " + Arrays.stream(color).max().getAsInt());
        } else {
            System.out.println("No valid scheduling possible with given slots.");
        }

        // --- Optional Room Allocation ---
        System.out.print("\nEnter number of classrooms available per slot: ");
        int rooms = sc.nextInt();
        int maxSlot = Arrays.stream(color).max().getAsInt();
        if (rooms < maxSlot)
            System.out.println("Not enough rooms for all exam slots!");
        else
            System.out.println("Room allocation possible for all slots.");

        sc.close();
    }
}
