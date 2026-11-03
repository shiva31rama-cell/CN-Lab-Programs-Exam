def print_tables(distance, next_hop, nodes):
    for source, node in enumerate(nodes):
        print("\nRouting table for node {}:".format(node))
        print("{:<14} {:<12} {}".format("Destination", "Next Hop", "Distance"))
        for dest, destination in enumerate(nodes):
            hop = "-" if source == dest or next_hop[source][dest] is None else next_hop[source][dest]
            cost = "INF" if distance[source][dest] == float("inf") else str(distance[source][dest])
            print("{:<14} {:<12} {}".format(destination, hop, cost))

def distance_vector(graph, nodes):
    n = len(nodes)
    distance = [row[:] for row in graph]
    next_hop = [[None] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            if i != j and graph[i][j] != float("inf"):
                next_hop[i][j] = nodes[j]

    print("\nInitial routing tables:")
    print_tables(distance, next_hop, nodes)
    iteration = 0
    while True:
        iteration += 1
        updated = False
        new_distance = [row[:] for row in distance]
        new_next = [row[:] for row in next_hop]
        for source in range(n):
            for destination in range(n):
                for neighbour in range(n):
                    link = graph[source][neighbour]
                    route = distance[neighbour][destination]
                    if link == float("inf") or route == float("inf"):
                        continue
                    candidate = link + route
                    if candidate < new_distance[source][destination]:
                        new_distance[source][destination] = candidate
                        new_next[source][destination] = nodes[neighbour]
                        updated = True
        distance, next_hop = new_distance, new_next
        print("\n--- Iteration {} ---".format(iteration))
        print_tables(distance, next_hop, nodes)
        if not updated: break
    print("\nRouting tables have converged.")

def main():
    n = int(input("Enter number of nodes: "))
    if n <= 0: raise ValueError("Number of nodes must be positive.")
    nodes = input("Enter node names separated by spaces: ").split()
    if len(nodes) != n: raise ValueError("Enter exactly {} node names.".format(n))
    graph = []
    print("Enter link-cost matrix row by row (-1 means no direct link):")
    for i in range(n):
        values = [int(value) for value in input("Row {}: ".format(i + 1)).split()]
        if len(values) != n: raise ValueError("Each row must contain {} values.".format(n))
        row = []
        for j, value in enumerate(values):
            if value < -1: raise ValueError("Use -1 for no link or non-negative cost.")
            row.append(0 if i == j else (float("inf") if value == -1 else value))
        graph.append(row)
    distance_vector(graph, nodes)

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)