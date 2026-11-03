import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Dijkstra {
    static final int INF = Integer.MAX_VALUE;

    static void shortestPath(int[][] graph, String[] names, int source, int destination) {
        int n = graph.length;
        int[] distance = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) { distance[i] = INF; parent[i] = -1; }
        distance[source] = 0;

        for (int count = 0; count < n; count++) {
            int current = -1, best = INF;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && distance[i] < best) { best = distance[i]; current = i; }
            }
            if (current == -1) break;
            visited[current] = true;
            for (int v = 0; v < n; v++) {
                if (graph[current][v] > 0 && !visited[v] && distance[current] != INF
                        && distance[current] + graph[current][v] < distance[v]) {
                    distance[v] = distance[current] + graph[current][v];
                    parent[v] = current;
                }
            }
        }

        if (distance[destination] == INF) {
            System.out.println("No path exists between the selected vertices.");
            return;
        }
        List<String> path = new ArrayList<>();
        for (int at = destination; at != -1; at = parent[at]) path.add(names[at]);
        Collections.reverse(path);
        System.out.println("Shortest path: " + String.join(" -> ", path));
        System.out.println("Total cost: " + distance[destination]);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter number of vertices: ");
            int n = Integer.parseInt(sc.nextLine().trim());
            if (n <= 0) throw new IllegalArgumentException("Number of vertices must be positive.");
            System.out.print("Enter vertex names separated by spaces: ");
            String[] names = sc.nextLine().trim().split("\\s+");
            if (names.length != n) throw new IllegalArgumentException("Enter exactly " + n + " names.");
            int[][] graph = new int[n][n];
            System.out.println("Enter adjacency matrix row by row (0 means no edge):");
            for (int i = 0; i < n; i++) {
                System.out.print("Row " + (i + 1) + ": ");
                String[] values = sc.nextLine().trim().split("\\s+");
                if (values.length != n) throw new IllegalArgumentException("Each row needs " + n + " values.");
                for (int j = 0; j < n; j++) {
                    graph[i][j] = Integer.parseInt(values[j]);
                    if (graph[i][j] < 0) throw new IllegalArgumentException("Weights cannot be negative.");
                }
            }
            for (int i = 0; i < n; i++) System.out.println((i + 1) + ": " + names[i]);
            System.out.print("Enter source position: ");
            int source = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.print("Enter destination position: ");
            int destination = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (source < 0 || source >= n || destination < 0 || destination >= n)
                throw new IllegalArgumentException("Source or destination is out of range.");
            shortestPath(graph, names, source, destination);
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}