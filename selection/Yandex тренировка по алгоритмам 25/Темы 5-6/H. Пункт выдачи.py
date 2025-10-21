def main():
    n = int(input())
    a_list = list(map(int, input().split()))
    a = [0] + a_list

    g = [[] for _ in range(n + 1)]
    for _ in range(n - 1):
        u, v = map(int, input().split())
        g[u].append(v)
        g[v].append(u)

    total = sum(a)

    subtree = [0] * (n + 1)
    parent = [0] * (n + 1)

    stack = [(1, 0, 0)]
    while stack:
        u, p, state = stack.pop()
        if state == 0:
            parent[u] = p
            stack.append((u, p, 1))
            for v in g[u]:
                if v != p:
                    stack.append((v, u, 0))
        else:
            subtree[u] = a[u]
            for v in g[u]:
                if v != p:
                    subtree[u] += subtree[v]

    best_vertex = 1
    best_value = float('inf')

    stack = [(1, 0)]
    while stack:
        u, p = stack.pop()

        max_down = 0
        for v in g[u]:
            if v != p:
                if subtree[v] > max_down:
                    max_down = subtree[v]

        external = total - subtree[u]
        max_queue = max(a[u], max_down, external)

        if max_queue < best_value:
            best_value = max_queue
            best_vertex = u

        for v in g[u]:
            if v != p:
                stack.append((v, u))

    print(best_vertex)


if __name__ == "__main__":
    main()