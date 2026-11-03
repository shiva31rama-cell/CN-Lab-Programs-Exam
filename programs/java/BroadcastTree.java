import java.util.Scanner;

public class BroadcastTree {
    static void prims(int[][] graph) {
        int n = graph.length;
        boolean[] selected = new boolean[n];
        selected[0] = true;
        int edges = 0, total = 0;
        System.out.println("\nBroadcast tree edges:");
        System.out.println("Edge\tWeight");

        while (edges < n - 1) {
            int minimum = Integer.MAX_VALUE, start = -1, end = -1;
            for (int i = 0; i < n; i++) {
                if (selected[i]) {
                    for (int j = 0; j < n; j++) {
                        if (!selected[j] && graph[i][j] > 0 && graph[i][j] < minimum) {
                            minimum = graph[i][j];
                            start = i;
                            end = j;
                        }
                    }
                }
            }
            if (start == -1) {
                System.out.println("The graph is disconnected; a spanning broadcast tree cannot be formed.");
                return;
            }
            selected[end] = true;
            edges++;
            total += graph[start][end];
            System.out.println((start + 1) + " - " + (end + 1) + "\t" + graph[start][end]);
        }
        System.out.println("Total tree weight: " + total);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Enter number of hosts: ");
            int n = Integer.parseInt(sc.nextLine().trim());
            if (n <= 0) throw new IllegalArgumentException("Number of hosts must be positive.");
            int[][] graph = new int[n][n];
            System.out.println("Enter cost adjacency matrix row by row (0 means no edge):");
            for (int i = 0; i < n; i++) {
                System.out.print("Row " + (i + 1) + ": ");
                String[] values = sc.nextLine().trim().split("\\s+");
                if (values.length != n) throw new IllegalArgumentException("Each row needs " + n + " values.");
                for (int j = 0; j < n; j++) {
                    graph[i][j] = Integer.parseInt(values[j]);
                    if (graph[i][j] < 0) throw new IllegalArgumentException("Weights cannot be negative.");
                }
            }
            prims(graph);
        } catch (RuntimeException error) {
            System.out.println("Input error: " + error.getMessage());
        } finally { sc.close(); }
    }
}