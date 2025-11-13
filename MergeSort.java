import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

class Order implements Comparable<Order> {
    String id;
    long timestamp;

    Order(String id, long time) {
        this.id = id;
        this.timestamp = time;
    }

    @Override
    public int compareTo(Order o) {
        return Long.compare(this.timestamp, o.timestamp);
    }
}

public class MergeSort {

    // Convert UNIX timestamp to readable format
    static String formatTimestamp(long timestamp) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochSecond(timestamp));
    }

    // Generate random sample orders
    static List<Order> generateOrders(int n) {
        List<Order> list = new ArrayList<>();
        Random r = new Random();
        long now = System.currentTimeMillis() / 1000; // seconds

        for (int i = 1; i <= n; i++)
            list.add(new Order("ORD" + i, now + r.nextInt(100000)));

        return list;
    }

    // Merge Sort
    static void mergeSort(List<Order> list) {
        if (list.size() <= 1) return;
        int mid = list.size() / 2;
        List<Order> left = new ArrayList<>(list.subList(0, mid));
        List<Order> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSort(left);
        mergeSort(right);

        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size())
            list.set(k++, left.get(i).compareTo(right.get(j)) <= 0 ? left.get(i++) : right.get(j++));
        while (i < left.size()) list.set(k++, left.get(i++));
        while (j < right.size()) list.set(k++, right.get(j++));
    }

    public static void main(String[] args) {
        List<Order> orders = generateOrders(1000000); // smaller number for demo
        System.out.println("Before sorting (first 5):");
        for (int i = 0; i < 5; i++)
            System.out.println(orders.get(i).id + " -> " + formatTimestamp(orders.get(i).timestamp));

        long start = System.nanoTime();
        mergeSort(orders);
        long end = System.nanoTime();

        System.out.println("\nAfter sorting (first 5):");
        for (int i = 0; i < 5; i++)
            System.out.println(orders.get(i).id + " -> " + formatTimestamp(orders.get(i).timestamp));

        double time=(end-start)/1000000.0;
        System.out.println("\n Time taken:"+time);
    }
}