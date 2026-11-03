def prims_broadcast_tree(graph):
    n = len(graph)
    if n == 0: raise ValueError("Graph must contain at least one host.")
    selected = [False] * n
    selected[0] = True
    edges, total = [], 0

    while len(edges) < n - 1:
        minimum, start, end = float("inf"), -1, -1
        for i in range(n):
            if selected[i]:
                for j in range(n):
                    if not selected[j] and 0 < graph[i][j] < minimum:
                        minimum, start, end = graph[i][j], i, j
        if start == -1:
            print("The graph is disconnected; a spanning broadcast tree cannot be formed.")
            return
        selected[end] = True
        edges.append((start, end, graph[start][end]))
        total += graph[start][end]

    print("\nBroadcast tree edges:")
    print("Edge\tWeight")
    for start, end, weight in edges:
        print("{} - {}\t{}".format(start + 1, end + 1, weight))
    print("Total tree weight:", total)

def main():
    n = int(input("Enter number of hosts: "))
    if n <= 0: raise ValueError("Number of hosts must be positive.")
    graph = []
    print("Enter cost adjacency matrix row by row (0 means no edge):")
    for i in range(n):
        row = [int(value) for value in input("Row {}: ".format(i + 1)).split()]
        if len(row) != n or any(value < 0 for value in row):
            raise ValueError("Each row must contain {} non-negative values.".format(n))
        graph.append(row)
    prims_broadcast_tree(graph)

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)