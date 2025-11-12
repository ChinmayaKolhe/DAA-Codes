package DAA;
import java.io.*;
import java.util.*;

class Movie {
    String title; float rating; int year, popularity;
    Movie(String t, float r, int y, int p) {
        title = t; rating = r; year = y; popularity = p;
    }
    void show() {
        System.out.println(title + " | " + rating + "★ | " + year + " | " + popularity);
    }
}

public class MovieSorter {
    static List<Movie> readCSV(String file) {
        List<Movie> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 4)
                    list.add(new Movie(p[0], Float.parseFloat(p[1]),
                            Integer.parseInt(p[2]), Integer.parseInt(p[3])));
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        return list;
    }

    static void quickSort(List<Movie> a, int l, int h, Comparator<Movie> c) {
        if (l < h) {
            int i = l - 1; Movie p = a.get(h);
            for (int j = l; j < h; j++)
                if (c.compare(a.get(j), p) < 0) Collections.swap(a, ++i, j);
            Collections.swap(a, i + 1, h);
            int pi = i + 1;
            quickSort(a, l, pi - 1, c); quickSort(a, pi + 1, h, c);
        }
    }

    public static void main(String[] args) {
        String path = "E:/DSA-With-Java/src/DAA/movies.csv";

        List<Movie> m = readCSV(path);
        if (m.isEmpty()) return;

        Comparator<Movie> cmp = Comparator.comparingDouble(x -> x.rating); // sort by rating
        long t1 = System.nanoTime();
        quickSort(m, 0, m.size() - 1, cmp);
        long t2 = System.nanoTime();

        System.out.println("Top 5 Movies (by Rating):");
        for (int i = Math.max(0, m.size() - 5); i < m.size(); i++) m.get(i).show();
        System.out.printf("\nSorted %d movies in %.3f ms\n", m.size(), (t2 - t1) / 1e6);
    }
}
