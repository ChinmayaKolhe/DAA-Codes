package DAA;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

class Order implements Comparable<Order> {
    String orderId;
    long timestamp;

    public Order(String orderId, long timestamp) {
        this.orderId = orderId;
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Order other) {
        return Long.compare(this.timestamp, other.timestamp);
    }
}

public class MergeSort {
    private static final int NUM_ORDERS = 1000000;

    // Generate random orders
    public static List<Order> generateSampleOrders(int n) {
        List<Order> orders = new ArrayList<>(n);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2025, Calendar.JUNE, 24, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long baseTime = calendar.getTimeInMillis() / 1000; // Convert to seconds

        Random random = new Random();

        for (int i = 0; i < n; i++) {
            int randomMinutes = random.nextInt(100000); // up to ~70 days
            long timestamp = baseTime + (randomMinutes * 60);
            orders.add(new Order("ORD" + (i + 1), timestamp));
        }

        return orders;
    }

    // Merge Sort implementation
    public static void mergeSort(List<Order> orders) {
        if (orders.size() <= 1) return;

        int mid = orders.size() / 2;
        List<Order> left = new ArrayList<>(orders.subList(0, mid));
        List<Order> right = new ArrayList<>(orders.subList(mid, orders.size()));

        mergeSort(left);
        mergeSort(right);

        merge(orders, left, right);
    }

    private static void merge(List<Order> result, List<Order> left, List<Order> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).timestamp <= right.get(j).timestamp) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }

    // Print first n orders
    public static void printFirstNOrders(List<Order> orders, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        int limit = Math.min(n, orders.size());
        for (int i = 0; i < limit; i++) {
            Order order = orders.get(i);
            Date date = new Date(order.timestamp * 1000L); // Convert to milliseconds
            System.out.println("Order ID: " + order.orderId +
                    ", Timestamp: " + sdf.format(date));
        }
    }

    public static void main(String[] args) {
        System.out.println("Generating orders...");
        List<Order> orders = generateSampleOrders(NUM_ORDERS);

        System.out.println("Sorting orders by timestamp...");
        long startTime = System.nanoTime();
        mergeSort(orders);
        long endTime = System.nanoTime();

        double timeTaken = (endTime - startTime) / 1_000_000_000.0;
        System.out.printf("Done! Sorted %d orders in %.2f seconds.%n",
                NUM_ORDERS, timeTaken);

        System.out.println("\nFirst 5 Sorted Orders:");
        printFirstNOrders(orders, 5);
    }
}
