package DAA;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

class Movie {
    String title;
    float rating;
    int releaseYear;
    int popularity;

    public Movie(String title, float rating, int releaseYear, int popularity) {
        this.title = title;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.popularity = popularity;
    }

    public void display() {
        System.out.println(title + " | Rating: " + rating +
                " | Year: " + releaseYear +
                " | Popularity: " + popularity);
    }
}

public class MovieSorter {
    private static void quickSort(List<Movie> movies, Comparator<Movie> comparator) {
        if (movies.size() <= 1) return;

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{0, movies.size() - 1});

        while (!stack.isEmpty()) {
            int[] range = stack.pop();
            int low = range[0];
            int high = range[1];

            if (low < high) {
                int pivotIndex = partition(movies, low, high, comparator);
                if (pivotIndex - low < high - pivotIndex) {
                    stack.push(new int[]{pivotIndex + 1, high});
                    stack.push(new int[]{low, pivotIndex - 1});
                } else {
                    stack.push(new int[]{low, pivotIndex - 1});
                    stack.push(new int[]{pivotIndex + 1, high});
                }
            }
        }
    }

    private static int partition(List<Movie> movies, int low, int high, Comparator<Movie> comparator) {
        Movie pivot = movies.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(movies.get(j), pivot) < 0) {
                i++;
                Collections.swap(movies, i, j);
            }
        }
        Collections.swap(movies, i + 1, high);
        return i + 1;
    }

    private static void dualPivotQuickSort(List<Movie> movies, Comparator<Movie> comparator) {
        movies.sort(comparator);
    }

    private static List<Movie> loadMoviesFromCSV(String filename) {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);

                if (parts.length >= 4 && !parts[0].isEmpty()) {
                    try {
                        String title = parts[0];
                        float rating = Float.parseFloat(parts[1]);
                        int year = Integer.parseInt(parts[2]);
                        int popularity = Integer.parseInt(parts[3]);

                        movies.add(new Movie(title, rating, year, popularity));
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return movies;
    }

    public static void main(String[] args) {
        String filename = "E:\\DSA-With-Java\\src\\DAA\\movies.csv";
        List<Movie> movies = loadMoviesFromCSV(filename);

        if (movies.isEmpty()) {
            System.out.println("No movies loaded.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Sort movies by (rating/year/popularity): ");
        String sortBy = scanner.nextLine().toLowerCase();

        Comparator<Movie> comparator;
        switch (sortBy) {
            case "rating":
                comparator = Comparator.comparingDouble(m -> m.rating);
                break;
            case "year":
                comparator = Comparator.comparingInt(m -> m.releaseYear);
                break;
            case "popularity":
                comparator = Comparator.comparingInt(m -> m.popularity);
                break;
            default:
                System.out.println("Invalid choice. Defaulting to rating.");
                comparator = Comparator.comparingDouble(m -> m.rating);
        }

        long startTime = System.nanoTime();

        if (movies.size() > 10000) {
            dualPivotQuickSort(movies, comparator);
        } else {
            quickSort(movies, comparator);
        }

        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1_000_000_000.0;

        System.out.println("\nTop 10 movies sorted by " + sortBy + ":");
        int startIndex = Math.max(0, movies.size() - 10);
        for (int i = movies.size() - 1; i >= startIndex; i--) {
            movies.get(i).display();
        }

        System.out.printf("\nSorted %d movies in %.4f seconds.\n", movies.size(), timeTaken);

        scanner.close();
    }
}
