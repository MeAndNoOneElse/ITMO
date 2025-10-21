import collections


def main():
    n = int(input().strip())
    if n == 2:
        print(1)
        return

    graph = [[] for _ in range(n + 1)]
    for _ in range(n - 1):
        a, b = map(int, input().split())
        graph[a].append(b)
        graph[b].append(a)

    # Находим корень - вершину с степенью > 1
    root = 1
    for i in range(1, n + 1):
        if len(graph[i]) > 1:
            root = i
            break

    # Строим дерево с корнем root
    children = [[] for _ in range(n + 1)]
    visited = [False] * (n + 1)
    order = []
    queue = collections.deque([root])
    visited[root] = True
    while queue:
        u = queue.popleft()
        order.append(u)
        for v in graph[u]:
            if not visited[v]:
                visited[v] = True
                children[u].append(v)
                queue.append(v)

    # Обрабатываем вершины в обратном порядке
    INF = 10 ** 9
    min1 = [INF] * (n + 1)
    min2 = [INF] * (n + 1)
    ans = INF

    for u in reversed(order):
        if not children[u]:  # лист
            min1[u] = 0
        else:
            for v in children[u]:
                current = min1[v] + 1
                if current < min1[u]:
                    min2[u] = min1[u]
                    min1[u] = current
                elif current < min2[u]:
                    min2[u] = current
            if min2[u] != INF:
                ans = min(ans, min1[u] + min2[u])

    if ans == INF:
        # Дерево является цепью
        ans = n - 1

    print(ans)


if __name__ == "__main__":
    main()