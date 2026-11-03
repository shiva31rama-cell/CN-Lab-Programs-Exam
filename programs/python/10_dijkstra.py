import sys

def dijkstra(graph, names, source, destination):
    n = len(graph)
    distance = [sys.maxsize] * n
    visited = [False] * n
    parent = [-1] * n
    distance[source] = 0

    for _ in range(n):
        current, best = -1, sys.maxsize
        for vertex in range(n):
            if not visited[vertex] and distance[vertex] < best:
                current, best = vertex, distance[vertex]
        if current == -1:
            break
        visited[current] = True
        for neighbour, weight in enumerate(graph[current]):
            if weight > 0 and not visited[neighbour]:
                candidate = distance[current] + weight
                if candidate < distance[neighbour]:
                    distance[neighbour] = candidate
                    parent[neighbour] = current

    if distance[destination] == sys.maxsize:
        return None, sys.maxsize
    path, current = [], destination
    while current != -1:
        path.append(names[current])
        current = parent[current]
    return path[::-1], distance[destination]

def main():
    n = int(input("Enter number of vertices: "))
    if n <= 0: raise ValueError("Number of vertices must be positive.")
    names = input("Enter vertex names separated by spaces: ").split()
    if len(names) != n: raise ValueError("Enter exactly {} names.".format(n))
    graph = []
    print("Enter adjacency matrix row by row (0 means no edge):")
    for i in range(n):
        row = [int(value) for value in input("Row {}: ".format(i + 1)).split()]
        if len(row) != n or any(value < 0 for value in row):
            raise ValueError("Each row needs {} non-negative values.".format(n))
        graph.append(row)
    for i, name in enumerate(names): print("{}: {}".format(i + 1, name))
    source = int(input("Enter source position: ")) - 1
    destination = int(input("Enter destination position: ")) - 1
    if not (0 <= source < n and 0 <= destination < n):
        raise ValueError("Source or destination is out of range.")
    path, cost = dijkstra(graph, names, source, destination)
    if path is None:
        print("No path exists between the selected vertices.")
    else:
        print("Shortest path:", " -> ".join(path))
        print("Total cost:", cost)

if __name__ == "__main__":
    try:
        main()
    except ValueError as error:
        print("Input error:", error)