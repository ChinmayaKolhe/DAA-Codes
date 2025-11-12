package DAA;
import java.util.*;

class Item {
    String name;
    double weight, value;
    boolean divisible;
    int priority; // 1=High, 2=Medium, 3=Low

    Item(String n, double w, double v, boolean d, int p) {
        name = n; weight = w; value = v; divisible = d; priority = p;
    }

    double ratio() { return value / weight; }
}

public class FractionalKnapsack {

    static double knapsack(List<Item> items, double capacity) {
        // Sort by priority, then by value/weight ratio
        items.sort((a, b) -> {
            if (a.priority != b.priority) return a.priority - b.priority;
            return Double.compare(b.ratio(), a.ratio());
        });

        double totalValue = 0, totalWeight = 0;
        System.out.println("\nSelected items:");

        for (Item it : items) {
            if (capacity <= 0) break;

            double take = 0;
            if (it.divisible) { // partial allowed
                take = Math.min(it.weight, capacity);
            } else if (it.weight <= capacity) { // full only
                take = it.weight;
            }

            if (take > 0) {
                double val = it.ratio() * take;
                totalValue += val;
                totalWeight += take;
                capacity -= take;

                System.out.println(" - " + it.name +
                        " | Taken: " + take + "kg" +
                        " | Value: " + val +
                        " | Priority: " + it.priority +
                        " | Type: " + (it.divisible ? "Divisible" : "Indivisible"));
            }
        }

        System.out.printf("\nTotal weight: %.2f kg\nTotal value: %.2f\n",
                totalWeight, totalValue);
        return totalValue;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of items: ");
        int n = sc.nextInt();

        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            System.out.println("\nItem " + i + ":");
            System.out.print("Name: "); String name = sc.next();
            System.out.print("Weight: "); double w = sc.nextDouble();
            System.out.print("Value: "); double v = sc.nextDouble();
            System.out.print("Divisible (1/0): "); boolean d = sc.nextInt() == 1;
            System.out.print("Priority (1=High,2=Med,3=Low): "); int p = sc.nextInt();
            items.add(new Item(name, w, v, d, p));
        }

        System.out.print("\nEnter max capacity: ");
        double cap = sc.nextDouble();

        knapsack(items, cap);
        sc.close();
    }
}
