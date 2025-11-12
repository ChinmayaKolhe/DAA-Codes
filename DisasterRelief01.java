package DAA;
import java.util.*;

public class DisasterRelief01 {

    static class Item {
        String name;
        int weight, value, priority; // smaller priority number = higher priority
        Item(String n, int w, int v, int p) { name = n; weight = w; value = v; priority = p; }
    }

    // 0/1 Knapsack using Dynamic Programming
    static int[][] knapsackDP(List<Item> items, int W) {
        int n = items.size();
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            Item it = items.get(i - 1);
            for (int w = 0; w <= W; w++) {
                if (it.weight <= w)
                    dp[i][w] = Math.max(it.value + dp[i - 1][w - it.weight], dp[i - 1][w]);
                else dp[i][w] = dp[i - 1][w];
            }
        }
        return dp;
    }

    static List<Item> getSelectedItems(List<Item> items, int W, int[][] dp) {
        List<Item> selected = new ArrayList<>();
        int n = items.size(), w = W;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Item it = items.get(i - 1);
                selected.add(it);
                w -= it.weight;
            }
        }
        Collections.reverse(selected);
        return selected;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of items: ");
        int n = sc.nextInt();

        List<Item> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.println("\nItem #" + (i + 1));
            System.out.print("Name: "); String name = sc.next();
            System.out.print("Weight (kg): "); int weight = sc.nextInt();
            System.out.print("Utility Value: "); int value = sc.nextInt();
            System.out.print("Priority (1=High, 2=Medium, 3=Low): "); int p = sc.nextInt();
            items.add(new Item(name, weight, value, p));
        }

        System.out.print("\nEnter truck capacity (W in kg): ");
        int W = sc.nextInt();

        // Sort by priority before applying knapsack
        items.sort(Comparator.comparingInt(it -> it.priority));

        int[][] dp = knapsackDP(items, W);
        List<Item> selected = getSelectedItems(items, W, dp);

        int totalValue = dp[items.size()][W];
        int totalWeight = selected.stream().mapToInt(i -> i.weight).sum();

        System.out.println("\n===== Optimal Resource Allocation =====");
        for (Item it : selected)
            System.out.println(it.name + " | Weight: " + it.weight + " | Value: " + it.value + " | Priority: " + it.priority);
        System.out.println("------------------------------------");
        System.out.println("Total Weight: " + totalWeight + " / " + W);
        System.out.println("Total Utility: " + totalValue);

        sc.close();
    }
}
