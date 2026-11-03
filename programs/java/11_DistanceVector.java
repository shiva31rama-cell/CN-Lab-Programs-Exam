import java.util.Scanner;

public class DistanceVector {
    static final int INF = 1000000000;

    static void printTables(int[][] distance, int[][] nextHop, String[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            System.out.println("\nRouting table for node " + nodes[i] + ":");
            System.out.printf("%-14s %-12s %s%n", "Destination", "Next Hop", "Distance");
            for (int j = 0; j < nodes.length; j++) {
                String hop = (i == j || nextHop[i][j] == -1) ? "-" : nodes[nextHop[i][j]];
                String cost = distance[i][j] >= INF ? "INF" : String.valueOf(distance[i][j]);
                System.out.printf("%-14s %-12s %s%n", nodes[j], hop, cost);
            }
        }
    }

    static void route(int[][] graph, String[] nodes) {
        int n = nodes.length;
        int[][] distance = new int[n][n], nextHop = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distance[i][j] = graph[i][j];
                nextHop[i][j] = (i != j && graph[i][j] < INF) ? j : -1;
            }
        }
        System.out.println("\nInitial routing tables:");
        printTables(distance, nextHop, nodes);

        int iteration = 0;
        while (true) {
            iteration++;
            boolean updated = false;
            int[][] newDistance = new int[n][n], newNext = new int[n][n];
            for (int i = 0; i < n; i++) {
                newDistance[i] = distance[i].clone();
                newNext[i] = nextHop[i].clone();
            }
            for (int source = 0; source < n; source++) {
                for (int destination = 0; destination < n; destination++) {
                    for (int neighbour = 0; neighbour < n; neighbour++) {
                        if (graph[source][neighbour] >= INF || distance[neighbour][destination] >= INF) continue;
                        int candidate = graph[source][neighbour] + distance[neighbour][destination];
                        if (candidate < newDistance[source][destination]) {
                            newDistance[source][destination] = candidate;
                            newNext[source][destination] = neighbour;
                            updated = true;
                        }
                    }
                }
            }
            distance = newDistance;
            nextHop = newNext;
            System.out.println("\n--- Iteration " + iteration + " ---");
            printTables(distance, nextHop, nodes);
            if (!updated) break;
        }
        System.out.println("\nRouting tables have converged.");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter number of nodes: ");
            int n = Integer.parseInt(sc.nextLine().trim());
            if (n <= 0) throw new IllegalArgumentException("Number of nodes must be positive.");
            System.out.print("Enter node names separated by spaces: ");
            String[] nodes = sc.nextLine().trim().split("\\s+");
            if (nodes.length != n) throw new IllegalArgumentException("Enter exactly " + n + " names.");
            int[][] graph = new int[n][n];
            System.out.println("Enter link-cost matrix row by row (-1 means no direct link):");
            for (int i = 0; i < n; i++) {
                System.out.print("Row " + (i + 1) + ": ");
                String[] values = sc.nextLine().trim().split("\\s+");
                if (values.length != n) throw new IllegalArgumentException("Each row needs " + n + " values.");
                for (int j = 0; j < n; j++) {
                    int value = Integer.parseInt(values[j]);
                    if (value < -1) throw new IllegalArgumentException("Use -1 or a non-negative cost.");
                    graph[i][j] = i == j ? 0 : (value == -1 ? INF : value);
                }
            }
            route(graph, nodes);
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}