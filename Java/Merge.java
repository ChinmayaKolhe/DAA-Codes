import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MergeSortOrders {

    // Generates n random timestamps (in seconds). Base is current time so values look realistic.
    static long[] generateTimestamps(int n) {
        long now = System.currentTimeMillis() / 1000L; // seconds
        long[] a = new long[n];
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        // generate timestamps in [now - range, now + range]
        long range = 100_000L; // ~ ~27 hours; adjust if you want wider range
        for (int i = 0; i < n; i++) {
            a[i] = now - range + rnd.nextLong(range * 2 + 1);
        }
        return a;
    }

    // In-place top-level merge sort wrapper that uses a single temp array
    static void mergeSort(long[] a) {
        int n = a.length;
        if (n <= 1) return;
        long[] temp = new long[n];
        mergeSortRecursive(a, temp, 0, n - 1);
    }

    // Recursive divide-and-merge using single temp buffer
    private static void mergeSortRecursive(long[] a, long[] temp, int left, int right) {
        if (left >= right) return;
        int mid = left + ((right - left) >> 1);
        mergeSortRecursive(a, temp, left, mid);
        mergeSortRecursive(a, temp, mid + 1, right);
        merge(a, temp, left, mid, right);
    }

    // Merge two sorted ranges: [left..mid] and [mid+1..right]
    private static void merge(long[] a, long[] temp, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) temp[k++] = a[i++];
            else temp[k++] = a[j++];
        }
        while (i <= mid) temp[k++] = a[i++];
        while (j <= right) temp[k++] = a[j++];
        // copy back
        for (k = left; k <= right; k++) a[k] = temp[k];
    }

    // Utility to print first/last few elements
    static void printSample(long[] a, int sampleCount) {
        int n = a.length;
        sampleCount = Math.min(sampleCount, n);
        System.out.println("First " + sampleCount + ":");
        for (int i = 0; i < sampleCount; i++) System.out.println("  " + i + " -> " + a[i]);
        System.out.println("Last " + sampleCount + ":");
        for (int i = n - sampleCount; i < n; i++) if (i >= 0) System.out.println("  " + i + " -> " + a[i]);
    }

    public static void main(String[] args) {
        // default size 1,000,000 (change here or pass as first arg)
        int n = 1_000_000;
        if (args.length > 0) {
            try { n = Integer.parseInt(args[0]); } catch (Exception ignored) {}
        }

        System.out.println("Generating " + n + " timestamps...");
        long[] arr = generateTimestamps(n);

        System.out.println("\nSample before sorting:");
        printSample(arr, 5);

        long start = System.nanoTime();
        mergeSort(arr);
        long end = System.nanoTime();

        System.out.println("\nSample after sorting:");
        printSample(arr, 5);

        double ms = (end - start) / 1e6;
        System.out.printf("\nSorted %d items in %.3f ms (%.3f s)\n", n, ms, ms / 1000.0);
    }
}
